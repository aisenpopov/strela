package ru.strela.web.controller.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.TransactionFilter;
import ru.strela.model.payment.Transaction;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping("/account/transaction")
public class TransactionController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        TransactionFilter filter = new TransactionFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("date", OrderDirection.Desc));
        Person person = personServer.getCurrentPerson();
        if (person != null && !person.isAdmin()) {
            filter.setPerson(person);
        }
        Page<Transaction> page = paymentService.findTransactions(filter, pageNumber - 1, pageSize);
        for (Transaction transaction : page) {
            JsonData jsonData = data.createCollection("transactions");

            jsonData.put("id", transaction.getId());
            jsonData.put("date", DateUtils.formatFull(transaction.getDate()));
            jsonData.put("amount", transaction.getAmount());
            Athlete athlete = personService.findByPerson(transaction.getPerson());
            jsonData.put("operator", athlete != null ? athlete.getDisplayName() : "");
        }

        JsonData pageDate = data.addJsonData("page");
        pageDate.put("number", page.getNumber());
        pageDate.put("totalPages", page.getTotalPages());

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Transaction transaction = paymentService.findById(new Transaction(id));
        if (transaction != null) {
            JsonData jsonData = data.addJsonData("transaction");

            jsonData.put("date", DateUtils.formatDDMMYYYY(transaction.getDate()));
            jsonData.put("amount", transaction.getAmount());

            Person operator = transaction.getPerson();
            jsonData.put("operatorId", operator.getId());

            String operatorString = operator.getLogin();
            Athlete athlete = personService.findByPerson(transaction.getPerson());
            if (athlete != null) {
                operatorString += ", " + athlete.getDisplayName();
            }
            jsonData.put("operator", operatorString);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Transaction(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(Transaction transaction, BindingResult result) {
        JsonResponse response = new JsonResponse();
        if (validate(transaction, response)) {
            Transaction existTransaction = paymentService.findById(transaction);
            existTransaction.setPerson(transaction.getPerson());
            existTransaction.setDate(transaction.getDate());
            existTransaction.setAmount(transaction.getAmount());

            paymentService.save(existTransaction);
        }

        return response;
    }

    private boolean validate(Transaction transaction, JsonResponse response) {
        Double amount = transaction.getAmount();
        if (transaction.getPerson() == null) {
            response.addFieldMessage("operator", FIELD_REQUIRED);
        }
        if (transaction.getDate() == null) {
            response.addFieldMessage("date", FIELD_REQUIRED);
        }
        if (amount == null) {
            response.addFieldMessage("amount", FIELD_REQUIRED);
        } else if (amount <= 0.0d) {
            response.addFieldMessage("amount", POSITIVE_NUMBER);
        }

        return !response.isStatusError();
    }

}

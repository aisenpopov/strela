package ru.strela.editor.controller.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.TransactionFilter;
import ru.strela.model.payment.Transaction;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.ajax.JsonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/editor/transaction")
public class EditorTransactionController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") TransactionFilter filter) {
        ModelBuilder model = new ModelBuilder("editor/transactions");
        if (filter == null) {
            filter = new TransactionFilter();
        }
        filter.addOrder(new Order("date", OrderDirection.Desc));
        Person person = personServer.getCurrentPerson();
        if (person != null && !person.isAdmin()) {
            filter.setPerson(person);
        }
        Page<Transaction> page = paymentService.findTransactions(filter, pageNumber - 1, pageSize);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Transaction transaction : page) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("transaction", transaction);
            item.put("athlete", personService.findByPerson(transaction.getPerson()));
            list.add(item);
        }
        model.put("page", new PageImpl<Map<String, Object>>(list));

        return model;
    }

    @RequestMapping(value = "/edit/{id}/", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable int id) {
        return getModel(id, true);
    }

    @RequestMapping(value = "/edit/{id}/", method = RequestMethod.POST)
    public ModelAndView save(Transaction transaction, BindingResult result) {
        if (validate(transaction, result)) {
            Transaction existTransaction = paymentService.findById(transaction);
            existTransaction.setPerson(transaction.getPerson());
            existTransaction.setDate(transaction.getDate());
            existTransaction.setAmount(transaction.getAmount());

            paymentService.save(existTransaction);

            return new Redirect("/editor/transaction/edit/" + existTransaction.getId() + "/");
        }

        return getModel(transaction.getId(), false);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Transaction(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    private ModelAndView getModel(int id, boolean insertTransaction) {
        ModelBuilder model = new ModelBuilder("editor/editTransaction");

        Transaction transaction = paymentService.findById(new Transaction(id));
        if (insertTransaction) {
            model.put("transaction", transaction);
        }
        model.put("athlete", personService.findByPerson(transaction.getPerson()));

        return model;
    }

    private boolean validate(Transaction transaction, BindingResult result) {
        Double amount = transaction.getAmount();
        if (transaction.getPerson() == null) {
            result.rejectValue("person", "field.required", FIELD_REQUIRED);
        }
        if (transaction.getDate() == null) {
            result.rejectValue("date", "field.required", FIELD_REQUIRED);
        }
        if (amount == null) {
            result.rejectValue("amount", "field.required", FIELD_REQUIRED);
        } else if (amount <= 0.0d) {
            result.rejectValue("amount", "field.required", POSITIVE_NUMBER);
        }

        return !result.hasErrors();
    }

}

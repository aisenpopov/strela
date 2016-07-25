package ru.strela.web.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.auth.Person;
import ru.strela.model.payment.PersonAccount;
import ru.strela.model.payment.Transaction;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/account/balance")
public class BalanceController extends WebController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView get() {
        return new ModelAndView("account/balance");
    }

    @ResponseBody
    @RequestMapping(value = "/getBalance", method = RequestMethod.POST)
    public JsonResponse getBalance() {
        JsonResponse response = new JsonResponse();

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            PersonAccount personAccount = paymentService.findByPerson(person);
            response.addData("balance", personAccount.getAccount());
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    public JsonResponse debit(@RequestBody Map<String, Object> json) {
        JsonResponse response = new JsonResponse();
        response.setErrorStatus();

        Double amount = null;
        try {
            amount = Double.parseDouble((String) json.get("amount"));
        } catch (NumberFormatException e) {
            response.addData("errorMessage", "Введите число!");
            return response;
        }

        Person person = personServer.getCurrentPerson();
        if (person != null && amount != null) {
            PersonAccount personAccount = paymentService.findByPerson(person);

            if (amount <= 0.0d) {
                response.addData("errorMessage", "Сумма должна быть положительной!");
                return response;
            } else if (amount > personAccount.getAccount()) {
                response.addData("errorMessage", "Сумма должна быть не больше баланса!");
                return response;
            }

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setPerson(person);
            transaction.setDate(new Date());

            paymentService.save(transaction);
        }

        response.setSuccessStatus();
        return response;
    }

}

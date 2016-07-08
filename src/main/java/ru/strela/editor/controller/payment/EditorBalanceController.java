package ru.strela.editor.controller.payment;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.auth.Person;
import ru.strela.model.payment.PersonAccount;
import ru.strela.model.payment.Transaction;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;

import java.util.Date;

/**
 * Created by Aisen on 08.07.2016.
 */
@Controller
@RequestMapping("/editor/balance")
public class EditorBalanceController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView get() {
        return getModel(true);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView save(Transaction transaction, BindingResult result) {
        if (validate(transaction, result)) {
            Person person = personServer.getCurrentPerson();
            if (person != null) {
                transaction.setPerson(person);
                transaction.setDate(new Date());

                paymentService.save(transaction);
            }

            return new Redirect("/editor/balance/");
        }

        return getModel(false);
    }

    private ModelAndView getModel(boolean insertTransaction) {
        ModelBuilder model = new ModelBuilder("editor/balance");

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            PersonAccount personAccount = paymentService.findByPerson(person);
            model.put("personAccount", personAccount);

            if (insertTransaction) {
                model.put("transaction", new Transaction());
            }
        }

        return model;
    }

    private boolean validate(Transaction transaction, BindingResult result) {
        Double amount = transaction.getAmount();
        if (amount == null) {
            result.rejectValue("amount", "field.required", FIELD_REQUIRED);
        } else {
            if (amount <= 0.0d) {
                result.rejectValue("amount", "field.required", POSITIVE_NUMBER);
            }
            Person person = personServer.getCurrentPerson();
            if (person != null) {
                PersonAccount personAccount = paymentService.findByPerson(person);
                if (personAccount == null || personAccount.getAccount() < amount) {
                    result.rejectValue("amount", "field.required", "Нельзя списать больше денег чем имеется в балансе");
                }
            }
        }

        return !result.hasErrors();
    }

}

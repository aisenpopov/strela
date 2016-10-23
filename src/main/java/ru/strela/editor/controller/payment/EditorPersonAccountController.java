package ru.strela.editor.controller.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PersonAccountFilter;
import ru.strela.model.payment.PersonAccount;
import ru.strela.util.ModelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/editor/person_account")
public class EditorPersonAccountController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") PersonAccountFilter filter) {
        ModelBuilder model = new ModelBuilder("editor/personAccounts");
        if (filter == null) {
            filter = new PersonAccountFilter();
        }
        filter.addOrder(new Order("id", OrderDirection.Desc));
        Page<PersonAccount> page = paymentService.findPersonAccounts(filter, pageNumber - 1, pageSize);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (PersonAccount personAccount : page) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("personAccount", personAccount);
            item.put("athlete", personService.findByPerson(personAccount.getPerson()));
            list.add(item);
        }
        model.put("page", new PageImpl<Map<String, Object>>(list));

        return model;
    }

}

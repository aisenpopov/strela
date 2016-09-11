package ru.strela.web.controller.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Team;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.TeamFilter;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.validate.JsonResponseValidateAdapter;
import ru.strela.web.controller.core.WebController;
import ru.strela.web.controller.dto.TeamDTO;

@Controller
@RequestMapping("/account/team")
public class TeamController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        TeamFilter filter = new TeamFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Team> page = applicationService.findTeams(filter, pageNumber - 1, pageSize);
        for (Team t : page) {
            data.addCollectionItem("teams", new TeamDTO(t));
        }

        fillPage(data, page);

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getTeam", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "id", defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Team team = applicationService.findById(new Team(id));
        if (team != null) {
            data.put("team", new TeamDTO(team));
        }
        
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(Team team) {
        JsonResponse response = new JsonResponse();

    	if (applicationService.validateTeam(team, new JsonResponseValidateAdapter(response))) {

            applicationService.saveTeam(team);

        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new Team(id));
        } catch(Exception e) {
            response.setErrorStatus();
        }
        return response;
    }

}

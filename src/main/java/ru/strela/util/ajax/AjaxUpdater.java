package ru.strela.util.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxUpdater {
	
	@SuppressWarnings("unchecked")
	public void ajaxUpdate(HttpServletRequest req, HttpServletResponse res, String id) {
		List<String> updates = (List<String>)req.getAttribute("ajaxUpdate");
		if(updates == null) {
			updates = new ArrayList<String>();
			req.setAttribute("ajaxUpdate", updates);
		}
		updates.add(id);
	}
	
}

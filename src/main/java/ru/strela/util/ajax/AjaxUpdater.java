package ru.strela.util.ajax;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class AjaxUpdater {
	
	@SuppressWarnings("unchecked")
	public void ajaxUpdate(HttpServletRequest req, String id) {
		List<String> updates = (List<String>)req.getAttribute("ajaxUpdate");
		if(updates == null) {
			updates = new ArrayList<String>();
			req.setAttribute("ajaxUpdate", updates);
		}
		updates.add(id);
	}
	
}

package ru.strela.model.filter;

public class PersonFilter extends OrderFilter {

	private String query;
	private String login;

	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}

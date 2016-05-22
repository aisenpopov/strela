package ru.strela.model.filter;


public class BaseFilter extends OrderFilter {

	private String query;
	private Boolean visible;
	private PermissionFilter permissionFilter;
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public PermissionFilter getPermissionFilter() {
		return permissionFilter;
	}

	public void setPermissionFilter(PermissionFilter permissionFilter) {
		this.permissionFilter = permissionFilter;
	}
}
package ru.strela.model.filter;


public class BaseFilter extends OrderFilter {

	/**
	 * Параметры пэйджинга.
	 */
	private int pageNumber = 0;
	private int pageSize = 1;

	private String query;
	private Boolean visible;

	/**
	 * Фильтр для ролей.
	 */
	private PermissionFilter permissionFilter;


	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

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
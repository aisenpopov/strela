package ru.strela.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.OrderFilter;

public class PageRequestBuilder {

    public static PageRequest build(OrderFilter filter, int pageNumber, int pageSize) {
        if(filter.getOrders() != null) {
        	List<Sort.Order> orders = new ArrayList<>();
        	for(Order order : filter.getOrders()) {
        		orders.add(new Sort.Order(order.getDirection() == OrderDirection.Asc ? Sort.Direction.ASC : Sort.Direction.DESC, order.getField()));
        	}
        	return new PageRequest(pageNumber, pageSize, new Sort(orders));
        }
    	return new PageRequest(pageNumber, pageSize);
    }
    
    public static Sort getSort(OrderFilter filter) {
    	if(filter.getOrders() != null) {
        	List<Sort.Order> orders = new ArrayList<>();
        	for(Order order : filter.getOrders()) {
        		orders.add(new Sort.Order(order.getDirection() == OrderDirection.Asc ? Sort.Direction.ASC : Sort.Direction.DESC, order.getField()));
        	}
        	return new Sort(orders);
        }
    	return null;
    }

}

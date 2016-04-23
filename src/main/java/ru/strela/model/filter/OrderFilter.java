package ru.strela.model.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class OrderFilter {

    private List<Order> orders;
    
    public List<Order> getOrders() {
		return orders;
	}
    
    public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
    
    public void addOrder(Order order) {
    	if(orders == null) {
    		orders = new ArrayList<>();
    	}
    	orders.add(order);
    }
    
}

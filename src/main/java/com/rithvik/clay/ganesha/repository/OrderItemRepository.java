package com.rithvik.clay.ganesha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rithvik.clay.ganesha.entity.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
	
	@Query("SELECT o FROM OrderItem o WHERE o.orderId = ?1")
	OrderItem findOrderItemById(String orderId);
	
	@Query("SELECT o FROM OrderItem o WHERE o.orderStatus=1")
	List<OrderItem> findDeliveredOrders();
	
	@Query("SELECT o FROM OrderItem o WHERE o.orderStatus=0")
	List<OrderItem> findPendingdOrders();
	
}
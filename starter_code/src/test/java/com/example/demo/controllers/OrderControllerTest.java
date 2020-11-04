package com.example.demo.controllers;

import com.example.demo.GenerateTestEntity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepo =mock(OrderRepository.class);
    private UserRepository userRepo =mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
        TestUtils.injectObject(orderController, "userRepository", userRepo);
    }

    @Test
    public void test_submit_order () throws  Exception{

        UserOrder order = GenerateTestEntity.generateOneUserOrder();
        User user = GenerateTestEntity.generateOneUser();
        when(userRepo.findByUsername("test")).thenReturn(user);

        String userName="test";
        final ResponseEntity<UserOrder> response = orderController.submit(userName);
        UserOrder o= response.getBody();
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("test",o.getUser().getUsername());
        assertEquals(new BigDecimal(1000),o.getTotal());
        assertEquals("test",o.getItems().get(0).getName());

    }
    @Test
    public void test_get_order () throws  Exception{

        UserOrder order = GenerateTestEntity.generateOneUserOrder();
        User user = GenerateTestEntity.generateOneUser();
        when(userRepo.findByUsername("test")).thenReturn(user);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepo.findByUser(user)).thenReturn(orderList);

        String userName="test";
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(userName);
        List<UserOrder> orders = response.getBody();
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("test",orders.get(0).getUser().getUsername());
        assertEquals(new BigDecimal(1000),orders.get(0).getUser().getCart().getTotal());
        assertEquals("test",orders.get(0).getItems().get(0).getName());

    }
}

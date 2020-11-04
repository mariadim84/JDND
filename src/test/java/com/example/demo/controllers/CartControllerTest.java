package com.example.demo.controllers;

import com.example.demo.GenerateTestEntity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private CartRepository cartRepo =mock(CartRepository.class);
    private UserRepository userRepo =mock(UserRepository.class);
    private ItemRepository itemRepo =mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();

        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
        TestUtils.injectObject(cartController, "userRepository", userRepo);
    }

    @Test
    public void test_add_to_cart()throws Exception {

        User user = GenerateTestEntity.generateOneUser();
        Item item = GenerateTestEntity.generateOneItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(0);
        request.setUsername("test");
        final ResponseEntity<Cart> response = cartController.addTocart(request);

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new Long(0), c.getItems().get(0).getId());
        assertEquals("test", c.getUser().getUsername());
        assertEquals(2, c.getItems().size());
        assertEquals(new BigDecimal(1200),c.getTotal());
    }

    @Test
    public void test_remove_from_cart()throws Exception {

        User user = GenerateTestEntity.generateOneUser();
        Item item = GenerateTestEntity.generateOneItem();
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(0);
        request.setUsername("test");
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", c.getUser().getUsername());
        assertEquals(0, c.getItems().size());
        assertEquals(new BigDecimal(800),c.getTotal());
    }
}

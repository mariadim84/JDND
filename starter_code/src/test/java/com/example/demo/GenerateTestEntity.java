package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

    public class GenerateTestEntity {

        private static User userBaseData(){
            User user = new User();
            user.setId(0);
            user.setUsername("test");
            user.setPassword("thisIsHashed");
            return user;
        }

        private static Cart cartBaseData(){
            Cart cart = new Cart();
            cart.setId(new Long(0));
            cart.setTotal(new BigDecimal(1000.00));
            return cart;
        }

        public static Item generateOneItem(){
            Item item = new Item();
            item.setId(new Long(0));
            item.setPrice(new BigDecimal(200.00));
            item.setDescription("description");
            item.setName("test");
            return item;
        }

        public static User generateOneUser(){
            User user = userBaseData();
//        user.setCart(cartBaseData());
            Cart cart = new Cart();
            cart.setId(new Long(0));
            cart.setTotal(new BigDecimal(1000.00));
            cart.setUser(user);
            List<Item> items = new ArrayList<>();
            items.add(generateOneItem());
            cart.setItems(items);
            user.setCart(cart);
            return user;
        }

        public static  Cart generateOneCart(){
            Cart cart = cartBaseData();
            List<Item> items = new ArrayList<>();
            items.add(generateOneItem());
            cart.setItems(items);
//        cart.setUser(userBaseData());
            cart.setUser(generateOneUser());
            return cart;
        }

        public static UserOrder generateOneUserOrder(){
            UserOrder userOrder = new UserOrder();
            userOrder.setId(new Long(0));
            userOrder.setTotal(new BigDecimal(9999.99));
            userOrder.setUser(generateOneUser());
            List<Item> items = new ArrayList<>();
            items.add(generateOneItem());
            userOrder.setItems(items);
            return userOrder;
        }

    }

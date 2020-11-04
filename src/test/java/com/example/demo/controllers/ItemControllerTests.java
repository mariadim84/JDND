package com.example.demo.controllers;

import com.example.demo.GenerateTestEntity;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemController itemController;
    private ItemRepository itemRepo =mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void get_item_by_id_test() throws Exception
    {
        Item item = GenerateTestEntity.generateOneItem();
        when(itemRepo.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        long itemId = 0;
        final ResponseEntity<Item> response = itemController.getItemById(itemId);
        Item i = response.getBody();
        assertNotNull(i);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new Long(0), i.getId());
        assertEquals("test", i.getName());
        assertEquals("description", i.getDescription());
        assertEquals(new BigDecimal(200),i.getPrice());

    }
    @Test
    public void get_item_by_name_test() throws Exception
    {
        List <Item> item = new ArrayList<>();
        item.add(GenerateTestEntity.generateOneItem());
        when(itemRepo.findByName("test")).thenReturn((List<Item>) item);

        String itemName="test";
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(itemName);
        List<Item> items = response.getBody();
        assertNotEquals(0,items.size());
        Item i = items.get(0);
        assertNotNull(i);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new Long(0), i.getId());
        assertEquals("test", i.getName());
        assertEquals("description", i.getDescription());
        assertEquals(new BigDecimal(200),i.getPrice());
    }

    @Test
    public void get_all_items_test() throws Exception
    {
        List <Item> item = new ArrayList<>();
        item.add(GenerateTestEntity.generateOneItem());
        when(itemRepo.findAll()).thenReturn((List<Item>) item);

        final ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> items = response.getBody();
        assertNotEquals(0,items.size());
        Item i = items.get(0);
        assertNotNull(i);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new Long(0), i.getId());
        assertEquals("test", i.getName());
        assertEquals("description", i.getDescription());
        assertEquals(new BigDecimal(200),i.getPrice());

    }
}

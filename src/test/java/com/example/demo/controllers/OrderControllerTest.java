package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest extends BaseControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        User user = getUser();

        Item item = getItem();
        item.setId(1L);
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
//        TestUtils.injectObjects(orderController, "itemRepository", itemRepository);
    }

    @Test
    public void submit(){
        User user = getUser();

        Item item = getItem();
        item.setId(1L);

        Item item2 = getItem();
        item.setId(2L);

        Cart cart = new Cart();
        cart.addItem(item);
        cart.addItem(item2);
        user.setCart(cart);

        when(userRepository.findByUsername(any())).thenReturn(user);

        ResponseEntity<UserOrder> orderResponse = orderController.submit("stanislas");
        assertNotNull(orderResponse);
        assertEquals(200, orderResponse.getStatusCodeValue());

        UserOrder userOrder = orderResponse.getBody();

        assert(userOrder.getItems().containsAll(Arrays.asList(item, item2)));

    }

    private Item getItem(){
        Item item = new Item();
        item.setDescription("One Item");
        item.setName("item Name");
        item.setPrice(new BigDecimal(10));
        return item;
    }
}
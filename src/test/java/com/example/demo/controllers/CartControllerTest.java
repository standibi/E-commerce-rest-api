package com.example.demo.controllers;

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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest extends BaseControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        User user = getUser();

        Item item = getItem();
        item.setId(1L);
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);


    }

    @Test
    public void add_to_cart(){
        User user = getUser();

        Item item = getItem();
        item.setId(1L);

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest cartModifyRequest = getCartModifyRequest();
        cartModifyRequest.setItemId(1L);

        final ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(cartModifyRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart cart = cartResponseEntity.getBody();

        assert(cart.getItems().contains(item));

    }

    @Test
    public void remove_from_cart(){
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
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findById(2L)).thenReturn(java.util.Optional.of(item2));

        ModifyCartRequest cartModifyRequest = getCartModifyRequest();
        cartModifyRequest.setItemId(1L);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(cartModifyRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart returnedCart = cartResponseEntity.getBody();

        assert(returnedCart.getItems().contains(item2));
        assertThat(returnedCart.getItems(), not(hasItem(item)));

    }

    private Item getItem(){
        Item item = new Item();
        item.setDescription("One Item");
        item.setName("item Name");
        item.setPrice(new BigDecimal(10));
        return item;
    }

    private ModifyCartRequest getCartModifyRequest(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("stanislas");
        return modifyCartRequest;
    }

}
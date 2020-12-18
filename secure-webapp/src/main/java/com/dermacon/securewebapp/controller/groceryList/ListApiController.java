package com.dermacon.securewebapp.controller.groceryList;

import com.dermacon.securewebapp.controller.services.ItemService;
import com.dermacon.securewebapp.data.InputItem;
import com.dermacon.securewebapp.data.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/groceryList")
public class ListApiController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(path = "/getNewItems")
    public Iterable<Item> getNewItems() {
        return itemService.getSortedItems_nextPurchase();
    }

    @RequestMapping(path = "/getOldItems")
    public Iterable<Item> getOldItems() {
        return itemService.getSortedItems_prevPurchase();
    }


    @PostMapping(value = "/addItem")
    public boolean addItem(@RequestBody InputItem inputItem) {
        System.out.println(inputItem);
        return itemService.addItem(inputItem);
    }

    @DeleteMapping(value = "/removeItem/{id}")
    public boolean removeItem(@PathVariable long id) {
        System.out.println("actually doing something");
        return true;
    }
}

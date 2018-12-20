package com.testyle.service;

import com.testyle.model.Item;

import java.util.List;

public interface IItemService {
    List<Item> select(Item item);
    int insert(Item item);
    int delete(long itemID);
    int update(Item item);
    int insertList(List<Item> items);
}

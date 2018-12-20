package com.testyle.dao;

import com.testyle.model.Item;

import java.util.List;

public interface IItemDao {
    List<Item> select(Item item);
    int insert(Item item);
    int delete(long itemID);
    int update(Item item);
    int insertList(List<Item> items);
}

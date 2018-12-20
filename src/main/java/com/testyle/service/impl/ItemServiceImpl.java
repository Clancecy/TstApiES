package com.testyle.service.impl;

import com.testyle.dao.IItemDao;
import com.testyle.model.Item;
import com.testyle.service.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("itemService")
@Transactional
public class ItemServiceImpl implements IItemService {

    @Resource
    private IItemDao itemDao;

    @Override
    public List<Item> select(Item item) {
        return itemDao.select(item);
    }

    @Override
    public int insert(Item item) {
        return itemDao.insert(item);
    }

    @Override
    public int delete(long itemID) {
        return itemDao.delete(itemID);
    }

    @Override
    public int update(Item item) {
        return itemDao.update(item);
    }
    @Override
    public int insertList(List<Item> items) {
        return itemDao.insertList(items);
    }

}

package com.foresight.test.repo;

import com.foresight.test.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepo extends CrudRepository<Item, String> {
    @Override
    List<Item> findAll();


    Item findByParentUid(String uid);
}

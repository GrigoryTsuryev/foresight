package com.foresight.test.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foresight.test.api.ItemsApiDelegate;
import com.foresight.test.entity.Item;
import com.foresight.test.model.ItemDTO;
import com.foresight.test.repo.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ItemApiDelegateImpl implements ItemsApiDelegate {

    @Autowired
    ItemRepo itemRepo;


    @Autowired
    ObjectMapper mapper;

    @Override
    public ResponseEntity<List<ItemDTO>> getItems() {
        List<Item> items = itemRepo.findAll();
        List<ItemDTO> itemsDTO = mapper.convertValue(items, new TypeReference<>() {});
        return ResponseEntity.ok(itemsDTO);
    }

    @Override
    public ResponseEntity<BigDecimal> calculateStatus(String uid) {
        Item item = itemRepo.findById(uid).orElse(null);
        LocalDate now = LocalDate.now();
        if (item.getEndDate().isBefore(now))    return ResponseEntity.ok(BigDecimal.ONE);
        if (item.getStartDate().isAfter(now))    return ResponseEntity.ok(BigDecimal.ZERO);
        long projectDays = ChronoUnit.DAYS.between(item.getEndDate(), item.getStartDate());
        long daysleft = ChronoUnit.DAYS.between(now, item.getStartDate());
        long status = daysleft * 100 / projectDays;
        return ResponseEntity.ok(BigDecimal.valueOf(status));
    }

    @Override
    public ResponseEntity<Boolean> deleteItemById(String uid) {
        Item item = itemRepo.findByParentUid(uid);
        if(item !=null) ResponseEntity.ok(Boolean.FALSE);
        itemRepo.deleteById(uid);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}

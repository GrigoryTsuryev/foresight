package com.foresight.test.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;


@Data
public class Items {

    private List<Item> items;
}

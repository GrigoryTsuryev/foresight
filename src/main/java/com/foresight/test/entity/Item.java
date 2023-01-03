package com.foresight.test.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Item {

    @Id
    private String uid;
    private String name;
    private String type;
    private String parentUid;
    private LocalDate startDate;
    private LocalDate endDate;


}
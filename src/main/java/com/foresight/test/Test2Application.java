package com.foresight.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foresight.test.constants.Type;
import com.foresight.test.entity.Item;
import com.foresight.test.entity.Items;
import com.foresight.test.repo.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class Test2Application {
    @Value("classpath:proj.json")
    Resource projJson;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemRepo itemRepo;

    public static void main(String[] args) {
        SpringApplication.run(Test2Application.class, args);
    }

    @Bean
    ApplicationRunner appStarted() {
        return args -> {
            File jsonFile = projJson.getFile();
            Items items = objectMapper.readValue(jsonFile, Items.class);
            List<Item> tasks = items.getItems().stream().filter(item -> item.getType().equalsIgnoreCase(Type.TASK.name())).collect(Collectors.toList());
            Map<String, Item> itemsMap = items.getItems().stream().collect(Collectors.toMap(Item::getUid, Function.identity()));
			tasks.forEach(task->getParentAndSetDates(itemsMap, task));
			itemRepo.saveAll(itemsMap.values());
        };
    }

    private void getParentAndSetDates(Map<String, Item> itemsMap, Item task) {
        Item parentItem = itemsMap.getOrDefault(task.getParentUid(), null);
        LocalDate startDate = task.getStartDate();
        LocalDate endDate = task.getEndDate();
        if (parentItem != null) {
            if (parentItem.getStartDate() == null || parentItem.getStartDate().isAfter(startDate)) {
                parentItem.setStartDate(startDate);
            }
            if (parentItem.getEndDate() == null || parentItem.getEndDate().isBefore(endDate)) {
				parentItem.setEndDate(endDate);
			}
			getParentAndSetDates(itemsMap, parentItem);
        }
    }
}

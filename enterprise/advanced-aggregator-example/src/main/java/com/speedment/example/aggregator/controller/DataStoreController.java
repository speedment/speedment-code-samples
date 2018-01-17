package com.speedment.example.aggregator.controller;

import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("datastore")
public class DataStoreController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataStoreController.class);

    private @Autowired DataStoreComponent dataStore;

    @PostMapping
    void reload() {
        LOGGER.info("Reloading Speedment data.");
        dataStore.reload();
    }

    @DeleteMapping
    void clear() {
        LOGGER.info("Clearing Speedment data.");
        dataStore.clear();
    }
}
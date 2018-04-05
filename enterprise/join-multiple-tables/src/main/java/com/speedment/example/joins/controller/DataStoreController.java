package com.speedment.example.joins.controller;

import com.speedment.common.json.Json;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Emil Forslund
 * @since  1.2.0
 */
@RestController
@RequestMapping("datastore")
public class DataStoreController {

    @Autowired DataStoreComponent dataStore;

    @PostConstruct
    void loadInitialData() {
        dataStore.load();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    String showStatistics() {
        return Json.toJson(dataStore.getStatistics());
    }

    @PostMapping
    void reloadData() {
        dataStore.reload();
    }

    @DeleteMapping
    void release() {
        dataStore.clear();
    }
}
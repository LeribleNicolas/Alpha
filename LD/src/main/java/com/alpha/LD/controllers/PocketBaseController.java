package com.alpha.LD.controllers;

import com.alpha.LD.services.PocketBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PocketBaseController {

    private final PocketBaseService pocketBaseService;

    @Autowired
    public PocketBaseController(PocketBaseService pocketBaseService) {
        this.pocketBaseService = pocketBaseService;
        // Autenticar el administrador al inicio (puedes mover esto a un controlador separado para un mejor control)
        pocketBaseService.authenticateAdmin("adorado51@gmail.com", "BPUkjr2cUABrej4");
    }

    @GetMapping("/collections/{collectionName}/records")
    public List<Object> getAllRecords(@PathVariable String collectionName) {
        return pocketBaseService.getAllRecords(collectionName);
    }

    @GetMapping("/collections/{collectionName}/records/{id}")
    public Object getRecordById(@PathVariable String collectionName, @PathVariable String id) {
        return pocketBaseService.getRecordById(collectionName, id);
    }

    @PostMapping("/collections/{collectionName}/records")
    public Object createRecord(@PathVariable String collectionName, @RequestBody Object record) {
        return pocketBaseService.createRecord(collectionName, record);
    }

    @PutMapping("/collections/{collectionName}/records/{id}")
    public void updateRecord(@PathVariable String collectionName, @PathVariable String id, @RequestBody Object record) {
        pocketBaseService.updateRecord(collectionName, id, record);
    }

    @DeleteMapping("/collections/{collectionName}/records/{id}")
    public void deleteRecord(@PathVariable String collectionName, @PathVariable String id) {
        pocketBaseService.deleteRecord(collectionName, id);
    }
}

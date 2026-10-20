package rs.ac.uns.ftn.ci.isa.blob_storage.images.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ci.isa.blob_storage.images.dto.GetPutUrlResponse;
import rs.ac.uns.ftn.ci.isa.blob_storage.images.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("api/images/whatToDo")
@CrossOrigin(value = "*", exposedHeaders = "Location")
public class WhatToDoImageController {
    private final ImageService service;


    public WhatToDoImageController(ImageService service) {
        this.service = service;
    }

    @GetMapping("url/{key}")
    public ResponseEntity<String> getGetUrl(@PathVariable(value = "key") String key) throws IOException {
        var response = service.generateGetUrl(key);
        return ResponseEntity.ok(response);
    }

    @PutMapping("url")
    public ResponseEntity<GetPutUrlResponse> getPutUrl() throws IOException {
        var response = service.generatePutUrl();
        return ResponseEntity.ok(response);
    }
}


package com.ocr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ocr.service.AzureVisionService;

@RestController
@RequestMapping("/api/ocr/azure")
public class AzureVisionController {

    @Autowired
    private AzureVisionService azureVisionService;

    //http://localhost:9095/api/ocr/azure/extract
    @PostMapping("/extract")
    public ResponseEntity<String> extract(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String text = azureVisionService.extractText(bytes);
            return ResponseEntity.ok(text);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Extraction failed: " + e.getMessage());
        }
    }
}

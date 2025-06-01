package com.ocr.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ocr.service.OcrService;

import net.sourceforge.tess4j.TesseractException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {
	
    @Autowired
    private OcrService ocrService;

    @PostMapping("/extract")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        String result = ocrService.extractText(convFile);
        return ResponseEntity.ok(result);
    }
}

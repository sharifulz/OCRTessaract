package com.ocr.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.ocr.service.HandwritingOCRService;
import com.ocr.service.OcrService;

import net.sourceforge.tess4j.TesseractException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {
	
    @Autowired
    private OcrService ocrService;

    @Autowired
    private HandwritingOCRService googleCloudService;
    
    @PostMapping("/extract")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        String result = ocrService.extractText(convFile);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/extract/google")
    public ResponseEntity<String> extract(@RequestParam("file") MultipartFile file) {
        try {
            // Load service account credentials from classpath
            InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("handwriting-extract-ced9c3054691.json");
            if (credentialsStream == null) {
                return ResponseEntity.status(500).body("Google credentials file not found in resources.");
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

            // Save the uploaded file temporarily
            File tempFile = File.createTempFile("upload-", "-" + file.getOriginalFilename());
            file.transferTo(tempFile);

            // Call service to extract text
            String text;
            try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
                text = googleCloudService.extractTextFromImage(tempFile.getAbsolutePath(), vision);
            }

            // Clean up temp file
            tempFile.delete();

            return ResponseEntity.ok(text);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}

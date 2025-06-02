package com.ocr.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;

import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

@Configuration
public class VisionClientFactory {

    public static ImageAnnotatorClient createClient() throws Exception {
        InputStream credentialsStream = VisionClientFactory.class
            .getClassLoader()
            .getResourceAsStream("handwriting-extract-ced9c3054691.json");

        if (credentialsStream == null) {
            throw new IllegalStateException("Could not find handwriting-extract-ced9c3054691.json in resources folder");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        return ImageAnnotatorClient.create(settings);
    }
}

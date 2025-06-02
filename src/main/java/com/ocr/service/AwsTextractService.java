package com.ocr.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocr.config.AwsProperties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

@Service
public class AwsTextractService {

    private final TextractClient textractClient;

    @Autowired
    public AwsTextractService(AwsProperties awsProperties) {
        this.textractClient = TextractClient.builder()
						                .region(Region.of(awsProperties.getRegion()))
						                .credentialsProvider(StaticCredentialsProvider.create(
						                        AwsBasicCredentials.create(
						                                awsProperties.getAccessKey(),
						                                awsProperties.getSecretKey()
						                        )))
						                .build();
    }

    public String extractTextFromImage(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        Document document = Document.builder().bytes(SdkBytes.fromByteArray(fileContent)).build();

        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder().document(document).build();

        DetectDocumentTextResponse result = textractClient.detectDocumentText(request);

        StringBuilder extractedText = new StringBuilder();
        for (Block block : result.blocks()) {
            if (block.blockType() == BlockType.LINE) {
                extractedText.append(block.text()).append("\n");
            }
        }

        return extractedText.toString();
    }
}

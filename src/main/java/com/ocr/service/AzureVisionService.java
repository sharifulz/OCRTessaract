package com.ocr.service;

import org.springframework.stereotype.Service;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentPage;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.ocr.config.AzureVisionProperties;

@Service
public class AzureVisionService {

    private final DocumentAnalysisClient client;

    public AzureVisionService(AzureVisionProperties properties) {
    	
    	System.out.println("Key: "+ properties.getKey());
    	System.out.println("Endpoint: "+ properties.getEndpoint());

        this.client = new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(properties.getKey()))
                .endpoint(properties.getEndpoint())
                .buildClient();
    }

    public String extractText(byte[] imageBytes) {
        try {
            // Convert byte array to BinaryData
            BinaryData document = BinaryData.fromBytes(imageBytes);

            // Start the document analysis process
            SyncPoller<OperationResult, AnalyzeResult> poller = client.beginAnalyzeDocument("prebuilt-read", document);

            AnalyzeResult result = poller.getFinalResult();

            // Extract lines of text
            StringBuilder sb = new StringBuilder();
            for (DocumentPage page : result.getPages()) {
                page.getLines().forEach(line -> sb.append(line.getContent()).append("\n"));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text: " + e.getMessage(), e);
        }
    }
}

package com.ocr.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

@Service
public class HandwritingOCRService {

	public String extractTextFromImage(String imagePath, ImageAnnotatorClient vision) throws IOException {
	    // Read the image file into memory
	    ByteString imgBytes = ByteString.readFrom(new FileInputStream(imagePath));

	    // Build the image object
	    Image img = Image.newBuilder().setContent(imgBytes).build();

	    // Define the feature type (DOCUMENT_TEXT_DETECTION is suitable for handwriting and printed docs)
	    Feature feat = Feature.newBuilder()
	            .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
	            .build();

	    // Build the annotation request
	    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
	            .addFeatures(feat)
	            .setImage(img)
	            .build();

	    // Perform the annotation request
	    BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
	    AnnotateImageResponse res = response.getResponsesList().get(0);

	    // Check for errors in the response
	    if (res.hasError()) {
	        System.err.println("Error: " + res.getError().getMessage());
	        return "Failed to extract text: " + res.getError().getMessage();
	    }

	    // Return the full extracted text
	    return res.getFullTextAnnotation().getText();
	}
}




🧠 OCR with Google Cloud, AWS Textract, and Azure Document Intelligence

This project demonstrates how to extract text (including handwritten content) from images using:

- Google Cloud Vision API
- AWS Textract
- Azure Document Intelligence (Form Recognizer)

📁 Project Structure

- Java Spring Boot backend with endpoints for each provider
- HTML frontend to upload images and select provider
- Dynamic OCR processing based on selected provider

🔑 Credential Setup Guide

Below are detailed steps to get the required credentials and endpoints from each provider.

🔵 Google Cloud Vision API

✅ Step 1: Enable Vision API
1. Go to Google Cloud Console: https://console.cloud.google.com/
2. Create or select a project.
3. Navigate to APIs & Services > Library
4. Search for "Vision API" and click Enable

✅ Step 2: Create Service Account
1. Go to IAM & Admin > Service Accounts
2. Click Create Service Account
3. Assign a name and description
4. Assign the role: Project > Editor or Cloud Vision API User
5. Click Done

✅ Step 3: Generate JSON Key
1. After creating the service account, click on it.
2. Go to the Keys tab
3. Click Add Key > Create new key
4. Choose JSON, and download the key

✅ Step 4: Add to Project
Save the file as: src/main/resources/handwriting-extract-xxx.json

Sample Java loading code:
InputStream stream = getClass().getClassLoader().getResourceAsStream("handwriting-extract-xxx.json");
GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

🟠 AWS Textract

✅ Step 1: Create an IAM User
1. Go to AWS IAM Console: https://console.aws.amazon.com/iam/
2. Create a new user (e.g., textract-user)
3. Enable Programmatic access
4. Attach the policy: AmazonTextractFullAccess
5. Create the user and download the Access Key ID and Secret Access Key

✅ Step 2: Add to application.yml or use environment variables
application.yml:
aws:
  region: us-east-1
  accessKeyId: YOUR_ACCESS_KEY
  secretAccessKey: YOUR_SECRET_KEY

Environment variables:
export AWS_ACCESS_KEY_ID=your_key
export AWS_SECRET_ACCESS_KEY=your_secret

✅ Step 3: Call Textract from Java
TextractClient client = TextractClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(...)))
        .build();

🔵 Azure Document Intelligence (Form Recognizer)

✅ Step 1: Create Resource
1. Go to Azure Portal: https://portal.azure.com/
2. Search for "Azure AI Document Intelligence"
3. Click Create and configure the resource
4. Click Review + Create

✅ Step 2: Get Endpoint & Key
1. Go to the created resource
2. Navigate to Keys and Endpoint
3. Copy Key1 (or Key2) and Endpoint

✅ Step 3: Add to application.yml
azure:
  vision:
    endpoint: https://your-resource.cognitiveservices.azure.com/
    key: your_subscription_key

✅ Step 4: Use in Java Code
DocumentAnalysisClient client = new DocumentAnalysisClientBuilder()
        .endpoint(endpoint)
        .credential(new AzureKeyCredential(key))
        .buildClient();

✅ Sample API Endpoints

Provider | Endpoint
---------|-------------------------------
Google   | POST /api/ocr/extract/google
AWS      | POST /api/aws/textract/extract
Azure    | POST /api/ocr/azure/extract

💡 Frontend (HTML + JS)

Supports dynamic dropdown to choose provider and submit file:
<select id="providerSelect" class="form-control mb-3">
  <option value="google">Google Cloud</option>
  <option value="aws">AWS</option>
  <option value="azure">Azure</option>
</select>

🛡️ Security Notes

- Never commit your credential JSONs or keys to version control
- Use environment variables in production (GOOGLE_APPLICATION_CREDENTIALS, AWS_ACCESS_KEY_ID, etc.)
- Restrict IAM permissions to the least privilege needed

📦 Dependencies (Maven)

<!-- Google Cloud Vision -->
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-vision</artifactId>
  <version>3.21.0</version>
</dependency>

<!-- AWS Textract -->
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>textract</artifactId>
  <version>2.25.22</version>
</dependency>

<!-- Azure AI Document Intelligence -->
<dependency>
  <groupId>com.azure</groupId>
  <artifactId>azure-ai-formrecognizer</artifactId>
  <version>4.1.5</version>
</dependency>

✅ Summary

Provider | Setup Needed             | API                     | Key Format
-------- |-------------------------|-------------------------|-------------
Google   | Vision API + JSON       | google-cloud-vision     | Full JSON file
AWS      | IAM + Access Keys       | aws-textract            | Access & Secret Key
Azure    | Document Intelligence   | azure-ai-formrecognizer | Endpoint + Key

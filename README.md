
OCR Text Extraction API (Spring Boot + Tesseract + AI Handwriting Support)

This project is a RESTful API built with Spring Boot (Java 21) that allows you to:
- Upload an image containing text (printed or handwritten)
- Automatically extract the text using Tesseract OCR
- (Optionally) Use AI models to improve recognition of handwritten text

Features

- Upload image via REST API
- Preprocess image using OpenCV (grayscale + thresholding)
- OCR using Tesseract
- (Optional) Route to a Python-based AI microservice for better handwriting recognition
- Save extracted text to file (or return as response)

Getting Started

Prerequisites

Install the following on your Linux server (deployment) or Windows machine (development):

Java 21

sudo apt update
sudo apt install openjdk-21-jdk
java -version

Tesseract OCR

sudo apt install tesseract-ocr
sudo apt install tesseract-ocr-eng  # English language support

OpenCV (for preprocessing)
Install OpenCV native libraries or use opencv-java dependency.

Optional: AI Handwriting Recognition Microservice

If you want to improve results for handwritten input, deploy this Python microservice:

Python Handwriting API (using Hugging Face)

git clone https://github.com/sharifulz/OCRTessaract
cd OCRTessaract
pip install -r requirements.txt
python app.py

Then configure your Spring Boot project to call this via HTTP.

Build and Run the Project

Clone the repo:

git clone https://github.com/sharifulz/OCRTessaract.git
cd OCRTessaract

Run locally:

./mvnw spring-boot:run

Or run jar:

java -jar target/ocr-api-0.0.1-SNAPSHOT.jar

REST API Usage

Upload Image and Extract Text

curl -X POST -F "file=@OCR1.jpeg" http://localhost:9095/api/ocr/extract

Returns:

{
  "extractedText": "Your recognized text from image..."
}

Save Extracted HTML/Text

You can modify the API to:
- Return text as .html
- Save locally using Files.writeString()

SSL Certificate (Optional)

If exposing publicly via HTTPS:

cd /etc/letsencrypt/live/yourdomain.com/
mv springboot.p12 springboot.p12_old
openssl pkcs12 -export   -in fullchain.pem   -inkey privkey.pem   -out springboot.p12   -name springboot   -CAfile chain.pem -caname root

Update your application.properties:

server.ssl.key-store=classpath:springboot.p12
server.ssl.key-store-password=yourpassword
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=springboot

NGINX Fix for 404 Refresh

If your frontend shows 404 on page refresh, fix NGINX config:

Edit file:

sudo nano /etc/nginx/sites-available/yourdomain.com

Replace:

location / {
    try_files $uri $uri/ /index.html;
}

Then:

sudo ln -sf /etc/nginx/sites-available/yourdomain.com /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx

Testing

Use Postman, curl, or frontend form to upload .jpeg, .png, etc.

Resources

- Tesseract OCR Documentation: https://tesseract-ocr.github.io/
- Hugging Face Handwriting Models: https://huggingface.co/models?search=handwriting
- Spring Boot Documentation: https://spring.io/projects/spring-boot

Author

Shariful Islam – https://github.com/sharifulz/

License

This project is licensed under the MIT License.

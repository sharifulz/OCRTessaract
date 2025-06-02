document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("uploadForm");
    const imageInput = document.getElementById("fileInput");
    const previewImg = document.getElementById("uploadedImage");
    const outputSection = document.getElementById("result-section");
    const extractedTextArea = document.getElementById("extractedText");

    if (!form || !imageInput || !previewImg || !outputSection || !extractedTextArea) {
        console.error("One or more required DOM elements are missing.");
        return;
    }

    imageInput.addEventListener("change", function () {
        const file = imageInput.files[0];
        if (file) {
            previewImg.src = URL.createObjectURL(file);
            previewImg.style.display = "block";
        } else {
            previewImg.style.display = "none";
        }
    });

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const file = imageInput.files[0];
        if (!file) {
            alert("Please select a file to upload.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        fetch("/api/ocr/extract/google", {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server responded with status ${response.status}`);
                }
                return response.text();
            })
            .then((text) => {
                extractedTextArea.textContent = text;
                outputSection.classList.remove("d-none");
            })
            .catch((error) => {
                console.error("Fetch error:", error);
                alert("Failed to extract text: " + error.message);
            });
    });
});

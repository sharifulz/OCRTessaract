document.addEventListener("DOMContentLoaded", function() {
	const form = document.getElementById("uploadForm");
	const imageInput = document.getElementById("fileInput");
	const previewImg = document.getElementById("uploadedImage");
	const outputSection = document.getElementById("result-section");
	const extractedTextArea = document.getElementById("extractedText");

	if (!form || !imageInput || !previewImg || !outputSection || !extractedTextArea) {
		console.error("One or more required DOM elements are missing.");
		return;
	}

	imageInput.addEventListener("change", function() {
		const file = imageInput.files[0];
		if (file) {
			previewImg.src = URL.createObjectURL(file);
			previewImg.style.display = "block";
		} else {
			previewImg.style.display = "none";
		}
	});

	form.addEventListener("submit", function(e) {
		e.preventDefault();

		var provider = document.getElementById('providerSelect').value;

		const file = imageInput.files[0];
		if (!file) {
			alert("Please select a file to upload.");
			return;
		}

		const formData = new FormData();
		formData.append("file", file);

		let url = "";
		if (provider === "google") {
			url = "/api/ocr/extract/google";
		} else if (provider === "aws") {
			url = "/api/aws/textract/extract";
		} else if (provider === "azure") {
			url = "/api/ocr/azure/extract";
		}

		// Show loader
		loader.style.display = "block";
		outputSection.classList.add("d-none"); // Hide output area while loading

		fetch(url, {
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
			})
			.finally(() => {
				loader.style.display = "none"; // Hide loader regardless of success or failure
			});
	});
});

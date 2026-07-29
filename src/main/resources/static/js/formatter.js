// ==========================================================
// FORMATTER.JS
// Common Input Formatter
// ==========================================================

document.addEventListener("DOMContentLoaded", () => {

    initializeNameFormatter();
    initializeEmailFormatter();
    initializeMobileFormatter();
    initializeAadhaarFormatter();
    initializeTrimFormatter();

    // clear validation
    initializeValidationCleaner();
    initializeResetFormatter();
    initializePhotoPreview();
    initializeFirstInvalidFocus();

});

// ==========================================================
// Name Formatter
// naim khan -> Naim Khan
// ==========================================================

function initializeNameFormatter() {

    document.querySelectorAll(".name-input").forEach(input => {

        input.addEventListener("input", function () {

            let value = this.value;

            value = value.replace(/\s+/g, " ");

            value = value.toLowerCase();

            value = value.replace(/\b\w/g, c => c.toUpperCase());

            this.value = value;

        });

    });

}

// ==========================================================
// Email Formatter
// ==========================================================

function initializeEmailFormatter() {

    document.querySelectorAll(".email-input").forEach(input => {

        input.addEventListener("input", function () {

            this.value = this.value.toLowerCase().trim();

        });

    });

}

// ==========================================================
// Mobile Formatter
// 9876543210
// ↓
// 98765 43210
// ==========================================================

// ==========================================================
// Mobile Formatter
// ==========================================================

function formatMobile(input) {

    let digits = input.value.replace(/\D/g, "");

    digits = digits.slice(0, 10);

    if (digits.length > 5) {
        digits = digits.slice(0, 5) + " " + digits.slice(5);
    }

    input.value = digits;
}

function initializeMobileFormatter() {

    document.querySelectorAll(".mobile-input").forEach(input => {

        // Format on page load (Edit Form)
        formatMobile(input);

        // Format while typing
        input.addEventListener("input", function () {
            formatMobile(this);
        });

    });

}

// ==========================================================
// Aadhaar Formatter
// 123456789012
// ↓
// 1234 5678 9012
// ==========================================================

// ==========================================================
// Aadhaar Formatter
// ==========================================================

function formatAadhaar(input) {

    let digits = input.value.replace(/\D/g, "");

    digits = digits.slice(0, 12);

    let formatted = "";

    for (let i = 0; i < digits.length; i++) {

        if (i > 0 && i % 4 === 0) {
            formatted += " ";
        }

        formatted += digits.charAt(i);

    }

    input.value = formatted;
}

function initializeAadhaarFormatter() {

    document.querySelectorAll(".aadhaar-input").forEach(input => {

        // Format on page load (Edit Form)
        formatAadhaar(input);

        // Format while typing
        input.addEventListener("input", function () {
            formatAadhaar(this);
        });

    });

}

// ==========================================================
// Trim Spaces
// ==========================================================

function initializeTrimFormatter() {

    document.querySelectorAll("input, textarea").forEach(input => {

        input.addEventListener("blur", function () {

            this.value = this.value.trim();

        });

    });

}


// ==========================================================
// Remove Spring Validation Error On Input
// ==========================================================

function initializeValidationCleaner() {

    document.querySelectorAll("input, select, textarea").forEach(field => {

        field.addEventListener("input", function () {

            // Remove Bootstrap Invalid Class
            this.classList.remove("is-invalid");

            // Remove Bootstrap Valid Class
            this.classList.remove("is-valid");

            // Clear Duplicate Error
            this.removeAttribute("data-duplicate");

            // Remove Invalid Feedback
            let feedback = this.parentElement.querySelector(".invalid-feedback");

            if (feedback) {
                feedback.style.display = "none";
            }

        });

    });

}

// ==========================================================
// Reset Validation
// ==========================================================

function initializeResetFormatter() {

    document.querySelectorAll("form").forEach(form => {

        form.addEventListener("reset", function () {

            setTimeout(() => {

                this.querySelectorAll(".is-invalid")
                    .forEach(e => e.classList.remove("is-invalid"));

                this.querySelectorAll(".is-valid")
                    .forEach(e => e.classList.remove("is-valid"));

                this.querySelectorAll(".invalid-feedback")
                    .forEach(e => e.style.display = "none");

                this.querySelectorAll(".duplicate-message")
                    .forEach(e => e.textContent = "");

            }, 0);

        });

    });

}
// ==========================================================
// Photo Preview
// ==========================================================

function initializePhotoPreview() {

    const fileInput = document.getElementById("photoFile");
    const preview = document.getElementById("photoPreview");

    if (!fileInput || !preview) return;

    const defaultImage = preview.src;

    fileInput.addEventListener("change", function () {

        const file = this.files[0];

        if (!file) {

            preview.src = defaultImage;
            return;

        }

        // ==========================
        // Image Type Validation
        // ==========================

        const allowedTypes = [

            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"

        ];

        if (!allowedTypes.includes(file.type)) {

            alert("Only JPG, JPEG, PNG and WEBP images are allowed.");

            this.value = "";

            preview.src = defaultImage;

            return;

        }

        // ==========================
        // Image Size Validation
        // 2 MB
        // ==========================

        if (file.size > 2 * 1024 * 1024) {

            alert("Image size must not exceed 2 MB.");

            this.value = "";

            preview.src = defaultImage;

            return;

        }

        const reader = new FileReader();

        reader.onload = function (e) {

            preview.src = e.target.result;

        };

        reader.readAsDataURL(file);

    });

}

// ==========================================================
// Focus First Invalid Field
// ==========================================================

function initializeFirstInvalidFocus() {

    const firstInvalid = document.querySelector(".is-invalid");

    if (firstInvalid) {

        firstInvalid.focus();

        firstInvalid.scrollIntoView({

            behavior: "smooth",
            block: "center"

        });

    }

}
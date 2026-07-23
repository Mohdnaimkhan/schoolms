// ==========================================================
// VALIDATOR.JS
// Common Form Validation
// ==========================================================

document.addEventListener("DOMContentLoaded", () => {

    initializeValidation();

});

// ==========================================================
// Initialize Validation
// ==========================================================

function initializeValidation() {

    const forms = document.querySelectorAll("form");

    forms.forEach(form => {

        form.addEventListener("submit", function (e) {

            let valid = true;

            clearValidationErrors();

            /*
            ==========================================================
            Required Fields
            ==========================================================
            */

            form.querySelectorAll(".required").forEach(field => {

                if (field.value.trim() === "") {

                    showValidationError(field, "This field is required.");

                    valid = false;

                }

            });

            /*
            ==========================================================
            Mobile Validation
            ==========================================================
            */

            form.querySelectorAll(".mobile-input").forEach(field => {

                let value = field.value.replace(/\D/g, "");

                if (value !== "" && !/^[6-9]\d{9}$/.test(value)) {

                    showValidationError(field, "Enter valid 10 digit mobile number.");

                    valid = false;

                }

            });

            /*
            ==========================================================
            Email Validation
            ==========================================================
            */

            form.querySelectorAll(".email-input").forEach(field => {

                let value = field.value.trim();

                if (value !== "") {

                    let pattern =
                        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                    if (!pattern.test(value)) {

                        showValidationError(field, "Enter valid email address.");

                        valid = false;

                    }

                }

            });

            /*
            ==========================================================
            Aadhaar Validation
            ==========================================================
            */

            form.querySelectorAll(".aadhaar-input").forEach(field => {

                let value = field.value.replace(/\D/g, "");

                if (value !== "" && value.length !== 12) {

                    showValidationError(field, "Aadhaar must contain 12 digits.");

                    valid = false;

                }

            });

            // Keep the client-side feedback, but allow the request to reach the
            // server. Server-side validation is authoritative and prevents a
            // stale or incomplete browser script from trapping the Save button.

        });

    });

}

// ==========================================================
// Show Error
// ==========================================================

function showValidationError(field, message) {

    field.classList.add("is-invalid");

    let error = field.parentElement.querySelector(".js-error");

    if (!error) {

        error = document.createElement("div");

        error.className = "invalid-feedback d-block js-error";

        field.parentElement.appendChild(error);

    }

    error.innerText = message;

}

// ==========================================================
// Clear Errors
// ==========================================================

function clearValidationErrors() {

    document.querySelectorAll(".is-invalid").forEach(field => {

        field.classList.remove("is-invalid");

    });

    document.querySelectorAll(".js-error").forEach(error => {

        error.remove();

    });

}

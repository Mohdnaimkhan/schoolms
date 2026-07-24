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

function initializeMobileFormatter() {

    document.querySelectorAll(".mobile-input").forEach(input => {

        input.addEventListener("input", function () {

            let digits = this.value.replace(/\D/g, "");

            digits = digits.slice(0, 10);

            if (digits.length > 5) {

                digits =
                    digits.slice(0, 5)
                    + " "
                    + digits.slice(5);

            }

            this.value = digits;

        });

    });

}

// ==========================================================
// Aadhaar Formatter
// 123456789012
// ↓
// 1234 5678 9012
// ==========================================================

function initializeAadhaarFormatter() {

    document.querySelectorAll(".aadhaar-input").forEach(input => {

        input.addEventListener("input", function () {

            let digits = this.value.replace(/\D/g, "");

            digits = digits.slice(0, 12);

            let formatted = "";

            for (let i = 0; i < digits.length; i++) {

                if (i > 0 && i % 4 === 0) {

                    formatted += " ";

                }

                formatted += digits.charAt(i);

            }

            this.value = formatted;

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



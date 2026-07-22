// ==========================================
// DUPLICATOR.JS
// Live Duplicate Checker
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    initializeDuplicateChecker();

});

// ==========================================
// Initialize
// ==========================================

function initializeDuplicateChecker() {

    document.querySelectorAll("[data-check-url]").forEach(input => {

        let timer;

        input.addEventListener("input", function () {

            clearTimeout(timer);

            timer = setTimeout(() => {

                checkDuplicate(this);

            }, 500);

        });

    });

}

// ==========================================
// Duplicate Check
// ==========================================

async function checkDuplicate(input) {

    let value = input.value.replace(/\s/g, "").trim();

    if (value === "") {

        clearStatus(input);

        return;

    }

    let id = input.dataset.id || "";

    let url =
        input.dataset.checkUrl +
        "?value=" +
        encodeURIComponent(value) +
        "&id=" +
        id;

    showLoading(input);

    try {

        const response = await fetch(url);

        const exists = await response.json();

        if (exists) {

            showError(input, "Already Exists");

        } else {

            showSuccess(input, "Available");

        }

    } catch (e) {

        showError(input, "Unable to Check");

    }

}

// ==========================================
// Loading
// ==========================================

function showLoading(input) {

    let div = getStatusDiv(input);

    div.className = "small text-secondary mt-1";

    div.innerHTML = "Checking...";

}

// ==========================================
// Success
// ==========================================

function showSuccess(input, message) {

    let div = getStatusDiv(input);

    div.className = "small text-success mt-1";

    div.innerHTML = "✔ " + message;

}

// ==========================================
// Error
// ==========================================

function showError(input, message) {

    let div = getStatusDiv(input);

    div.className = "small text-danger mt-1";

    div.innerHTML = "✖ " + message;

}

// ==========================================
// Clear
// ==========================================

function clearStatus(input) {

    getStatusDiv(input).innerHTML = "";

}

// ==========================================
// Status Div
// ==========================================

function getStatusDiv(input) {

    let id = input.id + "-status";

    let div = document.getElementById(id);

    if (!div) {

        div = document.createElement("div");

        div.id = id;

        input.parentNode.appendChild(div);

    }

    return div;

}
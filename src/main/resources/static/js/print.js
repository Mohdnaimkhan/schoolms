// ==========================================
// PRINT.JS
// Common Print Utility
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    initializePrintButtons();

});

// ==========================================
// Print Current Page
// ==========================================

function initializePrintButtons() {

    document.querySelectorAll(".btn-print").forEach(button => {

        button.addEventListener("click", function (e) {

            e.preventDefault();

            window.print();

        });

    });

}

// ==========================================
// Print Specific Section
// ==========================================

function printElement(elementId) {

    const element = document.getElementById(elementId);

    if (!element) {

        console.error("Print element not found : " + elementId);

        return;

    }

    const printWindow = window.open("", "_blank", "width=1000,height=800");

    printWindow.document.write(`
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Print Preview</title>

<link rel="stylesheet"
      href="/css/bootstrap.min.css">

<link rel="stylesheet"
      href="/css/style.css">

<link rel="stylesheet"
      href="/css/print.css">

</head>

<body class="bg-white">

<div class="container-fluid p-0">

${element.outerHTML}

</div>

<script>

window.onload = function () {

    window.focus();

    window.print();

    window.onafterprint = function () {

        window.close();

    };

};

<\/script>

</body>

</html>
    `);

    printWindow.document.close();

}
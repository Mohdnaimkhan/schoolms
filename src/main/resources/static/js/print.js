// ==========================================
// PRINT.JS
// Common Print Functions
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    initializePrintButtons();

});

// ==========================================
// Print Button
// ==========================================

function initializePrintButtons() {

    document.querySelectorAll(".btn-print").forEach(button => {

        button.addEventListener("click", function () {

            window.print();

        });

    });

}

// ==========================================
// Print Specific Div
// ==========================================

function printElement(id) {

    const element = document.getElementById(id);

    if (!element) {

        return;

    }

    const printWindow = window.open("", "_blank");

    printWindow.document.write(`
        <html>
            <head>
                <title>Print</title>

                <link rel="stylesheet"
                      href="/css/bootstrap.min.css">

                <link rel="stylesheet"
                      href="/css/print.css">

            </head>

            <body>

                ${element.outerHTML}

            </body>

        </html>
    `);

    printWindow.document.close();

    printWindow.focus();

    printWindow.print();

    printWindow.close();

}
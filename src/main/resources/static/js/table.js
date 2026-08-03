// ==========================================
// TABLE.JS
// Common Table Functions
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    initializeTableSearch();

});

// ==========================================
// Live Search
// ==========================================

function initializeTableSearch() {

    document.querySelectorAll(".table-search").forEach(searchBox => {

        searchBox.addEventListener("keyup", function () {

            let value = this.value.toLowerCase();

            let tableId = this.dataset.table;

            let table = document.getElementById(tableId);

            if (!table) return;

            let rows = table.querySelectorAll("tbody tr");

            let visible = 0;

            rows.forEach(row => {

                if (row.innerText.toLowerCase().includes(value)) {

                    row.style.display = "";

                    visible++;

                }

                else {

                    row.style.display = "none";

                }

            });

            updateEmptyMessage(table, visible);

        });

    });

}

// ==========================================
// Empty Message
// ==========================================

function updateEmptyMessage(table, visible) {

    let tbody = table.querySelector("tbody");

    let emptyRow = tbody.querySelector(".empty-row");

    if (visible === 0) {

        if (!emptyRow) {

            emptyRow = document.createElement("tr");

            emptyRow.className = "empty-row";

            emptyRow.innerHTML =

                `<td colspan="100" class="text-center text-muted py-4">

                    No Records Found

                </td>`;

            tbody.appendChild(emptyRow);

        }

    }

    else {

        if (emptyRow) {

            emptyRow.remove();

        }

    }

}
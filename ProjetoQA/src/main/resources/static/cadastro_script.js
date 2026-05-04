// ============================================
// BIBLIOTECA — CADASTRO SCRIPT
// ============================================

const params = new URLSearchParams(window.location.search);

const errorElement = document.getElementById("error");

if (params.has("error")) {

    errorElement.style.display = "block";

    errorElement.textContent =
        "Erro ao cadastrar. Tente novamente.";
}
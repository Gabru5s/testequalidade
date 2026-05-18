// ============================================
// BIBLIOTECA — CADASTRO SCRIPT
// Formulário Thymeleaf injeta _csrf sozinho.
// ============================================

const params = new URLSearchParams(window.location.search);
const errorElement = document.getElementById("error");

if (params.has("error")) {
    errorElement.style.display = "block";
    errorElement.textContent   = "Erro ao cadastrar. Tente novamente.";
}

// ============================================
// VIA CEP
// ============================================

const cepInput = document.getElementById("cep");
const enderecoInput = document.getElementById("endereco");

cepInput.addEventListener("blur", async () => {

    const cep = cepInput.value.replace(/\D/g, "");

    if (cep.length !== 8) {
        enderecoInput.value = "";
        alert("CEP inválido.");
        return;
    }

    try {

        const resposta = await fetch(`/api/cep/${cep}`);

        if (!resposta.ok) {
            throw new Error("CEP não encontrado");
        }

        const dados = await resposta.json();

        enderecoInput.value =
            `${dados.logradouro}, ${dados.bairro}, ${dados.localidade} - ${dados.uf}`;

    } catch (erro) {

        console.error(erro);

        enderecoInput.value = "";

        alert("Erro ao buscar CEP.");
    }
    });
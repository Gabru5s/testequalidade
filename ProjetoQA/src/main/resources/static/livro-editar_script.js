// ============================================
// BIBLIOTECA — EDITAR LIVRO SCRIPT
// ============================================

const livroId = document.getElementById("id").value;

const erroEl = document.getElementById("erro");

// CSRF — lê o token da meta tag injetada pelo Thymeleaf
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content ?? null;
}

function mostrarErro(mensagem) {
    erroEl.style.display = "block";
    erroEl.textContent = mensagem;
}

function esconderErro() {
    erroEl.style.display = "none";
    erroEl.textContent = "";
}

document.getElementById("formLivro").addEventListener("submit", async (e) => {

    e.preventDefault();

    esconderErro();

    const titulo = document.getElementById("titulo").value.trim();

    const autor = document.getElementById("autor").value.trim();

    const isbn = document.getElementById("isbn").value.trim();

    const ano = document.getElementById("ano").value;

    const anoAtual = new Date().getFullYear();

    if (!titulo || !autor || !ano) {
        mostrarErro("Preencha todos os campos obrigatórios.");
        return;
    }

    if (ano > anoAtual) {
        mostrarErro(`Ano de publicação inválido.`);
        return;
    }

    try {

        const response = await fetch(`/api/livros/${livroId}`, {

            method: "PUT",

            credentials: "same-origin",

            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": getCsrfToken()  // CSRF obrigatório em PUT
            },

            body: JSON.stringify({
                titulo: titulo,
                autor: autor,
                isbn: isbn || null,
                anoPublicacao: parseInt(ano)
            })
        });

        if (!response.ok) {
            throw new Error("Erro ao atualizar livro");
        }

        window.location.href = "/livros";

    } catch (error) {

        console.error(error);

        mostrarErro("Erro ao atualizar livro. Tente novamente.");
    }
});
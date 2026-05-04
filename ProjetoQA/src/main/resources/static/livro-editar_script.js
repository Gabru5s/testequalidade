// ============================================
// BIBLIOTECA — EDITAR LIVRO SCRIPT
// ============================================

const livroId = document.getElementById("id").value;

const erroEl = document.getElementById("erro");

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

    if (!titulo || !autor || !ano) {
        mostrarErro("Preencha todos os campos obrigatórios.");
        return;
    }

    try {

        const response = await fetch(`/api/livros/${livroId}`, {
            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Id": usuarioId
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
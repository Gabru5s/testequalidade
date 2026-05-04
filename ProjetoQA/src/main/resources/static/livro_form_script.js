// ============================================
// NOVO LIVRO — SCRIPT
// ============================================

// ANO ATUAL

const anoAtual = new Date().getFullYear();

// INPUT DO ANO

const inputAno = document.getElementById("ano");

inputAno.max = anoAtual;

// FORMULÁRIO

const formLivro = document.getElementById("formLivro");

// EVENTO DE SUBMIT

formLivro.addEventListener("submit", async (e) => {

    e.preventDefault();

    // ELEMENTO DE ERRO

    const erro = document.getElementById("erro");

    erro.textContent = "";

    // DADOS DOS CAMPOS

    const titulo = document
        .getElementById("titulo")
        .value
        .trim();

    const autor = document
        .getElementById("autor")
        .value
        .trim();

    const isbn = document
        .getElementById("isbn")
        .value
        .trim();

    const ano = document
        .getElementById("ano")
        .value;

    // VALIDAÇÃO

    if (!titulo || !autor || !ano) {

        erro.textContent =
            "Preencha todos os campos obrigatórios.";

        return;
    }

    // VALIDAÇÃO DO USUÁRIO

    if (!usuarioId || usuarioId === "") {

        erro.textContent =
            "Usuário não identificado.";

        console.error("usuarioId inválido:", usuarioId);

        return;
    }

    try {

        // REQUISIÇÃO

        const response = await fetch("/api/livros", {

            method: "POST",

            headers: {
                "Content-Type": "application/json",
                "X-Usuario-Id": usuarioId
            },

            body: JSON.stringify({
                titulo: titulo,
                autor: autor,
                isbn: isbn || null,
                anoPublicacao: Number(ano)
            })

        });

        // VERIFICA ERRO

        if (!response.ok) {

            const mensagem = await response.text();

            throw new Error(mensagem);
        }

        // REDIRECIONA

        window.location.href = "/livros";

    } catch (err) {

        console.error("Erro ao salvar livro:", err);

        erro.textContent =
            err.message || "Erro ao salvar livro.";

    }

});
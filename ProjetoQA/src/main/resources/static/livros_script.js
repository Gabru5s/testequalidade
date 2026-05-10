// ============================================
// BIBLIOTECA — LIVROS SCRIPT
// ============================================

console.log("usuarioId:", usuarioId);

// ============================================
// CSRF — lê o cookie XSRF-TOKEN do Spring
// ============================================

function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content ?? null;
}
// ============================================
// CRIAR CARD
// ============================================

function criarCardLivro(livro) {

    const card = document.createElement("div");

    card.className = "livro-card";

    card.innerHTML = `
        <div class="livro-info">

            <h3>${escapeHtml(livro.titulo)}</h3>

            <div class="livro-meta">

                <div class="livro-meta-item">

                    <svg viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round">

                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                        <circle cx="12" cy="7" r="4"/>

                    </svg>

                    <strong>Autor:</strong>
                    ${escapeHtml(livro.autor)}

                </div>

                <div class="livro-meta-item">

                    <svg viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round">

                        <line x1="4" y1="9" x2="20" y2="9"/>
                        <line x1="4" y1="15" x2="20" y2="15"/>
                        <line x1="10" y1="3" x2="8" y2="21"/>
                        <line x1="16" y1="3" x2="14" y2="21"/>

                    </svg>

                    <strong>ISBN:</strong>
                    ${escapeHtml(livro.isbn)}

                </div>

                <div class="livro-meta-item">

                    <svg viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round">

                        <rect x="3" y="4"
                            width="18"
                            height="18"
                            rx="2"
                            ry="2"/>

                        <line x1="16" y1="2" x2="16" y2="6"/>
                        <line x1="8" y1="2" x2="8" y2="6"/>
                        <line x1="3" y1="10" x2="21" y2="10"/>

                    </svg>

                    <strong>Ano:</strong>
                    ${livro.anoPublicacao}

                </div>

            </div>

        </div>

        <div class="livro-actions">

            <button class="btn btn-info"
                onclick="editarLivro('${livro.id}')">

                Editar

            </button>

            <button class="btn btn-danger"
                onclick="deletarLivro('${livro.id}')">

                Deletar

            </button>

        </div>
    `;

    return card;
}

// ============================================
// ESCAPE HTML
// ============================================

function escapeHtml(text) {

    if (!text) return '';

    const div = document.createElement('div');

    div.textContent = text;

    return div.innerHTML;
}

// ============================================
// ESTADO VAZIO
// ============================================

function mostrarVazio() {

    const lista = document.getElementById("lista");

    lista.innerHTML = `
        <div class="empty-state">

            <h3>Nenhum livro cadastrado</h3>

            <p>
                Comece sua biblioteca pessoal adicionando o primeiro livro.
            </p>

            <a href="/livros/novo">

                Adicionar primeiro livro

            </a>

        </div>
    `;

    document.getElementById("contador").textContent = "0";
}

// ============================================
// ERRO
// ============================================

function mostrarErro() {

    const lista = document.getElementById("lista");

    lista.innerHTML = `
        <div class="error-state">

            <p>
                Erro ao carregar livros.
            </p>

        </div>
    `;
}

// ============================================
// CARREGAR LIVROS
// ============================================

function carregarLivros() {

    // GET não precisa de CSRF, mas mantemos credentials e o header de usuário
    fetch("/api/livros", {

        method: "GET",

        credentials: "same-origin",

        headers: {
            "Content-Type": "application/json",
            "X-Usuario-Id": usuarioId
        }

    })

        .then(res => {

            if (res.status === 401) {

                window.location.href = "/login";

                return;
            }

            if (!res.ok) {
                throw new Error("Erro ao carregar livros");
            }

            return res.json();
        })

        .then(data => {

            if (!data) return;

            console.log("Livros:", data);

            const lista = document.getElementById("lista");

            lista.innerHTML = "";

            if (data.length === 0) {

                mostrarVazio();

                return;
            }

            document.getElementById("contador").textContent = data.length;

            data.forEach(livro => {

                lista.appendChild(
                    criarCardLivro(livro)
                );

            });

        })

        .catch(err => {

            console.error(err);

            mostrarErro();

        });
}

// ============================================
// DELETAR
// ============================================

function deletarLivro(id) {

    if (!confirm("Deseja deletar este livro?")) {
        return;
    }

    fetch(`/api/livros/${id}`, {

        method: "DELETE",

        credentials: "same-origin",

        headers: {
            "Content-Type": "application/json",
            "X-Usuario-Id": usuarioId,
            "X-CSRF-TOKEN": getCsrfToken()  // CSRF obrigatório em DELETE
        }

    })

        .then(res => {

            if (res.status === 401) {

                window.location.href = "/login";

                return;
            }

            if (!res.ok) {
                throw new Error("Erro ao deletar");
            }

            carregarLivros();
        })

        .catch(err => {

            console.error(err);

            alert("Erro ao deletar livro.");

        });
}

// ============================================
// EDITAR
// ============================================

function editarLivro(id) {

    window.location.href = `/livros/editar/${id}`;
}

// ============================================
// INIT
// ============================================

document.addEventListener("DOMContentLoaded", () => {

    if (!usuarioId || usuarioId === "null") {

        window.location.href = "/login";

        return;
    }

    carregarLivros();

});
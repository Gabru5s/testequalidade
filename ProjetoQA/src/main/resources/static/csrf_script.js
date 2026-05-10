// ============================================
// csrf-utils.js — utilitário CSRF global
// Lê o token via meta tag injetada pelo Thymeleaf,
// sem depender de cookie httpOnly=false.
//
// Adicione no <head> de cada página autenticada:
//   <meta name="_csrf"        th:content="${_csrf.token}"/>
//   <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
// ============================================

function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content ?? null;
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]')?.content ?? 'X-CSRF-TOKEN';
}

async function csrfFetch(url, options = {}) {
    const token  = getCsrfToken();
    const header = getCsrfHeader();

    const headers = {
        ...(options.headers || {}),
        ...(token ? { [header]: token } : {})
    };

    return fetch(url, { ...options, headers });
}
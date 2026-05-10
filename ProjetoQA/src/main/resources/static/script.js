// ============================================
// BIBLIOTECA — LOGIN SCRIPT
// Nenhuma chamada fetch aqui: o formulário
// Thymeleaf já inclui _csrf automaticamente.
// ============================================

// Error handling
const params = new URLSearchParams(window.location.search);
const errorDiv = document.getElementById('error');
if (params.has('error')) {
    errorDiv.classList.add('show');
}

// Password toggle
const togglePassword = document.getElementById('togglePassword');
const passwordInput  = document.getElementById('password');
const eyeIcon        = document.getElementById('eyeIcon');
const eyeOffIcon     = document.getElementById('eyeOffIcon');

togglePassword.addEventListener('click', () => {
    const isPassword = passwordInput.type === 'password';
    passwordInput.type = isPassword ? 'text' : 'password';
    eyeIcon.style.display    = isPassword ? 'none'  : 'block';
    eyeOffIcon.style.display = isPassword ? 'block' : 'none';
    togglePassword.setAttribute('aria-label', isPassword ? 'Ocultar senha' : 'Mostrar senha');
});

// Loading state
const form      = document.getElementById('loginForm');
const submitBtn = document.getElementById('submitBtn');

form.addEventListener('submit', () => {
    submitBtn.classList.add('loading');
    submitBtn.disabled = true;
});

// BIBLIOTECA — LOGIN SCRIPT

// Error handling
const params = new URLSearchParams(window.location.search);
const errorDiv = document.getElementById('error');
if (params.has('error')) {
    errorDiv.classList.add('show');
}

// Password toggle
const togglePassword = document.getElementById('togglePassword');
const passwordInput = document.getElementById('password');
const eyeIcon = document.getElementById('eyeIcon');
const eyeOffIcon = document.getElementById('eyeOffIcon');

togglePassword.addEventListener('click', () => {
    const type = passwordInput.type === 'password' ? 'text' : 'password';
    passwordInput.type = type;
    eyeIcon.style.display = type === 'password' ? 'block' : 'none';
    eyeOffIcon.style.display = type === 'password' ? 'none' : 'block';
    togglePassword.setAttribute('aria-label', type === 'password' ? 'Mostrar senha' : 'Ocultar senha');
});

// Loading state
const form = document.getElementById('loginForm');
const submitBtn = document.getElementById('submitBtn');

form.addEventListener('submit', () => {
    submitBtn.classList.add('loading');
    submitBtn.disabled = true;
});
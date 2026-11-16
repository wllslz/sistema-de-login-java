const alertBox = document.getElementById('alertBox');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const profileSection = document.getElementById('profileSection');
const goRegister = document.getElementById('goRegister');
const goLogin = document.getElementById('goLogin');
const logoutBtn = document.getElementById('logoutBtn');

function showAlert(msg, type) {
  alertBox.textContent = msg;
  alertBox.className = 'alert ' + type;
}

function clearAlert() {
  alertBox.className = 'alert';
}

goRegister.addEventListener('click', () => {
  clearAlert();
  loginForm.classList.add('hidden');
  registerForm.classList.remove('hidden');
});

goLogin.addEventListener('click', () => {
  clearAlert();
  registerForm.classList.add('hidden');
  loginForm.classList.remove('hidden');
});

loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  clearAlert();

  const params = new URLSearchParams();
  params.append('identifier', document.getElementById('loginIdentifier').value);
  params.append('password', document.getElementById('loginPassword').value);

  const res = await fetch('/api/login', { method: 'POST', body: params });
  const data = await res.json();

  if (!data.ok) {
    showAlert(data.message, 'error');
    return;
  }

  loginForm.reset();
  document.getElementById('profUser').textContent = data.user.username;
  document.getElementById('profEmail').textContent = data.user.email;
  document.getElementById('profRole').textContent = data.user.role;

  loginForm.classList.add('hidden');
  profileSection.classList.remove('hidden');
  showAlert(data.message, 'success');
});

registerForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  clearAlert();

  const params = new URLSearchParams();
  params.append('username', document.getElementById('regUsername').value);
  params.append('email', document.getElementById('regEmail').value);
  params.append('password', document.getElementById('regPassword').value);
  params.append('confirmPassword', document.getElementById('regConfirm').value);

  const res = await fetch('/api/register', { method: 'POST', body: params });
  const data = await res.json();

  if (!data.ok) {
    showAlert(data.message, 'error');
    return;
  }

  registerForm.reset();
  registerForm.classList.add('hidden');
  loginForm.classList.remove('hidden');
  showAlert(data.message, 'success');
});

logoutBtn.addEventListener('click', () => {
  profileSection.classList.add('hidden');
  loginForm.classList.remove('hidden');
  showAlert('Sessão encerrada.', 'success');
});

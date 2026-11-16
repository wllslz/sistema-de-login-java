const alertBox = document.getElementById('alertBox');
const tabLogin = document.getElementById('tabLogin');
const tabRegister = document.getElementById('tabRegister');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const authSection = document.getElementById('authSection');
const profileSection = document.getElementById('profileSection');
const logoutBtn = document.getElementById('logoutBtn');

function showAlert(msg, type) {
  alertBox.textContent = msg;
  alertBox.className = 'alert ' + type;
}

function clearAlert() {
  alertBox.className = 'alert';
}

function switchTab(tab) {
  clearAlert();
  tabLogin.classList.toggle('active', tab === 'login');
  tabRegister.classList.toggle('active', tab === 'register');
  loginForm.classList.toggle('hidden', tab !== 'login');
  registerForm.classList.toggle('hidden', tab !== 'register');
}

tabLogin.addEventListener('click', () => switchTab('login'));
tabRegister.addEventListener('click', () => switchTab('register'));

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
  document.getElementById('profId').textContent = '#' + data.user.id;
  document.getElementById('profUser').textContent = data.user.username;
  document.getElementById('profEmail').textContent = data.user.email;
  document.getElementById('profRole').textContent = data.user.role;

  authSection.classList.add('hidden');
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
  switchTab('login');
  showAlert(data.message + ' Faça login abaixo.', 'success');
});

logoutBtn.addEventListener('click', () => {
  profileSection.classList.add('hidden');
  authSection.classList.remove('hidden');
  showAlert('Logout realizado com sucesso.', 'success');
});

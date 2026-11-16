const alertBox = document.getElementById('alertBox');
const tabLogin = document.getElementById('tabLogin');
const tabRegister = document.getElementById('tabRegister');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const authSection = document.getElementById('authSection');
const profileSection = document.getElementById('profileSection');
const logoutBtn = document.getElementById('logoutBtn');
const panelTitle = document.getElementById('panelTitle');
const panelSubtitle = document.getElementById('panelSubtitle');

// Alternar visualização de senha
document.querySelectorAll('.toggle-pass').forEach((btn) => {
  btn.addEventListener('click', () => {
    const input = document.getElementById(btn.dataset.target);
    const isPass = input.type === 'password';
    input.type = isPass ? 'text' : 'password';
    btn.textContent = isPass ? '🙈' : '👁️';
  });
});

// Medidor em tempo real de força da senha no cadastro
const regPasswordInput = document.getElementById('regPassword');
const strengthFill = document.getElementById('strengthFill');
const reqLen = document.getElementById('reqLen');
const reqUpper = document.getElementById('reqUpper');
const reqLower = document.getElementById('reqLower');
const reqNum = document.getElementById('reqNum');

regPasswordInput.addEventListener('input', () => {
  const val = regPasswordInput.value;
  const checks = [
    { el: reqLen, ok: val.length >= 8 },
    { el: reqUpper, ok: /[A-Z]/.test(val) },
    { el: reqLower, ok: /[a-z]/.test(val) },
    { el: reqNum, ok: /[0-9]/.test(val) }
  ];

  let score = 0;
  checks.forEach((c) => {
    c.el.classList.toggle('met', c.ok);
    if (c.ok) score++;
  });

  strengthFill.style.width = (score * 25) + '%';
  if (score <= 1) strengthFill.style.background = '#ef4444';
  else if (score <= 3) strengthFill.style.background = '#f59e0b';
  else strengthFill.style.background = '#10b981';
});

function showAlert(msg, type) {
  alertBox.textContent = msg;
  alertBox.className = 'alert ' + type;
}

function clearAlert() {
  alertBox.className = 'alert';
}

function switchTab(tab) {
  clearAlert();
  const isLogin = tab === 'login';
  tabLogin.classList.toggle('active', isLogin);
  tabRegister.classList.toggle('active', !isLogin);
  loginForm.classList.toggle('hidden', !isLogin);
  registerForm.classList.toggle('hidden', isLogin);

  panelTitle.textContent = isLogin ? 'Bem-vindo de volta' : 'Criar nova conta';
  panelSubtitle.textContent = isLogin
    ? 'Entre com seu nome de usuário ou e-mail cadastrado.'
    : 'Preencha os campos abaixo para registrar suas credenciais.';
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
  const initial = data.user.username.charAt(0).toUpperCase();
  document.getElementById('profAvatar').textContent = initial;
  document.getElementById('profGreeting').textContent = 'Olá, ' + data.user.username;
  document.getElementById('profId').textContent = '#' + data.user.id;
  document.getElementById('profUser').textContent = data.user.username;
  document.getElementById('profEmail').textContent = data.user.email;
  document.getElementById('profRole').textContent = data.user.role;
  document.getElementById('profTime').textContent = new Date().toLocaleTimeString('pt-BR');

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
  strengthFill.style.width = '0%';
  [reqLen, reqUpper, reqLower, reqNum].forEach((el) => el.classList.remove('met'));
  switchTab('login');
  showAlert(data.message + ' Faça login abaixo.', 'success');
});

logoutBtn.addEventListener('click', () => {
  profileSection.classList.add('hidden');
  authSection.classList.remove('hidden');
  showAlert('Sessão encerrada com segurança.', 'success');
});

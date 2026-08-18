
const API = '';
function getHeaders() { return { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}` }; }
async function login(username, password) {
    const fd = new URLSearchParams(); fd.append('username', username); fd.append('password', password);
    const res = await fetch(`${API}/auth/login`, { method: 'POST', body: fd });
    if(res.ok) { const data = await res.json(); localStorage.setItem('token', data.access_token); localStorage.setItem('role', data.role); return data; }
    throw new Error('Login failed');
}
async function register(data) {
    const res = await fetch(`${API}/auth/register`, { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(data) });
    if(!res.ok) throw new Error('Registration failed'); return res.json();
}
function logout() { localStorage.clear(); window.location.href = 'index.html'; }
function checkAuth(allowedRole) {
    const token = localStorage.getItem('token'); const role = localStorage.getItem('role');
    if(!token) window.location.href = 'index.html';
    if(allowedRole && role !== allowedRole) window.location.href = role === 'LAB_HEAD' ? 'labhead_dashboard.html' : 'scholar_dashboard.html';
}

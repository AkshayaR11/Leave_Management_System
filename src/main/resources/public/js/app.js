const API = 'http://localhost:8080/api';
let currentUser = null;

// Routing logic
function showView(viewId) {
    document.getElementById('login-view').style.display = 'none';
    document.getElementById('employee-view').style.display = 'none';
    document.getElementById('manager-view').style.display = 'none';
    document.getElementById('admin-view').style.display = 'none';
    
    document.getElementById(viewId).style.display = 'grid';
    
    const cal = document.getElementById('shared-calendar-view');
    if (cal) {
        cal.style.display = (viewId !== 'login-view') ? 'block' : 'none';
    }
}

async function login() {
    const username = document.getElementById('login-username').value;
    try {
        const res = await fetch(`${API}/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username})
        });
        if(res.ok) {
            currentUser = await res.json();
            let role = 'ADMIN';
            if (currentUser.department !== undefined) role = 'EMPLOYEE';
            else if (currentUser.managedDepartment !== undefined) role = 'MANAGER';
            currentUser.userRole = role;

            document.getElementById('topbar').style.display = 'flex';
            document.getElementById('welcome-msg').innerText = `Welcome, ${currentUser.name}`;
            document.getElementById('role-badge').innerText = currentUser.userRole;
            
            initDashboard();
        }
    } catch (e) {
        alert("Login failed");
    }
}

function logout() {
    currentUser = null;
    document.getElementById('topbar').style.display = 'none';
    showView('login-view');
}

function initDashboard() {
    loadCalendar();
    if (currentUser.userRole === 'EMPLOYEE') {
        showView('employee-view');
        loadLeaveTypesOptions();
        loadEmployeeLeaves();
        loadNotifications();
    } else if (currentUser.userRole === 'MANAGER') {
        showView('manager-view');
        loadManagerPendingLeaves();
    } else if (currentUser.userRole === 'ADMIN') {
        showView('admin-view');
        loadAdminLeaveTypes();
        loadReports();
    }
}

// ------ API CALLS & DOM MANIPULATIONS ------

async function fetchAPI(endpoint, method='GET', body=null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'username': currentUser.username
        }
    };
    if (body) options.body = JSON.stringify(body);
    const response = await fetch(`${API}${endpoint}`, options);
    return await response.json();
}

// MEMBER 1: Employee Features
async function loadLeaveTypesOptions() {
    const types = await fetchAPI('/leavetypes');
    const select = document.getElementById('leaveType');
    select.innerHTML = types.map(t => `<option value="${t.id}">${t.name} (Max ${t.maxDays} days)</option>`).join('');
}

async function applyLeave(e) {
    e.preventDefault();
    const payload = {
        username: currentUser.username,
        leaveTypeId: document.getElementById('leaveType').value,
        startDate: document.getElementById('startDate').value,
        endDate: document.getElementById('endDate').value,
        reason: document.getElementById('reason').value
    };
    await fetchAPI('/leave', 'POST', payload);
    document.getElementById('leave-form').reset();
    loadEmployeeLeaves(); // Refresh Table
}

async function loadEmployeeLeaves() {
    const leaves = await fetchAPI(`/leaves/${currentUser.username}`);
    const tbody = document.querySelector('#my-leaves-table tbody');
    tbody.innerHTML = leaves.map(l => `
        <tr>
            <td>${l.leaveType.name}</td>
            <td>${l.startDate} to ${l.endDate}</td>
            <td><span class="badge badge-${l.status}">${l.status}</span></td>
            <td>
                ${l.status === 'PENDING' ? `<button onclick="cancelLeave(${l.id})" class="btn btn-secondary btn-sm">Cancel</button>` : '-'}
            </td>
        </tr>
    `).join('');
}

async function cancelLeave(id) {
    await fetchAPI(`/leave/${id}/cancel`, 'POST');
    loadEmployeeLeaves();
}

async function loadNotifications() {
    const notifs = await fetchAPI(`/notifications/${currentUser.username}`);
    const ul = document.getElementById('notif-list');
    document.getElementById('notif-badge').innerText = notifs.length;
    ul.innerHTML = notifs.map(n => `
        <li>
            <div class="text-sm">${new Date(n.createdAt).toLocaleDateString()}</div>
            <div>${n.message}</div>
        </li>
    `).join('');
}

async function loadCalendar() {
    const events = await fetchAPI('/calendar');
    const container = document.getElementById('calendar-container');
    
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const firstDayIndex = new Date(year, month, 1).getDay();
    
    const dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    let html = '<div class="calendar-grid">';
    
    dayNames.forEach(d => html += `<div class="calendar-day-header">${d}</div>`);
    for(let i=0; i<firstDayIndex; i++) {
        html += `<div class="calendar-day" style="opacity:0.2"></div>`;
    }
    
    for(let d=1; d<=daysInMonth; d++) {
        const currentDateStr = `${year}-${String(month+1).padStart(2,'0')}-${String(d).padStart(2,'0')}`;
        let matchingEvents = '';
        
        events.forEach(ev => {
            if(currentDateStr >= ev.start && currentDateStr <= ev.end) {
                matchingEvents += `<div class="leave-strip" style="background-color:${ev.color}" title="${ev.status}">${ev.title}</div>`;
            }
        });
        
        html += `<div class="calendar-day"><strong>${d}</strong>${matchingEvents}</div>`;
    }
    
    html += '</div>';
    container.innerHTML = html;
}

// MEMBER 2: Manager Features
async function loadManagerPendingLeaves() {
    const leaves = await fetchAPI('/leaves/pending');
    const tbody = document.querySelector('#pending-leaves-table tbody');
    tbody.innerHTML = leaves.map(l => `
        <tr>
            <td>${l.employee.name} (${l.employee.department})</td>
            <td>${l.leaveType.name}</td>
            <td>${l.startDate} to ${l.endDate}</td>
            <td>${l.reason}</td>
            <td>
                <button onclick="leaveAction(${l.id}, 'approve')" class="btn btn-success btn-sm">Approve</button>
                <button onclick="leaveAction(${l.id}, 'reject')" class="btn btn-danger btn-sm">Reject</button>
            </td>
        </tr>
    `).join('');
}

async function leaveAction(id, action) {
    await fetchAPI(`/leave/${id}/${action}`, 'POST');
    loadManagerPendingLeaves();
}

// MEMBER 3 & 4: Admin Features
async function loadAdminLeaveTypes() {
    const types = await fetchAPI('/leavetypes');
    const ul = document.getElementById('leavetype-list');
    ul.innerHTML = types.map(t => `
        <li>
            <strong>${t.name}</strong> - Max ${t.maxDays} Days
            <div class="text-muted text-sm">${t.description}</div>
        </li>
    `).join('');
}

async function addLeaveType(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('lt-name').value,
        maxDays: document.getElementById('lt-days').value,
        description: document.getElementById('lt-desc').value
    };
    await fetchAPI('/leavetypes', 'POST', payload);
    document.getElementById('leavetype-form').reset();
    loadAdminLeaveTypes();
}

async function loadReports() {
    const reports = await fetchAPI('/reports');
    const container = document.getElementById('reports-list');
    container.innerHTML = reports.map(r => `
        <div class="card mb-2" style="background: rgba(0,0,0,0.2)">
            <strong>${r.title}</strong> - ${new Date(r.generatedOn).toLocaleDateString()}
            <p>${r.content}</p>
            <small class="text-muted">Generated by: ${r.generatedBy.name}</small>
        </div>
    `).join('');
}

async function generateReport() {
    await fetchAPI('/reports/generate', 'POST');
    loadReports();
}

/**
 * Authentication Module
 * Handles login, logout, and session management
 */

document.addEventListener('DOMContentLoaded', function() {
    const logoutBtn = document.getElementById('logoutBtn');
    const loginForm = document.getElementById('loginForm');
    
    // Handle logout
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            logout();
        });
    }
    
    // Handle login form submission
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            handleLogin();
        });
    }
});

/**
 * Handle login form submission
 */
function handleLogin() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorAlert = document.getElementById('errorAlert');
    
    // Validate inputs
    if (!username || !password) {
        showError('Please fill in all fields', errorAlert);
        return;
    }
    
    // Send login request
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Store token and redirect
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('userName', data.userName);
            localStorage.setItem('userRole', data.userRole);
            window.location.href = '/dashboard';
        } else {
            showError(data.message || 'Login failed. Please try again.', errorAlert);
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        showError('An error occurred. Please try again.', errorAlert);
    });
}

/**
 * Handle logout
 */
function logout() {
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        }
    })
    .then(response => {
        localStorage.clear();
        window.location.href = '/login';
    })
    .catch(error => {
        console.error('Logout error:', error);
        localStorage.clear();
        window.location.href = '/login';
    });
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return localStorage.getItem('authToken') !== null;
}

/**
 * Get auth token
 */
function getAuthToken() {
    return localStorage.getItem('authToken');
}

/**
 * Get current user
 */
function getCurrentUser() {
    return {
        name: localStorage.getItem('userName'),
        role: localStorage.getItem('userRole')
    };
}

/**
 * Make authenticated API call
 */
function apiCall(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getAuthToken(),
        ...options.headers
    };
    
    return fetch(url, {
        ...options,
        headers: headers
    }).then(response => {
        if (response.status === 401) {
            // Token expired, redirect to login
            localStorage.clear();
            window.location.href = '/login';
        }
        return response.json();
    });
}

/**
 * Show error message
 */
function showError(message, element) {
    if (element) {
        element.textContent = message;
        element.style.display = 'block';
        setTimeout(() => {
            element.style.display = 'none';
        }, 5000);
    } else {
        alert(message);
    }
}

/**
 * Show success message
 */
function showSuccess(message, element) {
    if (element) {
        element.textContent = message;
        element.style.display = 'block';
        element.className = 'success-msg';
        element.style.background = '#4CAF50';
        setTimeout(() => {
            element.style.display = 'none';
        }, 5000);
    }
}

// Redirect to login if not authenticated (for protected pages)
window.addEventListener('load', function() {
    const isLoginPage = window.location.pathname === '/login';
    const isPublicPage = window.location.pathname === '/';
    
    if (!isLoginPage && !isPublicPage && !isAuthenticated()) {
        window.location.href = '/login';
    }
});

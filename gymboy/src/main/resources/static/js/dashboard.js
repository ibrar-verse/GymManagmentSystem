/**
 * Dashboard Module
 * Handles dashboard statistics and recent activity
 */

document.addEventListener('DOMContentLoaded', function() {
    const user = getCurrentUser();
    const userNameElement = document.getElementById('userName');
    
    if (userNameElement) {
        userNameElement.textContent = user.name || 'User';
    }
    
    // Load dashboard data
    loadDashboardStats();
    loadRecentCheckIns();
    
    // Refresh data every 5 minutes
    setInterval(() => {
        loadDashboardStats();
        loadRecentCheckIns();
    }, 300000);
});

/**
 * Load dashboard statistics
 */
function loadDashboardStats() {
    apiCall('/api/dashboard/stats')
        .then(data => {
            if (data.success) {
                updateStatCards(data.stats);
            }
        })
        .catch(error => console.error('Error loading stats:', error));
}

/**
 * Update stat cards with data
 */
function updateStatCards(stats) {
    const elements = {
        'totalMembers': document.getElementById('totalMembers'),
        'todayCheckIns': document.getElementById('todayCheckIns'),
        'totalStaff': document.getElementById('totalStaff'),
        'totalClasses': document.getElementById('totalClasses')
    };
    
    Object.keys(elements).forEach(key => {
        if (elements[key] && stats[key] !== undefined) {
            elements[key].textContent = stats[key];
            animateNumber(elements[key], stats[key]);
        }
    });
}

/**
 * Animate number change
 */
function animateNumber(element, targetNumber) {
    const currentNumber = parseInt(element.textContent);
    const diff = targetNumber - currentNumber;
    const steps = 20;
    let currentStep = 0;
    
    const interval = setInterval(() => {
        currentStep++;
        const newNumber = Math.ceil(currentNumber + (diff / steps) * currentStep);
        element.textContent = newNumber;
        
        if (currentStep >= steps) {
            element.textContent = targetNumber;
            clearInterval(interval);
        }
    }, 50);
}

/**
 * Load recent check-ins
 */
function loadRecentCheckIns() {
    apiCall('/api/check-in/recent')
        .then(data => {
            if (data.success && data.checkIns) {
                populateCheckInTable(data.checkIns);
            }
        })
        .catch(error => console.error('Error loading check-ins:', error));
}

/**
 * Populate check-in table
 */
function populateCheckInTable(checkIns) {
    const tableBody = document.getElementById('checkInsTableBody');
    
    if (checkIns.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--dark-gold);">No check-ins yet today</td></tr>';
        return;
    }
    
    tableBody.innerHTML = checkIns.slice(0, 10).map(checkIn => `
        <tr>
            <td>${sanitize(checkIn.memberName)}</td>
            <td>${formatTime(checkIn.checkInTime)}</td>
            <td>${formatDuration(checkIn.duration)}</td>
            <td>
                <span style="color: ${checkIn.isActive ? '#4CAF50' : '#FFD700'};">
                    ${checkIn.isActive ? 'Active' : 'Inactive'}
                </span>
            </td>
        </tr>
    `).join('');
}

/**
 * Format time for display
 */
function formatTime(timestamp) {
    if (!timestamp) return 'N/A';
    const date = new Date(timestamp);
    return date.toLocaleTimeString();
}

/**
 * Format duration for display
 */
function formatDuration(minutes) {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
}

/**
 * Sanitize output to prevent XSS
 */
function sanitize(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

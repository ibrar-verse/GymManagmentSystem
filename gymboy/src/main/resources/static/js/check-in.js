/**
 * Check-in Module
 * Handles member check-in operations
 */

document.addEventListener('DOMContentLoaded', function() {
    loadFacilities();
    loadTodaysSummary();
    loadCheckInHistory();
    setupEventListeners();
    
    // Refresh data every 2 minutes
    setInterval(() => {
        loadCheckInHistory();
        loadTodaysSummary();
    }, 120000);
});

/**
 * Setup event listeners
 */
function setupEventListeners() {
    const checkInForm = document.getElementById('checkInForm');
    
    if (checkInForm) {
        checkInForm.addEventListener('submit', handleCheckIn);
    }
}

/**
 * Load facilities
 */
function loadFacilities() {
    apiCall('/api/facilities/list')
        .then(data => {
            if (data.success && data.facilities) {
                const facilitySelect = document.getElementById('facility');
                facilitySelect.innerHTML = '<option value="">Select Facility</option>' +
                    data.facilities.map(facility => 
                        `<option value="${facility.id}">${sanitize(facility.name)}</option>`
                    ).join('');
            }
        })
        .catch(error => console.error('Error loading facilities:', error));
}

/**
 * Handle check-in
 */
function handleCheckIn(e) {
    e.preventDefault();
    
    const memberId = document.getElementById('memberId').value;
    const facilityId = document.getElementById('facility').value;
    const messageDiv = document.getElementById('checkInMessage');
    
    if (!memberId || !facilityId) {
        showErrorMessage('Please fill in all fields', messageDiv);
        return;
    }
    
    apiCall('/api/check-in/process', {
        method: 'POST',
        body: JSON.stringify({
            memberIdOrEmail: memberId,
            facilityId: facilityId
        })
    })
    .then(data => {
        if (data.success) {
            showSuccessMessage(`Check-in successful for ${data.memberName}!`, messageDiv);
            document.getElementById('checkInForm').reset();
            loadCheckInHistory();
            loadTodaysSummary();
        } else {
            showErrorMessage(data.message || 'Check-in failed', messageDiv);
        }
    })
    .catch(error => {
        console.error('Error during check-in:', error);
        showErrorMessage('An error occurred. Please try again.', messageDiv);
    });
}

/**
 * Load today's summary
 */
function loadTodaysSummary() {
    apiCall('/api/check-in/today-summary')
        .then(data => {
            if (data.success) {
                document.getElementById('totalCheckIns').textContent = data.summary.totalCheckIns || 0;
                document.getElementById('activeMembers').textContent = data.summary.activeMembers || 0;
                document.getElementById('peakTime').textContent = data.summary.peakTime || 'N/A';
            }
        })
        .catch(error => console.error('Error loading summary:', error));
}

/**
 * Load check-in history
 */
function loadCheckInHistory() {
    apiCall('/api/check-in/today')
        .then(data => {
            if (data.success && data.checkIns) {
                populateCheckInHistory(data.checkIns);
            }
        })
        .catch(error => console.error('Error loading check-in history:', error));
}

/**
 * Populate check-in history table
 */
function populateCheckInHistory(checkIns) {
    const tableBody = document.getElementById('checkInHistoryBody');
    
    if (checkIns.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--dark-gold);">No check-ins today</td></tr>';
        return;
    }
    
    tableBody.innerHTML = checkIns.map(checkIn => `
        <tr>
            <td>${sanitize(checkIn.memberName)}</td>
            <td>${formatTime(checkIn.checkInTime)}</td>
            <td>${sanitize(checkIn.facilityName)}</td>
            <td>${formatDuration(checkIn.durationMinutes)}</td>
            <td>
                <span style="color: ${checkIn.isActive ? '#4CAF50' : '#FFD700'};">
                    ${checkIn.isActive ? 'Active' : 'Checked Out'}
                </span>
            </td>
        </tr>
    `).join('');
}

/**
 * Show error message
 */
function showErrorMessage(message, element) {
    element.textContent = message;
    element.style.background = '#ff4444';
    element.style.color = 'white';
    element.style.display = 'block';
    setTimeout(() => {
        element.style.display = 'none';
    }, 5000);
}

/**
 * Show success message
 */
function showSuccessMessage(message, element) {
    element.textContent = message;
    element.style.background = '#4CAF50';
    element.style.color = 'white';
    element.style.display = 'block';
    setTimeout(() => {
        element.style.display = 'none';
    }, 5000);
}

/**
 * Format time
 */
function formatTime(timestamp) {
    if (!timestamp) return 'N/A';
    const date = new Date(timestamp);
    return date.toLocaleTimeString();
}

/**
 * Format duration
 */
function formatDuration(minutes) {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
}

/**
 * Sanitize output
 */
function sanitize(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

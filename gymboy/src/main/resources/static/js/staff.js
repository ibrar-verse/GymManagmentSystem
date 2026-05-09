/**
 * Staff Module
 * Handles staff management (CRUD operations)
 */

let currentEditingStaffId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadStaff();
    setupEventListeners();
});

/**
 * Setup event listeners
 */
function setupEventListeners() {
    const addStaffBtn = document.getElementById('addStaffBtn');
    const staffForm = document.getElementById('staffForm');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    
    if (addStaffBtn) {
        addStaffBtn.addEventListener('click', openAddStaffModal);
    }
    
    if (staffForm) {
        staffForm.addEventListener('submit', handleStaffFormSubmit);
    }
    
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }
    
    if (searchBtn) {
        searchBtn.addEventListener('click', handleSearch);
    }
    
    if (searchInput) {
        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') handleSearch();
        });
    }
    
    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        const modal = document.getElementById('staffModal');
        if (e.target === modal) {
            closeModal();
        }
    });
}

/**
 * Load all staff
 */
function loadStaff() {
    apiCall('/api/staff/list')
        .then(data => {
            if (data.success && data.staff) {
                populateStaffTable(data.staff);
            }
        })
        .catch(error => console.error('Error loading staff:', error));
}

/**
 * Populate staff table
 */
function populateStaffTable(staff) {
    const tableBody = document.getElementById('staffTableBody');
    
    if (staff.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" style="text-align: center; color: var(--dark-gold);">No staff found</td></tr>';
        return;
    }
    
    tableBody.innerHTML = staff.map(member => `
        <tr>
            <td>${sanitize(member.name)}</td>
            <td>${sanitize(member.email)}</td>
            <td>${sanitize(member.phone)}</td>
            <td>${sanitize(member.position || 'N/A')}</td>
            <td>${formatDate(member.hireDate)}</td>
            <td>
                <span style="color: ${member.active ? '#4CAF50' : '#FF6B6B'};">
                    ${member.active ? 'Active' : 'Inactive'}
                </span>
            </td>
            <td>
                <button class="btn-primary" onclick="editStaff(${member.id})" style="padding: 0.4rem 0.8rem; margin-right: 0.5rem;">Edit</button>
                <button class="btn-delete" onclick="deleteStaff(${member.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

/**
 * Open add staff modal
 */
function openAddStaffModal() {
    currentEditingStaffId = null;
    document.getElementById('modalTitle').textContent = 'Add New Staff';
    document.getElementById('staffForm').reset();
    openModal();
}

/**
 * Edit staff
 */
function editStaff(staffId) {
    apiCall(`/api/staff/${staffId}`)
        .then(data => {
            if (data.success && data.staff) {
                currentEditingStaffId = staffId;
                const staff = data.staff;
                
                document.getElementById('modalTitle').textContent = 'Edit Staff';
                document.getElementById('staffName').value = staff.name;
                document.getElementById('staffEmail').value = staff.email;
                document.getElementById('staffPhone').value = staff.phone;
                document.getElementById('staffPosition').value = staff.position;
                
                openModal();
            }
        })
        .catch(error => console.error('Error loading staff:', error));
}

/**
 * Delete staff
 */
function deleteStaff(staffId) {
    if (!confirm('Are you sure you want to delete this staff member?')) {
        return;
    }
    
    apiCall(`/api/staff/${staffId}`, {
        method: 'DELETE'
    })
    .then(data => {
        if (data.success) {
            showSuccess('Staff deleted successfully', null);
            loadStaff();
        } else {
            showError(data.message || 'Failed to delete staff', null);
        }
    })
    .catch(error => console.error('Error deleting staff:', error));
}

/**
 * Handle staff form submission
 */
function handleStaffFormSubmit(e) {
    e.preventDefault();
    
    const staffData = {
        name: document.getElementById('staffName').value,
        email: document.getElementById('staffEmail').value,
        phone: document.getElementById('staffPhone').value,
        position: document.getElementById('staffPosition').value
    };
    
    // Validate
    if (!staffData.name || !staffData.email || !staffData.phone || !staffData.position) {
        showError('Please fill in all fields', null);
        return;
    }
    
    const url = currentEditingStaffId 
        ? `/api/staff/${currentEditingStaffId}` 
        : '/api/staff/create';
    const method = currentEditingStaffId ? 'PUT' : 'POST';
    
    apiCall(url, {
        method: method,
        body: JSON.stringify(staffData)
    })
    .then(data => {
        if (data.success) {
            showSuccess(currentEditingStaffId ? 'Staff updated successfully' : 'Staff created successfully', null);
            closeModal();
            loadStaff();
        } else {
            showError(data.message || 'Operation failed', null);
        }
    })
    .catch(error => console.error('Error saving staff:', error));
}

/**
 * Handle search
 */
function handleSearch() {
    const searchTerm = document.getElementById('searchInput').value;
    
    if (!searchTerm) {
        loadStaff();
        return;
    }
    
    apiCall(`/api/staff/search?q=${encodeURIComponent(searchTerm)}`)
        .then(data => {
            if (data.success && data.staff) {
                populateStaffTable(data.staff);
            }
        })
        .catch(error => console.error('Error searching staff:', error));
}

/**
 * Open modal
 */
function openModal() {
    document.getElementById('staffModal').classList.add('active');
}

/**
 * Close modal
 */
function closeModal() {
    document.getElementById('staffModal').classList.remove('active');
}

/**
 * Format date
 */
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

/**
 * Sanitize output
 */
function sanitize(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

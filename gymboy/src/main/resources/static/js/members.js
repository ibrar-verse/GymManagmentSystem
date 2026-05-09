/**
 * Members Module
 * Handles member management (CRUD operations)
 */

let currentEditingMemberId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadMembers();
    setupEventListeners();
});

/**
 * Setup event listeners
 */
function setupEventListeners() {
    const addMemberBtn = document.getElementById('addMemberBtn');
    const memberForm = document.getElementById('memberForm');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    
    if (addMemberBtn) {
        addMemberBtn.addEventListener('click', openAddMemberModal);
    }
    
    if (memberForm) {
        memberForm.addEventListener('submit', handleMemberFormSubmit);
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
        const modal = document.getElementById('memberModal');
        if (e.target === modal) {
            closeModal();
        }
    });
}

/**
 * Load all members
 */
function loadMembers() {
    apiCall('/api/members/list')
        .then(data => {
            if (data.success && data.members) {
                populateMembersTable(data.members);
            }
        })
        .catch(error => console.error('Error loading members:', error));
}

/**
 * Populate members table
 */
function populateMembersTable(members) {
    const tableBody = document.getElementById('membersTableBody');
    
    if (members.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" style="text-align: center; color: var(--dark-gold);">No members found</td></tr>';
        return;
    }
    
    tableBody.innerHTML = members.map(member => `
        <tr>
            <td>${sanitize(member.name)}</td>
            <td>${sanitize(member.email)}</td>
            <td>${sanitize(member.phone)}</td>
            <td>${sanitize(member.membershipTier || 'N/A')}</td>
            <td>${formatDate(member.joinDate)}</td>
            <td>
                <span style="color: ${member.active ? '#4CAF50' : '#FF6B6B'};">
                    ${member.active ? 'Active' : 'Inactive'}
                </span>
            </td>
            <td>
                <button class="btn-primary" onclick="editMember(${member.id})" style="padding: 0.4rem 0.8rem; margin-right: 0.5rem;">Edit</button>
                <button class="btn-delete" onclick="deleteMember(${member.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

/**
 * Open add member modal
 */
function openAddMemberModal() {
    currentEditingMemberId = null;
    document.getElementById('modalTitle').textContent = 'Add New Member';
    document.getElementById('memberForm').reset();
    openModal();
}

/**
 * Edit member
 */
function editMember(memberId) {
    apiCall(`/api/members/${memberId}`)
        .then(data => {
            if (data.success && data.member) {
                currentEditingMemberId = memberId;
                const member = data.member;
                
                document.getElementById('modalTitle').textContent = 'Edit Member';
                document.getElementById('memberName').value = member.name;
                document.getElementById('memberEmail').value = member.email;
                document.getElementById('memberPhone').value = member.phone;
                document.getElementById('memberTier').value = member.membershipTier;
                
                openModal();
            }
        })
        .catch(error => console.error('Error loading member:', error));
}

/**
 * Delete member
 */
function deleteMember(memberId) {
    if (!confirm('Are you sure you want to delete this member?')) {
        return;
    }
    
    apiCall(`/api/members/${memberId}`, {
        method: 'DELETE'
    })
    .then(data => {
        if (data.success) {
            showSuccess('Member deleted successfully', null);
            loadMembers();
        } else {
            showError(data.message || 'Failed to delete member', null);
        }
    })
    .catch(error => console.error('Error deleting member:', error));
}

/**
 * Handle member form submission
 */
function handleMemberFormSubmit(e) {
    e.preventDefault();
    
    const memberData = {
        name: document.getElementById('memberName').value,
        email: document.getElementById('memberEmail').value,
        phone: document.getElementById('memberPhone').value,
        membershipTier: document.getElementById('memberTier').value
    };
    
    // Validate
    if (!memberData.name || !memberData.email || !memberData.phone || !memberData.membershipTier) {
        showError('Please fill in all fields', null);
        return;
    }
    
    const url = currentEditingMemberId 
        ? `/api/members/${currentEditingMemberId}` 
        : '/api/members/create';
    const method = currentEditingMemberId ? 'PUT' : 'POST';
    
    apiCall(url, {
        method: method,
        body: JSON.stringify(memberData)
    })
    .then(data => {
        if (data.success) {
            showSuccess(currentEditingMemberId ? 'Member updated successfully' : 'Member created successfully', null);
            closeModal();
            loadMembers();
        } else {
            showError(data.message || 'Operation failed', null);
        }
    })
    .catch(error => console.error('Error saving member:', error));
}

/**
 * Handle search
 */
function handleSearch() {
    const searchTerm = document.getElementById('searchInput').value;
    
    if (!searchTerm) {
        loadMembers();
        return;
    }
    
    apiCall(`/api/members/search?q=${encodeURIComponent(searchTerm)}`)
        .then(data => {
            if (data.success && data.members) {
                populateMembersTable(data.members);
            }
        })
        .catch(error => console.error('Error searching members:', error));
}

/**
 * Open modal
 */
function openModal() {
    document.getElementById('memberModal').classList.add('active');
}

/**
 * Close modal
 */
function closeModal() {
    document.getElementById('memberModal').classList.remove('active');
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

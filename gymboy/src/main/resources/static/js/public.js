/**
 * Public Website Module
 * Handles public-facing page interactions
 */

document.addEventListener('DOMContentLoaded', function() {
    loadFacilities();
    setupScrollAnimations();
});

/**
 * Load facilities for display
 */
function loadFacilities() {
    fetch('/api/public/facilities')
        .then(response => response.json())
        .then(data => {
            if (data.success && data.facilities) {
                displayFacilities(data.facilities);
            }
        })
        .catch(error => console.error('Error loading facilities:', error));
}

/**
 * Display facilities in grid
 */
function displayFacilities(facilities) {
    const grid = document.getElementById('facilitiesGrid');
    
    if (facilities.length === 0) {
        grid.innerHTML = '<p style="text-align: center; color: var(--dark-gold); grid-column: 1/-1;">No facilities available</p>';
        return;
    }
    
    grid.innerHTML = facilities.slice(0, 3).map(facility => `
        <div class="facility-card">
            <div style="font-size: 3rem; margin-bottom: 1rem;">
                ${facility.emoji || '🏋️'}
            </div>
            <h3 style="color: var(--gold); margin-bottom: 0.5rem;">${sanitize(facility.name)}</h3>
            <p>${sanitize(facility.description)}</p>
        </div>
    `).join('');
}

/**
 * Setup scroll animations
 */
function setupScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -100px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    document.querySelectorAll('.facility-card, .pricing-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(card);
    });
}

/**
 * Smooth scroll to section
 */
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

/**
 * Sanitize output
 */
function sanitize(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

/**
 * Add click handlers to subscription buttons
 */
document.querySelectorAll('.pricing-card .btn-primary').forEach(btn => {
    btn.addEventListener('click', function() {
        window.location.href = '/login';
    });
});

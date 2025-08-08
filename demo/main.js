// Professional Library Management System JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeLibrarySystem();
});

function initializeLibrarySystem() {
    // Initialize all components
    initializeFormValidation();
    initializeAnimations();
    initializeInteractiveElements();
    initializeSearch();
    initializeResponsiveMenu();
    initializeTooltips();
}

// Form Validation and Enhancement
function initializeFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        // Add loading states to form submissions
        form.addEventListener('submit', function() {
            const submitBtn = form.querySelector('input[type="submit"]');
            if (submitBtn) {
                submitBtn.classList.add('loading');
                submitBtn.value = 'Processing...';
            }
        });

        // Real-time validation for inputs
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(input);
            });

            input.addEventListener('input', function() {
                // Remove error state on input
                if (input.classList.contains('error')) {
                    input.classList.remove('error');
                }
            });
        });
    });
}

function validateField(field) {
    const value = field.value.trim();
    const fieldName = field.getAttribute('name') || field.getAttribute('id');
    
    // Remove existing error messages
    const existingError = field.parentElement.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }
    
    let isValid = true;
    let errorMessage = '';
    
    // Basic validation rules
    if (field.hasAttribute('required') && !value) {
        isValid = false;
        errorMessage = 'This field is required';
    } else if (fieldName === 'year' || fieldName === 'year_of_birth') {
        const year = parseInt(value);
        const currentYear = new Date().getFullYear();
        
        if (fieldName === 'year' && (year < 1000 || year > currentYear)) {
            isValid = false;
            errorMessage = `Please enter a valid year (1000-${currentYear})`;
        } else if (fieldName === 'year_of_birth' && (year < 1900 || year > currentYear)) {
            isValid = false;
            errorMessage = `Please enter a valid birth year (1900-${currentYear})`;
        }
    } else if (fieldName === 'name' && value.length < 2) {
        isValid = false;
        errorMessage = 'Name must be at least 2 characters long';
    } else if (fieldName === 'author' && value.length < 2) {
        isValid = false;
        errorMessage = 'Author name must be at least 2 characters long';
    } else if (fieldName === 'fio' && value.length < 3) {
        isValid = false;
        errorMessage = 'Full name must be at least 3 characters long';
    }
    
    if (!isValid) {
        field.classList.add('error');
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = errorMessage;
        field.parentElement.appendChild(errorDiv);
    } else {
        field.classList.remove('error');
    }
    
    return isValid;
}

// Animations and Visual Effects
function initializeAnimations() {
    // Fade in elements on page load
    const animatedElements = document.querySelectorAll('.card, .item-card, .form-container');
    animatedElements.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            element.style.transition = 'all 0.5s ease';
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Animate page title
    const pageTitle = document.querySelector('.page-title');
    if (pageTitle) {
        pageTitle.style.opacity = '0';
        pageTitle.style.transform = 'translateY(-20px)';
        setTimeout(() => {
            pageTitle.style.transition = 'all 0.6s ease';
            pageTitle.style.opacity = '1';
            pageTitle.style.transform = 'translateY(0)';
        }, 200);
    }
}

// Interactive Elements
function initializeInteractiveElements() {
    // Enhanced button interactions
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
        
        button.addEventListener('mousedown', function() {
            this.style.transform = 'translateY(0) scale(0.98)';
        });
        
        button.addEventListener('mouseup', function() {
            this.style.transform = 'translateY(-2px) scale(1)';
        });
    });
    
    // Confirm deletion actions
    const deleteButtons = document.querySelectorAll('input[value="Delete Book"], input[value="Delete Reader"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            showDeleteConfirmation(this);
        });
    });
}

function showDeleteConfirmation(button) {
    const itemType = button.value.includes('Book') ? 'book' : 'reader';
    const itemName = getItemName(itemType);
    
    if (confirm(`Are you sure you want to delete this ${itemType}${itemName ? ': ' + itemName : ''}? This action cannot be undone.`)) {
        // Proceed with deletion
        button.form.submit();
    }
}

function getItemName(itemType) {
    if (itemType === 'book') {
        const nameElement = document.querySelector('p:first-of-type');
        return nameElement ? nameElement.textContent : '';
    } else {
        const nameElement = document.querySelector('p:first-of-type');
        return nameElement ? nameElement.textContent : '';
    }
}

// Search Enhancement
function initializeSearch() {
    const searchInput = document.querySelector('input[name="query"]');
    if (searchInput) {
        // Add search icon
        const searchContainer = searchInput.parentElement;
        searchContainer.style.position = 'relative';
        
        const searchIcon = document.createElement('span');
        searchIcon.innerHTML = '🔍';
        searchIcon.style.position = 'absolute';
        searchIcon.style.right = '10px';
        searchIcon.style.top = '50%';
        searchIcon.style.transform = 'translateY(-50%)';
        searchIcon.style.fontSize = '1.2em';
        searchIcon.style.color = 'var(--text-secondary)';
        searchContainer.appendChild(searchIcon);
        
        searchInput.style.paddingRight = '40px';
        
        // Live search feedback
        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            if (query.length > 0) {
                searchIcon.style.color = 'var(--primary-color)';
            } else {
                searchIcon.style.color = 'var(--text-secondary)';
            }
        });
    }
}

// Responsive Navigation
function initializeResponsiveMenu() {
    const navbar = document.querySelector('.navbar-content');
    if (navbar && window.innerWidth < 768) {
        // Add mobile menu functionality if needed
        addMobileMenuToggle();
    }
}

function addMobileMenuToggle() {
    const navbar = document.querySelector('.navbar-content');
    const menuToggle = document.createElement('button');
    menuToggle.innerHTML = '☰';
    menuToggle.className = 'mobile-menu-toggle';
    menuToggle.style.cssText = `
        background: none;
        border: none;
        font-size: 1.5rem;
        cursor: pointer;
        display: none;
        color: var(--primary-color);
        padding: 0.5rem;
        border-radius: var(--border-radius-md);
    `;
    
    if (window.innerWidth < 768) {
        menuToggle.style.display = 'block';
        navbar.parentElement.insertBefore(menuToggle, navbar);
        
        menuToggle.addEventListener('click', function() {
            navbar.style.display = navbar.style.display === 'none' ? 'flex' : 'none';
        });
    }
}

// Tooltips for better UX
function initializeTooltips() {
    const tooltipElements = [
        { selector: 'input[value="Release Book"]', text: 'Make this book available for other readers' },
        { selector: 'input[value="Assign Book"]', text: 'Assign this book to the selected reader' },
        { selector: 'a[href*="edit"]', text: 'Edit this item' },
        { selector: 'input[type="submit"][value*="Delete"]', text: 'Permanently delete this item' }
    ];
    
    tooltipElements.forEach(({ selector, text }) => {
        const elements = document.querySelectorAll(selector);
        elements.forEach(element => {
            element.title = text;
        });
    });
}

// Utility Functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 1.5rem;
        border-radius: var(--border-radius-md);
        color: white;
        font-weight: 500;
        z-index: 1000;
        animation: slideInRight 0.3s ease;
        box-shadow: var(--shadow-lg);
    `;
    
    const colors = {
        success: 'var(--success-color)',
        error: 'var(--error-color)',
        warning: 'var(--warning-color)',
        info: 'var(--primary-color)'
    };
    
    notification.style.backgroundColor = colors[type] || colors.info;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Add CSS for notification animations
const notificationStyles = document.createElement('style');
notificationStyles.textContent = `
    @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(notificationStyles);

// Handle form submissions with better UX
document.addEventListener('submit', function(e) {
    const form = e.target;
    const submitButton = form.querySelector('input[type="submit"]');
    
    if (submitButton) {
        const originalValue = submitButton.value;
        submitButton.value = 'Processing...';
        submitButton.disabled = true;
        
        // Re-enable after 3 seconds in case of errors
        setTimeout(() => {
            submitButton.value = originalValue;
            submitButton.disabled = false;
        }, 3000);
    }
});

// Window resize handler
window.addEventListener('resize', debounce(() => {
    initializeResponsiveMenu();
}, 250));

// Page visibility change handler
document.addEventListener('visibilitychange', function() {
    if (document.visibilityState === 'visible') {
        // Refresh animations when page becomes visible
        initializeAnimations();
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + K for search focus
    if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        const searchInput = document.querySelector('input[name="query"]');
        if (searchInput) {
            searchInput.focus();
        }
    }
    
    // Escape key to clear focused elements
    if (e.key === 'Escape') {
        document.activeElement.blur();
    }
});

console.log('Library Management System initialized successfully! 📚');
(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    
    // Initiate the wowjs
    new WOW().init();


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(window).width() < 992) {
            if ($(this).scrollTop() > 45) {
                $('.fixed-top').addClass('bg-dark shadow');
            } else {
                $('.fixed-top').removeClass('bg-dark shadow');
            }
        } else {
            if ($(this).scrollTop() > 45) {
                $('.fixed-top').addClass('bg-dark shadow').css('top', -45);
            } else {
                $('.fixed-top').removeClass('bg-dark shadow').css('top', 0);
            }
        }
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // // Causes progress
    // $('.causes-progress').waypoint(function () {
    //     $('.progress .progress-bar').each(function () {
    //         $(this).css("width", $(this).attr("aria-valuenow") + '%');
    //     });
    // }, {offset: '80%'});


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: false,
        smartSpeed: 1000,
        center: true,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            768:{
                items:2
            }
        }
    });

    
})(jQuery);

document.addEventListener('DOMContentLoaded', function() {
    // Get DOM elements
    const amountBadges = document.querySelectorAll('.amount-badge');
    const amountInput = document.getElementById('amount');

    // Format amount with commas
    function formatAmount(amount) {
        return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // Handle badge clicks
    amountBadges.forEach(badge => {
        badge.addEventListener('click', function() {
            // Remove active class from all badges
            amountBadges.forEach(b => b.classList.remove('active'));
            
            // Add active class to clicked badge
            this.classList.add('active');
            
            // Set amount input value
            const amount = parseInt(this.dataset.amount);
            amountInput.value = amount;
            
            // Update badge display
            this.textContent = formatAmount(amount) + 'đ';
            
            // Validate input
            validateAmount(amount);
        });
    });

    // Validate amount
    function validateAmount(amount) {
        const isValid = amount >= 10000;
        amountInput.setCustomValidity(isValid ? '' : 'Amount must be at least 10,000 VND');
        
        if (isValid) {
            amountInput.classList.remove('is-invalid');
            amountInput.classList.add('is-valid');
        } else {
            amountInput.classList.remove('is-valid');
            amountInput.classList.add('is-invalid');
        }
    }
    // Form validation
    const form = document.querySelector('form');
    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    });
});
document.getElementById('sendOtpBtn').addEventListener('click', function() {
    const newEmail = document.getElementById('newEmail').value;
    if (newEmail) {
        fetch('/send-otp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ newEmail: newEmail })
        })
        .then(response => response.json())
        .then(data => {
            console.log(data); 
            if (data.success) {
                document.getElementById('otp').disabled = false;
                document.getElementById('submitBtn').disabled = false;
                alert('OTP sent to your new email address.');
            } else {
                alert('Failed to send OTP. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        });
    } else {
        alert('Please enter a new email address.');
    }
});

// Show toast 
document.addEventListener('DOMContentLoaded', function () {
    var toastElList = [].slice.call(document.querySelectorAll('.toast'))
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl)
    })
    toastList.forEach(toast => toast.show())
});
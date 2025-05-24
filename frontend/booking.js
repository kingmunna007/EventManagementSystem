// Function to handle redirection to "My Bookings" page
function redirectToBookings() {
    alert('Redirecting to your bookings page...');
    window.location.href = 'my-bookings.html';
  }

let eventBasePrices = {};
let verifiedDiscount = null;

// Fetch events for the dropdown and store base prices
window.addEventListener('DOMContentLoaded', () => {
    const eventSelect = document.getElementById('eventId');
    eventSelect.innerHTML = '<option value="">Loading events...</option>';
    const adminUsername = 'admin user';
    const adminPassword = 'pass3';
    const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
    fetch('http://localhost:8080/api/events', {
        headers: { 'Authorization': basicAuth }
    })
        .then(res => res.json())
        .then(events => {
            eventSelect.innerHTML = '<option value="">Select an event</option>';
            events.forEach(event => {
                eventSelect.innerHTML += `<option value="${event.id}">${event.name}</option>`;
                eventBasePrices[event.id] = event.basePrice;
            });
        })
        .catch(() => {
            eventSelect.innerHTML = '<option value="">Failed to load events</option>';
        });
});

// Discount verification
const verifyDiscountBtn = document.getElementById('verifyDiscountBtn');
if (verifyDiscountBtn) {
    verifyDiscountBtn.addEventListener('click', async function() {
        const code = document.getElementById('discountCode').value.trim();
        const discountStatus = document.getElementById('discountStatus');
        if (!code) {
            discountStatus.textContent = 'Enter a code.';
            discountStatus.style.color = 'orange';
            verifiedDiscount = null;
            return;
        }
        try {
            const adminUsername = 'admin user';
            const adminPassword = 'pass3';
            const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
            const res = await fetch(`http://localhost:8080/api/discounts/${encodeURIComponent(code)}`, {
                headers: { 'Authorization': basicAuth }
            });
            if (res.ok) {
                const discount = await res.json();
                discountStatus.textContent = `Valid: ${discount.percentage}% off`;
                discountStatus.style.color = 'green';
                verifiedDiscount = discount;
            } else {
                discountStatus.textContent = 'Invalid code.';
                discountStatus.style.color = 'red';
                verifiedDiscount = null;
            }
        } catch (err) {
            discountStatus.textContent = 'Error verifying code.';
            discountStatus.style.color = 'red';
            verifiedDiscount = null;
        }
    });
}

// Price calculation logic
function calculateTotalPrice() {
    const eventId = document.getElementById('eventId').value;
    const participants = parseInt(document.getElementById('participants').value, 10);
    const totalPriceSpan = document.getElementById('totalPrice');
    if (!eventId || !participants || !eventBasePrices[eventId]) {
        totalPriceSpan.textContent = '-';
        return;
    }
    let basePrice = parseFloat(eventBasePrices[eventId]);
    let total = basePrice * participants;
    if (verifiedDiscount && verifiedDiscount.percentage) {
        total = total - (total * (verifiedDiscount.percentage / 100));
    }
    totalPriceSpan.textContent = `₹${total.toFixed(2)}`;
    return total;
}

const calculatePriceBtn = document.getElementById('calculatePriceBtn');
if (calculatePriceBtn) {
    calculatePriceBtn.addEventListener('click', calculateTotalPrice);
}

// Handle booking form submission
const bookingForm = document.getElementById('bookingForm');
if (bookingForm) {
    bookingForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const userId = localStorage.getItem('userId') || prompt('Enter your user ID:');
        const eventId = document.getElementById('eventId').value;
        const participants = document.getElementById('participants').value;
        const bookingType = document.getElementById('bookingType').value;
        const discountCode = document.getElementById('discountCode').value;
        const bookingResult = document.getElementById('bookingResult');
        if (!userId || !eventId || !participants || !bookingType) {
            bookingResult.textContent = 'Please fill all required fields.';
            return;
        }
        // Calculate price before sending
        const finalPrice = calculateTotalPrice();
        const params = new URLSearchParams({
            userId,
            eventId,
            participants,
            bookingType
        });
        if (discountCode) params.append('discountCode', discountCode);
        try {
            const adminUsername = 'admin user';
            const adminPassword = 'pass3';
            const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
            const response = await fetch(`http://localhost:8080/bookings/create?${params.toString()}`, {
                method: 'POST',
                headers: { 'Authorization': basicAuth, 'Content-Type': 'application/json' },
                body: JSON.stringify({ finalPrice })
            });
            if (response.ok) {
                bookingResult.textContent = 'Booking successful!';
                alert('Booking successful!');
            } else {
                const error = await response.text();
                bookingResult.textContent = 'Booking failed: ' + error;
            }
        } catch (err) {
            bookingResult.textContent = 'Booking failed: ' + err.message;
        }
    });
}

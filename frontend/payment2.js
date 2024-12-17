// Tab switching functionality
function showTab(tabId) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
      tab.classList.remove('active');
    });
  
    // Remove active class from buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
      btn.classList.remove('active');
    });
  
    // Show selected tab
    document.getElementById(tabId).classList.add('active');
    event.target.classList.add('active');
  }
  
  // Simulate UPI Payment
  function simulatePayment(appName) {
    alert(`Redirecting to ${appName} for payment...`);
  }
  
  // Process Card Payment
  function processCardPayment() {
    const cardNumber = document.getElementById('card-number').value.trim();
    const expiry = document.getElementById('expiry').value.trim();
    const cvv = document.getElementById('cvv').value.trim();
  
    if (cardNumber.length < 16 || expiry.length < 5 || cvv.length < 3) {
      alert('Please enter valid card details.');
      return;
    }
  
    alert('Payment successful! Thank you for your booking.');
  }
  
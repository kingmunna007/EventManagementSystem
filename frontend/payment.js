document.addEventListener('DOMContentLoaded', () => {
    const participantCount = 100; // Number of participants
    const basePrice = 100; // Base price per participant
    const vipMultiplier = 1; // Booking type multiplier
    const discountCodes = {
      SAVE10: 0.1, // 10% discount
      VIP50: 0.5,  // 50% discount
    };
  
    const totalPriceElement = document.getElementById('total-price');
    const finalPriceElement = document.getElementById('final-price');
    const discountInput = document.getElementById('discount-code');
    const applyDiscountButton = document.getElementById('apply-discount');
  
    let totalPrice = participantCount * basePrice * vipMultiplier;
    totalPriceElement.textContent = `$${totalPrice.toLocaleString()}`;
    finalPriceElement.textContent = `$${totalPrice.toLocaleString()}`;
  
    applyDiscountButton.addEventListener('click', () => {
      const discountCode = discountInput.value.trim().toUpperCase();
      if (discountCodes[discountCode]) {
        const discount = discountCodes[discountCode];
        const discountedPrice = totalPrice * (1 - discount);
        finalPriceElement.textContent = `$${discountedPrice.toLocaleString()}`;
        alert('Discount applied successfully!');
      } else {
        alert('Invalid discount code!');
      }
    });
  });
  
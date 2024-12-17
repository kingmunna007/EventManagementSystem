// Add functionality to buttons if needed
document.querySelectorAll('.view-details-btn').forEach((button, index) => {
    button.addEventListener('click', () => {
      alert(`More details for Event ${index + 1}`);
    });
  });
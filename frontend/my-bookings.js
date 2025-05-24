// Fetch and display only the bookings for the logged-in user
window.addEventListener('DOMContentLoaded', async () => {
  const userId = localStorage.getItem('userId');
  const bookingsList = document.getElementById('myBookingsList');
  if (!userId) {
    bookingsList.textContent = 'You must be logged in to view your bookings.';
    return;
  }
  const adminUsername = 'admin user';
  const adminPassword = 'pass3';
  const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
  try {
    const res = await fetch(`http://localhost:8080/bookings/user?userId=${userId}`, {
      headers: { 'Authorization': basicAuth }
    });
    if (!res.ok) throw new Error('Failed to fetch bookings');
    const bookings = await res.json();
    if (!bookings.length) {
      bookingsList.textContent = 'No bookings found.';
      return;
    }
    bookingsList.innerHTML = bookings.map((b, idx) => {
      // Generate event image filename from event name
      const fileName = b.eventName ? b.eventName.replace(/\s+/g, '_').toLowerCase() + '.jpg' : 'default-event.jpg';
      const imgPath = 'images/' + fileName;
      return `
        <div class="booking-card" style="background: #fff; border-radius: 12px; box-shadow: 0 2px 10px rgba(99,102,241,0.08); padding: 22px 18px 18px 18px; margin-bottom: 22px; display: flex; align-items: center; gap: 18px; transition: box-shadow 0.2s;">
          <img src="${imgPath}" alt="${b.eventName}" onerror="this.onerror=null;this.src='images/default-event.jpg';" style="width: 90px; height: 90px; object-fit: cover; border-radius: 10px; box-shadow: 0 1px 4px rgba(99,102,241,0.10);">
          <div style="flex:1;">
            <h3 style="margin: 0 0 6px 0; color: #6366f1; font-size: 1.25em;">${b.eventName || 'Event'}</h3>
            <p style="margin: 2px 0 0 0; color: #374151;"><b>Participants:</b> ${b.participants}</p>
            <p style="margin: 2px 0 0 0; color: #374151;"><b>Type:</b> ${b.type}</p>
            <p style="margin: 2px 0 0 0; color: #374151;"><b>Total Price:</b> <span style='color:#10b981;font-weight:bold;'>₹${b.eventPrice !== undefined ? b.eventPrice : (b.finalPrice !== undefined ? b.finalPrice : 'N/A')}</span></p>
            <button class="view-details-btn" data-idx="${idx}" style="margin-top: 10px; background: linear-gradient(90deg, #6366f1 60%, #f472b6 100%); color: #fff; border: none; border-radius: 6px; padding: 7px 16px; font-size: 15px; cursor: pointer; transition: background 0.2s;">View Details</button>
            <button class="cancel-booking-btn" data-id="${b.id}" style="margin-top: 10px; margin-left: 10px; background: linear-gradient(90deg, #f472b6 60%, #6366f1 100%); color: #fff; border: none; border-radius: 6px; padding: 7px 16px; font-size: 15px; cursor: pointer; transition: background 0.2s;">Cancel Booking</button>
          </div>
        </div>
      `;
    }).join('');

    // Add interactivity for 'View Details' buttons
    document.querySelectorAll('.view-details-btn').forEach(btn => {
      btn.addEventListener('click', function() {
        const idx = this.getAttribute('data-idx');
        const b = bookings[idx];
        // Create and show a modal with more booking details
        const modal = document.createElement('div');
        modal.style.position = 'fixed';
        modal.style.top = '0';
        modal.style.left = '0';
        modal.style.width = '100vw';
        modal.style.height = '100vh';
        modal.style.background = 'rgba(0,0,0,0.25)';
        modal.style.display = 'flex';
        modal.style.alignItems = 'center';
        modal.style.justifyContent = 'center';
        modal.style.zIndex = '9999';
        modal.innerHTML = `
          <div style="background: #fff; border-radius: 14px; max-width: 370px; width: 90vw; padding: 32px 24px 24px 24px; box-shadow: 0 8px 32px rgba(99,102,241,0.18); position: relative; animation: popIn 0.2s;">
            <span style="position: absolute; top: 10px; right: 18px; font-size: 22px; cursor: pointer; color: #555; transition: color 0.2s;" id="closeModalBtn">&times;</span>
            <img src="images/${b.eventName ? b.eventName.replace(/\s+/g, '_').toLowerCase() + '.jpg' : 'default-event.jpg'}" alt="${b.eventName}" onerror="this.onerror=null;this.src='images/default-event.jpg';" style="width: 100%; height: 120px; object-fit: cover; border-radius: 8px; margin-bottom: 16px;">
            <h2 style="color: #6366f1; margin-bottom: 8px;">${b.eventName || 'Event'}</h2>
            <p style="margin: 6px 0;"><b>Participants:</b> ${b.participants}</p>
            <p style="margin: 6px 0;"><b>Type:</b> ${b.type}</p>
            <p style="margin: 6px 0;"><b>Total Price:</b> <span style='color:#10b981;font-weight:bold;'>₹${b.eventPrice !== undefined ? b.eventPrice : (b.finalPrice !== undefined ? b.finalPrice : 'N/A')}</span></p>
            <p style="margin: 6px 0;"><b>Booking ID:</b> ${b.id || 'N/A'}</p>
            <button id="closeModalBtn2" style="margin-top: 18px; background: linear-gradient(90deg, #f472b6 40%, #6366f1 100%); color: #fff; border: none; border-radius: 6px; padding: 8px 18px; font-size: 15px; cursor: pointer; width: 100%;">Close</button>
          </div>
        `;
        document.body.appendChild(modal);
        document.getElementById('closeModalBtn').onclick = () => modal.remove();
        document.getElementById('closeModalBtn2').onclick = () => modal.remove();
      });
    });

    // Add interactivity for 'Cancel Booking' buttons
    document.querySelectorAll('.cancel-booking-btn').forEach(btn => {
      btn.addEventListener('click', async function() {
        const bookingId = this.getAttribute('data-id');
        if (!confirm('Are you sure you want to cancel this booking?')) return;
        try {
          const adminUsername = 'admin user';
          const adminPassword = 'pass3';
          const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
          const res = await fetch(`http://localhost:8080/bookings/${bookingId}`, {
            method: 'DELETE',
            headers: { 'Authorization': basicAuth }
          });
          if (res.ok) {
            alert('Booking cancelled successfully!');
            this.closest('.booking-card').remove();
          } else {
            alert('Failed to cancel booking.');
          }
        } catch (err) {
          alert('Error cancelling booking.');
        }
      });
    });
  } catch (err) {
    bookingsList.textContent = 'Error loading bookings.';
  }
});

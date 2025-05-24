// Event Details Modal Logic
let currentEventDetails = null;

function openEventDetailsModal(event) {
  currentEventDetails = event;
  const img = document.getElementById('eventDetailsImage');
  const name = document.getElementById('eventDetailsName');
  const type = document.getElementById('eventDetailsType');
  const price = document.getElementById('eventDetailsPrice');
  const desc = document.getElementById('eventDetailsDescription');
  const bookBtn = document.getElementById('bookNowBtn');
  const modal = document.getElementById('eventDetailsModal');
  if (!img || !name || !type || !price || !desc || !bookBtn || !modal) {
    alert('Event details modal is not set up correctly.');
    return;
  }
  img.src = event.imageUrl || 'images/default-event.jpg';
  name.textContent = event.name;
  type.textContent = 'Type: ' + (event.type || 'N/A');
  price.textContent = 'Starting Price: ₹' + (event.basePrice || 'N/A');
  desc.textContent = event.description || '';
  // Show Book Now only if user is logged in
  const isLoggedIn = !!localStorage.getItem('userId');
  bookBtn.style.display = isLoggedIn ? '' : 'none';
  modal.style.display = 'flex';
  console.log('Event details modal opened for:', event.name);
}

function closeEventDetailsModal() {
  const modal = document.getElementById('eventDetailsModal');
  if (modal) modal.style.display = 'none';
}
window.openEventDetailsModal = openEventDetailsModal;
window.closeEventDetailsModal = closeEventDetailsModal;

window.addEventListener('DOMContentLoaded', () => {
  // Fetch events from backend and render them dynamically
  const eventContainer = document.querySelector('.event-card-container');
  eventContainer.innerHTML = '<p>Loading events...</p>';

  // Prepare Basic Auth header
  const username = 'admin user';
  const password = 'pass3';
  const basicAuth = 'Basic ' + btoa(username + ':' + password);

  fetch('http://localhost:8080/api/events', {
    headers: {
      'Authorization': basicAuth
    }
  })
    .then(async response => {
      if (!response.ok) {
        const text = await response.text();
        throw new Error(`HTTP ${response.status}: ${text}`);
      }
      return response.json();
    })
    .then(events => {
      if (!Array.isArray(events) || events.length === 0) {
        eventContainer.innerHTML = '<p>No events found.</p>';
        return;
      }
      eventContainer.innerHTML = '';
      events.forEach((event, index) => {
        const card = document.createElement('div');
        card.className = 'event-card';
        // Generate image filename from event name
        const fileName = event.name.replace(/\s+/g, '_').toLowerCase() + '.jpg';
        const imgPath = 'images/' + fileName;
        event.imageUrl = imgPath;
        card.innerHTML = `
          <img src="${imgPath}" alt="${event.name}" onerror="this.onerror=null;this.src='images/default-event.jpg';">
          <div class="event-info">
            <h2>${event.name}</h2>
            <p>Type: ${event.type || 'N/A'}</p>
            <p>Starting Price: ₹${event.basePrice}</p>
            <button class="view-details-btn">View Details</button>
          </div>
        `;
        card.querySelector('.view-details-btn').addEventListener('click', () => {
          window.location.href = `event-details.html?id=${event.id}`;
        });
        eventContainer.appendChild(card);
      });
    })
    .catch(err => {
      eventContainer.innerHTML = '<p style="color:red">Failed to load events.</p>';
      console.error('Error fetching events:', err.message);
    });

  // Book Now button handler (no longer needed for modal)
});

// Navbar and profile modal logic moved from home.html
function updateNavbarForAuth() {
  const isLoggedIn = !!localStorage.getItem('userId');
  document.getElementById('nav-login').style.display = isLoggedIn ? 'none' : '';
  document.getElementById('nav-signup').style.display = isLoggedIn ? 'none' : '';
  document.getElementById('nav-logout').style.display = isLoggedIn ? '' : 'none';
  document.getElementById('nav-booking').style.display = isLoggedIn ? '' : 'none';
  document.getElementById('nav-profile').style.display = isLoggedIn ? '' : 'none';
}
function logoutUser() {
  localStorage.removeItem('userId');
  alert('Logged out successfully!');
  updateNavbarForAuth();
  window.location.href = 'home.html';
}
function openProfileModal() {
  document.getElementById('profileModal').style.display = 'flex';
  loadProfile();
}
function closeProfileModal() {
  document.getElementById('profileModal').style.display = 'none';
  document.getElementById('updateProfileForm').style.display = 'none';
  document.getElementById('editProfileBtn').style.display = '';
}
async function loadProfile() {
  const userId = localStorage.getItem('userId');
  if (!userId) return;
  const adminUsername = 'admin user';
  const adminPassword = 'pass3';
  const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
  const res = await fetch(`http://localhost:8080/api/users/${userId}`, { headers: { 'Authorization': basicAuth } });
  if (res.ok) {
    const user = await res.json();
    document.getElementById('profileDetails').innerHTML =
      `<b>Name:</b> ${user.username}<br><b>Email:</b> ${user.email}<br><b>Role:</b> ${user.role}`;
    document.getElementById('profileName').value = user.username;
    document.getElementById('profileEmail').value = user.email;
    document.getElementById('profilePassword').value = '';
    // Set avatar initial
    document.getElementById('profileAvatar').textContent = user.username ? user.username[0].toUpperCase() : '👤';
  } else {
    document.getElementById('profileDetails').innerHTML = 'Failed to load profile.';
  }
}
document.getElementById('editProfileBtn').onclick = function() {
  document.getElementById('updateProfileForm').style.display = '';
  this.style.display = 'none';
};
document.getElementById('updateProfileForm').onsubmit = async function(e) {
  e.preventDefault();
  const userId = localStorage.getItem('userId');
  const adminUsername = 'admin user';
  const adminPassword = 'pass3';
  const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
  const name = document.getElementById('profileName').value;
  const email = document.getElementById('profileEmail').value;
  const password = document.getElementById('profilePassword').value;
  const body = { username: name, email: email };
  if (password) body.password = password;
  const res = await fetch(`http://localhost:8080/api/users/${userId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', 'Authorization': basicAuth },
    body: JSON.stringify(body)
  });
  if (res.ok) {
    alert('Profile updated!');
    loadProfile();
    document.getElementById('updateProfileForm').style.display = 'none';
    document.getElementById('editProfileBtn').style.display = '';
  } else {
    alert('Failed to update profile.');
  }
};
document.addEventListener('DOMContentLoaded', updateNavbarForAuth);
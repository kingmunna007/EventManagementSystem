// event-details.js
function getEventIdFromUrl() {
  const params = new URLSearchParams(window.location.search);
  return params.get('id');
}

// Map event names/types to promotional descriptions
const eventDescriptions = {
  'wedding': {
    promo: `Experience the magic of a perfect wedding with EventEase! Our expert planners turn your dreams into reality, handling every detail from grand decor to gourmet catering. With our seamless coordination and creative flair, your special day will be unforgettable for you and your guests. Trust us to deliver a celebration that’s elegant, joyful, and uniquely yours.`,
    extra: `From intimate gatherings to lavish affairs, we offer customizable packages to suit every couple’s vision and budget. Our team is dedicated to making your wedding stress-free and spectacular. Book with us and let your love story shine! <br><br><b>Special Offer:</b> Enjoy a hefty discount on your booking by using our official discount codes at checkout!`
  },
  'conference': {
    promo: `Host impactful conferences with EventEase, where professionalism meets innovation. We provide state-of-the-art venues, advanced AV setups, and flawless logistics to ensure your event runs smoothly. Our experienced team manages everything from registrations to refreshments, so you can focus on your message and your audience.`,
    extra: `Elevate your brand and inspire your attendees with our tailored conference solutions. Whether it’s a corporate summit or a tech expo, we guarantee a seamless experience that exceeds expectations. <br><br><b>Special Offer:</b> Use our discount codes for a significant price reduction on your next conference booking!`
  },
  'anniversary': {
    promo: `Celebrate milestones in style with EventEase! Our anniversary parties are crafted to reflect your journey and joy. From romantic settings to lively entertainment, we create the perfect ambiance for you and your loved ones to cherish memories and make new ones.`,
    extra: `Let us handle the details—decor, music, catering, and more—so you can focus on each other. Our creative team ensures every anniversary is as special as your first. <br><br><b>Special Offer:</b> Unlock a hefty discount with our official codes and make your celebration even sweeter!`
  },
  'birthday party': {
    promo: `Make every birthday a blast with EventEase! We specialize in fun, themed parties for all ages, complete with vibrant decor, engaging activities, and delicious treats. Our team brings your vision to life, ensuring a stress-free and memorable celebration for everyone.`,
    extra: `From kids’ parties to milestone birthdays, we handle it all with creativity and care. <br><br><b>Special Offer:</b> Use our discount codes for a fantastic price cut on your next birthday bash!`
  },
  'corporate meeting': {
    promo: `Impress your colleagues and clients with flawless corporate meetings by EventEase. We provide professional venues, top-notch amenities, and attentive service to ensure your meetings are productive and comfortable. Our team manages every detail, from setup to refreshments.`,
    extra: `Boost your business image and efficiency with our tailored meeting solutions. <br><br><b>Special Offer:</b> Book now and apply our discount codes for a generous reduction in your meeting costs!`
  },
  'cultural fest': {
    promo: `Bring communities together with vibrant cultural fests by EventEase! We celebrate diversity and creativity with immersive experiences, colorful decor, and engaging performances. Our team handles logistics, security, and entertainment, making your fest a resounding success.`,
    extra: `From food stalls to stage shows, we curate every element for maximum impact and enjoyment. <br><br><b>Special Offer:</b> Don’t miss out on our hefty discounts—use the right code at checkout!`
  },
  'product launch': {
    promo: `Launch your product in style with EventEase! We create buzzworthy events that showcase your brand and innovation. Our team delivers stunning setups, dynamic presentations, and seamless execution to ensure your launch is the talk of the town.`,
    extra: `From media invites to live demos, we handle every aspect with precision and flair. <br><br><b>Special Offer:</b> Use our discount codes for a substantial price drop on your next launch event!`
  },
  'award night': {
    promo: `Celebrate excellence with glamorous award nights by EventEase! We design red-carpet experiences, elegant venues, and memorable ceremonies that honor achievements in style. Our meticulous planning ensures a flawless, prestigious event.`,
    extra: `From stage design to guest management, we handle it all so you can focus on the celebration. <br><br><b>Special Offer:</b> Book your award night with us and enjoy a hefty discount using our official codes!`
  },
  'engagement': {
    promo: `Begin your forever with a magical engagement party by EventEase! We create romantic, joyful celebrations tailored to your love story. Our team takes care of decor, entertainment, and every special touch.`,
    extra: `Celebrate your commitment with friends and family in a setting that’s as unique as your bond. <br><br><b>Special Offer:</b> Use our discount codes for a generous price cut on your engagement celebration!`
  },
  'reunion': {
    promo: `Reconnect and relive memories with unforgettable reunions by EventEase! We organize seamless, fun-filled gatherings for families, friends, and alumni. Enjoy great food, games, and entertainment while we handle the logistics.`,
    extra: `Let us make your reunion stress-free and spectacular. <br><br><b>Special Offer:</b> Book now and use our discount codes for a hefty price reduction!`
  },
  'art exhibition': {
    promo: `Showcase creativity with stunning art exhibitions by EventEase! We provide elegant venues, professional lighting, and expert curation to highlight every masterpiece. Our team ensures a smooth, successful event for artists and guests alike.`,
    extra: `From setup to promotion, we handle every detail so you can focus on the art. <br><br><b>Special Offer:</b> Use our discount codes for a significant discount on your next exhibition!`
  },
  'concert night': {
    promo: `Turn up the volume with electrifying concert nights by EventEase! We deliver top-tier sound, lighting, and stage management for unforgettable live performances. Our team ensures a safe, energetic, and seamless experience for artists and fans.`,
    extra: `From ticketing to crowd control, we handle it all so you can enjoy the music. <br><br><b>Special Offer:</b> Book your concert with us and use our discount codes for a hefty price drop!`
  }
};

function renderEventDetails(event) {
  const container = document.getElementById('eventDetailsContainer');
  if (!event) {
    container.innerHTML = '<p style="color:red">Event not found.</p>';
    return;
  }
  const fileName = event.name.replace(/\s+/g, '_').toLowerCase() + '.jpg';
  const imgPath = 'images/' + fileName;
  // Get promotional text
  const key = (event.type || event.name || '').toLowerCase();
  const desc = eventDescriptions[key] || {};
  container.innerHTML = `
    <div class="event-details-card">
      <img src="${imgPath}" alt="${event.name}">
      <h1>${event.name}</h1>
      <div class="event-price">Base Price: ₹${event.basePrice} <span style='font-size:15px;color:#6366f1;'>(per person)</span></div>
      <div class="event-desc">${desc.promo || event.description || ''}</div>
      <div class="event-extra">${desc.extra || ''}</div>
      <button id="bookNowBtn" style="display:none;">Book Now</button>
    </div>
  `;
  // Show Book Now only if user is logged in
  const isLoggedIn = !!localStorage.getItem('userId');
  if (isLoggedIn) {
    document.getElementById('bookNowBtn').style.display = '';
    document.getElementById('bookNowBtn').onclick = function() {
      window.location.href = `booking.html?eventId=${event.id}`;
    };
  }
}

function updateNavbarForAuth() {
  const isLoggedIn = !!localStorage.getItem('userId');
  const navLogin = document.getElementById('nav-login');
  const navSignup = document.getElementById('nav-signup');
  const navLogout = document.getElementById('nav-logout');
  const navBooking = document.getElementById('nav-booking');
  if (navLogin) navLogin.style.display = isLoggedIn ? 'none' : '';
  if (navSignup) navSignup.style.display = isLoggedIn ? 'none' : '';
  if (navLogout) navLogout.style.display = isLoggedIn ? '' : 'none';
  if (navBooking) navBooking.style.display = isLoggedIn ? '' : 'none';
}

function logoutUser() {
  localStorage.removeItem('userId');
  alert('Logged out successfully!');
  updateNavbarForAuth();
  window.location.href = 'home.html';
}

// Close modal when clicking on close button or outside modal
window.addEventListener('DOMContentLoaded', () => {
  updateNavbarForAuth();
  const eventId = getEventIdFromUrl();
  if (!eventId) {
    renderEventDetails(null);
    return;
  }
  // Prepare Basic Auth header
  const username = 'admin user';
  const password = 'pass3';
  const basicAuth = 'Basic ' + btoa(username + ':' + password);
  fetch(`http://localhost:8080/api/events/${eventId}`, {
    headers: { 'Authorization': basicAuth }
  })
    .then(async response => {
      if (!response.ok) {
        renderEventDetails(null);
        return;
      }
      const event = await response.json();
      renderEventDetails(event);
    })
    .catch(() => renderEventDetails(null));
});

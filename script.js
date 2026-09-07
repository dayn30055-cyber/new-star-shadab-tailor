document.getElementById("year").textContent = new Date().getFullYear();

const menuBtn = document.getElementById("menuBtn");
const nav = document.getElementById("nav");
menuBtn.addEventListener("click", () => {
  const isOpen = nav.classList.toggle("open");
  menuBtn.setAttribute("aria-expanded", String(isOpen));
  menuBtn.textContent = isOpen ? "×" : "☰";
});
document.querySelectorAll("#nav a").forEach(a => a.addEventListener("click", () => {
  nav.classList.remove("open");
  menuBtn.setAttribute("aria-expanded", "false");
  menuBtn.textContent = "☰";
}));

const appointmentForm = document.getElementById("appointmentForm");
const appointmentDate = document.getElementById("appointmentDate");
const appointmentTime = document.getElementById("appointmentTime");
const slotStatus = document.getElementById("slotStatus");
const bookingResult = document.getElementById("bookingResult");
const bookingIdEl = document.getElementById("bookingId");
const bookingSummary = document.getElementById("bookingSummary");
const resultWhatsApp = document.getElementById("resultWhatsApp");
const googleCalendar = document.getElementById("googleCalendar");
const downloadIcs = document.getElementById("downloadIcs");

const pad = n => String(n).padStart(2, "0");
const toDateInput = d => `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
const today = new Date();
appointmentDate.min = toDateInput(today);

// Requests made in this browser are remembered so the same customer won't double-book a slot.
const STORAGE_KEY = "nsst_appointment_requests_v1";
const getBookings = () => JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
const saveBooking = booking => {
  const all = getBookings();
  all.push(booking);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(all));
};

function updateSlotStatus() {
  const date = appointmentDate.value;
  const time = appointmentTime.value;
  if (!date || !time) {
    slotStatus.textContent = "Choose date & time";
    slotStatus.className = "slot-status";
    return;
  }
  const booked = getBookings().some(b => b.date === date && b.time === time);
  slotStatus.textContent = booked ? "Already requested" : "Available";
  slotStatus.className = `slot-status ${booked ? "busy" : "available"}`;
  appointmentTime.querySelectorAll("option").forEach(o => {
    if (o.value) {
      const isBooked = getBookings().some(b => b.date === date && b.time === o.value);
      o.disabled = isBooked;
    }
  });
}
appointmentDate.addEventListener("change", updateSlotStatus);
appointmentTime.addEventListener("change", updateSlotStatus);
updateSlotStatus();

function makeBookingId() {
  const d = new Date();
  return `NSST-${d.getFullYear()}${pad(d.getMonth()+1)}${pad(d.getDate())}-${Math.random().toString(36).slice(2, 7).toUpperCase()}`;
}

function calendarParts(date, time) {
  const [clock, meridiem] = time.split(" ");
  let [h, m] = clock.split(":").map(Number);
  if (meridiem === "PM" && h !== 12) h += 12;
  if (meridiem === "AM" && h === 12) h = 0;
  const start = new Date(`${date}T${pad(h)}:${pad(m)}:00`);
  const end = new Date(start.getTime() + 60 * 60 * 1000);
  return { start, end };
}
function googleDate(d) { return d.toISOString().replace(/[-:]/g, "").replace(/\.\d{3}Z$/, "Z"); }
function escapeIcs(v) { return String(v).replace(/\\/g,"\\\\").replace(/;/g,"\\;").replace(/,/g,"\\,").replace(/\n/g,"\\n"); }

let latestBooking = null;

downloadIcs.addEventListener("click", () => {
  if (!latestBooking) return;
  const {start, end} = calendarParts(latestBooking.date, latestBooking.time);
  const ics = [
    "BEGIN:VCALENDAR","VERSION:2.0","PRODID:-//New Star Shadab Tailor//Appointment//EN","BEGIN:VEVENT",
    `UID:${latestBooking.id}@newstarshadabtailor`, `DTSTAMP:${googleDate(new Date())}`,
    `DTSTART:${googleDate(start)}`, `DTEND:${googleDate(end)}`,
    `SUMMARY:${escapeIcs("Tailoring Appointment - New Star Shadab Tailor")}`,
    `LOCATION:${escapeIcs("New Star Shadab Tailor, Sujanganj Belwar Road")}`,
    `DESCRIPTION:${escapeIcs(`Booking ID: ${latestBooking.id}\nService: ${latestBooking.service}\nCustomer: ${latestBooking.name}`)}`,
    "END:VEVENT","END:VCALENDAR"
  ].join("\r\n");
  const blob = new Blob([ics], {type:"text/calendar;charset=utf-8"});
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a"); a.href = url; a.download = `${latestBooking.id}.ics`; a.click();
  setTimeout(() => URL.revokeObjectURL(url), 1000);
});

appointmentForm.addEventListener("submit", event => {
  event.preventDefault();
  const name = document.getElementById("customerName").value.trim();
  const phone = document.getElementById("customerPhone").value.trim();
  const service = document.getElementById("service").value;
  const date = appointmentDate.value;
  const time = appointmentTime.value;
  const message = document.getElementById("appointmentMessage").value.trim();

  if (!/^[0-9]{10}$/.test(phone)) return alert("Please enter a valid 10-digit mobile number.");
  if (!date || !time) return alert("Please choose a date and time.");
  if (getBookings().some(b => b.date === date && b.time === time)) {
    alert("This slot has already been requested in this browser. Please choose another time.");
    updateSlotStatus(); return;
  }

  const booking = { id: makeBookingId(), name, phone, service, date, time, message, createdAt: new Date().toISOString() };
  saveBooking(booking); latestBooking = booking;

  const formattedDate = new Date(`${date}T00:00:00`).toLocaleDateString("en-IN", {day:"2-digit",month:"long",year:"numeric"});
  const whatsappText = `Hello New Star Shadab Tailor,\n\nI want to request an appointment.\n\nBooking ID: ${booking.id}\nName: ${name}\nMobile: ${phone}\nService: ${service}\nPreferred Date: ${formattedDate}\nPreferred Time: ${time}${message ? `\nMessage: ${message}` : ""}\n\nPlease confirm my appointment. Thank you!`;
  resultWhatsApp.href = `https://wa.me/917565053878?text=${encodeURIComponent(whatsappText)}`;

  const {start, end} = calendarParts(date, time);
  const calendarParams = new URLSearchParams({
    action:"TEMPLATE", text:"Tailoring Appointment - New Star Shadab Tailor",
    dates:`${googleDate(start)}/${googleDate(end)}`,
    details:`Booking ID: ${booking.id}\nService: ${service}\nCustomer: ${name}\nPhone: ${phone}`,
    location:"New Star Shadab Tailor, Sujanganj Belwar Road"
  });
  googleCalendar.href = `https://calendar.google.com/calendar/render?${calendarParams.toString()}`;
  bookingIdEl.textContent = booking.id;
  bookingSummary.textContent = `${service} • ${formattedDate} • ${time}. Please send the request on WhatsApp for final confirmation.`;
  bookingResult.classList.add("show");
  updateSlotStatus();
  bookingResult.scrollIntoView({behavior:"smooth", block:"center"});
  window.open(resultWhatsApp.href, "_blank", "noopener");
});

// ---------- Customer Tools: measurement, design reference and tracking ----------
(() => {
  const css = document.createElement("link");
  css.rel = "stylesheet";
  css.href = "customer-tools.css?v=1";
  document.head.appendChild(css);

  const toolsNav = document.createElement("a");
  toolsNav.href = "#customer-tools";
  toolsNav.textContent = "Customer Tools";
  const bookLink = nav.querySelector(".nav-book");
  nav.insertBefore(toolsNav, bookLink || null);
  toolsNav.addEventListener("click", () => {
    nav.classList.remove("open");
    menuBtn.setAttribute("aria-expanded", "false");
    menuBtn.textContent = "☰";
  });

  const appointmentSection = document.getElementById("appointment");
  const section = document.createElement("section");
  section.id = "customer-tools";
  section.className = "section customer-tools-section";
  section.innerHTML = `
    <div class="tools-intro">
      <p class="eyebrow">CUSTOMER STUDIO</p>
      <h2>Your Tailoring, Made Easier</h2>
      <p>Save your measurements, send a design reference and check your booking or order status from one place.</p>
    </div>
    <div class="tools-tabs" role="tablist" aria-label="Customer tools">
      <button class="tool-tab active" type="button" data-panel="measurements">📏 Measurements</button>
      <button class="tool-tab" type="button" data-panel="design">📸 Upload Design</button>
      <button class="tool-tab" type="button" data-panel="tracking">📦 Track Order</button>
    </div>
    <div class="tools-shell">
      <div class="tool-panel active" id="tool-measurements">
        <div class="tool-card">
          <div class="tool-card-head"><div><h3>Measurement Profile</h3><p>Save your tailoring measurements on this device and send them to the shop on WhatsApp whenever you need.</p></div><div class="tool-icon">📏</div></div>
          <div class="tool-grid">
            <label class="tool-field">Customer Name<input id="measureName" type="text" placeholder="Your name"></label>
            <label class="tool-field">Mobile Number<input id="measurePhone" type="tel" inputmode="numeric" maxlength="10" placeholder="10-digit mobile number"></label>
            <label class="tool-field full">Garment Type<select id="measureGarment"><option>Shirt</option><option>Pant</option><option>Suit / Blazer</option><option>Kurta</option><option>Sherwani</option><option>School Uniform</option></select></label>
          </div>
          <div class="measure-grid" style="margin-top:18px">
            ${["Chest","Waist","Hip","Shoulder","Sleeve","Shirt Length","Pant Length","Neck"].map((m,i)=>`<label class="tool-field">${m}<span class="measure-unit">INCHES</span><input class="measure-value" data-label="${m}" type="number" min="0" max="100" step="0.25" inputmode="decimal" placeholder="${[38,32,38,17,24,29,40,15][i]}"></label>`).join("")}
          </div>
          <label class="tool-field" style="margin-top:17px">Special Notes<textarea id="measureNotes" rows="3" placeholder="Slim fit, loose fit, preferred style, etc."></textarea></label>
          <div class="tool-actions"><button id="saveMeasurements" class="tool-action primary" type="button">💾 Save Measurements</button><button id="sendMeasurements" class="tool-action secondary" type="button">💬 Send on WhatsApp</button><button id="clearMeasurements" class="tool-action secondary" type="button">Clear</button></div>
          <p class="tool-note">Measurements are stored only in this browser until you send them to the shop.</p>
          <div id="measureSuccess" class="tool-success">✓ Measurement profile saved on this device.</div>
        </div>
      </div>
      <div class="tool-panel" id="tool-design">
        <div class="tool-card">
          <div class="tool-card-head"><div><h3>Upload Your Design</h3><p>Select a reference image of the shirt, blazer, kurta, sherwani or other style you want stitched.</p></div><div class="tool-icon">📸</div></div>
          <label class="upload-zone" id="designDropZone"><input id="designFile" type="file" accept="image/jpeg,image/png,image/webp"><div style="font-size:32px">＋</div><strong>Choose a reference image</strong><span>JPG, PNG or WEBP • Image stays on your device until you share it</span></label>
          <div id="designPreview" class="design-preview"><img id="designPreviewImg" alt="Selected design preview"><div class="design-meta"><strong id="designFileName"></strong><span id="designFileInfo"></span><div class="tool-grid" style="margin-top:16px"><label class="tool-field">Garment<select id="designGarment"><option>Shirt</option><option>Pant</option><option>Suit</option><option>Blazer</option><option>Kurta</option><option>Sherwani</option><option>School Uniform</option><option>Other</option></select></label><label class="tool-field">Your Name<input id="designName" type="text" placeholder="Your name"></label><label class="tool-field full">Instructions<textarea id="designNotes" rows="3" placeholder="What do you like about this design?"></textarea></label></div></div></div>
          <div class="tool-actions"><button id="shareDesign" class="tool-action primary" type="button">📲 Share Design</button><button id="designWhatsApp" class="tool-action secondary" type="button">💬 Send Details on WhatsApp</button></div>
          <p class="tool-note">On supported phones, “Share Design” can share the selected image directly. Otherwise WhatsApp opens with the details and you can attach the image there.</p>
        </div>
      </div>
      <div class="tool-panel" id="tool-tracking">
        <div class="tool-card">
          <div class="tool-card-head"><div><h3>Order & Booking Tracking</h3><p>Enter your New Star Shadab Tailor booking ID. Booking requests created on this device can be checked instantly.</p></div><div class="tool-icon">📦</div></div>
          <div class="track-search"><input id="trackId" type="text" autocomplete="off" placeholder="Example: NSST-20260907-ABCDE"><button id="trackButton" class="tool-action primary" type="button">Track</button></div>
          <div id="trackResult" class="track-result"></div>
          <div class="track-help">For stitching progress such as Cutting, Stitching, Finishing or Ready for Pickup, the shop must confirm/update the order status. If your ID is not found here, contact the shop on WhatsApp.</div>
        </div>
      </div>
    </div>
    <div class="tools-book-strip"><div><strong>Need a fitting or consultation?</strong><span>Choose a convenient time and send an appointment request.</span></div><a class="tool-action primary" href="#appointment">📅 Book Appointment</a></div>
  `;
  appointmentSection.parentNode.insertBefore(section, appointmentSection);

  const tabs = section.querySelectorAll(".tool-tab");
  const panels = section.querySelectorAll(".tool-panel");
  tabs.forEach(tab => tab.addEventListener("click", () => {
    tabs.forEach(t => t.classList.toggle("active", t === tab));
    panels.forEach(p => p.classList.toggle("active", p.id === `tool-${tab.dataset.panel}`));
  }));

  const MEASURE_KEY = "nsst_measurement_profile_v1";
  const measureName = document.getElementById("measureName");
  const measurePhone = document.getElementById("measurePhone");
  const measureGarment = document.getElementById("measureGarment");
  const measureNotes = document.getElementById("measureNotes");
  const measureInputs = [...document.querySelectorAll(".measure-value")];
  const measureSuccess = document.getElementById("measureSuccess");

  function collectMeasurements() {
    const values = {};
    measureInputs.forEach(i => { if (i.value) values[i.dataset.label] = i.value; });
    return {name:measureName.value.trim(),phone:measurePhone.value.trim(),garment:measureGarment.value,values,notes:measureNotes.value.trim(),savedAt:new Date().toISOString()};
  }
  function fillMeasurements(data) {
    if (!data) return;
    measureName.value = data.name || ""; measurePhone.value = data.phone || ""; measureGarment.value = data.garment || "Shirt"; measureNotes.value = data.notes || "";
    measureInputs.forEach(i => i.value = data.values?.[i.dataset.label] || "");
  }
  try { fillMeasurements(JSON.parse(localStorage.getItem(MEASURE_KEY) || "null")); } catch(e) {}

  document.getElementById("saveMeasurements").addEventListener("click", () => {
    const data = collectMeasurements();
    if (data.phone && !/^[0-9]{10}$/.test(data.phone)) return alert("Please enter a valid 10-digit mobile number.");
    localStorage.setItem(MEASURE_KEY, JSON.stringify(data));
    measureSuccess.classList.add("show");
    setTimeout(()=>measureSuccess.classList.remove("show"),3200);
  });
  document.getElementById("clearMeasurements").addEventListener("click", () => {
    localStorage.removeItem(MEASURE_KEY); fillMeasurements({}); measureSuccess.classList.remove("show");
  });
  document.getElementById("sendMeasurements").addEventListener("click", () => {
    const d = collectMeasurements();
    if (!d.name) return alert("Please enter your name first.");
    const lines = Object.entries(d.values).map(([k,v])=>`${k}: ${v} in`).join("\n");
    const text = `Hello New Star Shadab Tailor,\n\nMy measurement profile:\nName: ${d.name}${d.phone?`\nMobile: ${d.phone}`:""}\nGarment: ${d.garment}\n\n${lines || "Measurements: I will provide at the shop."}${d.notes?`\n\nNotes: ${d.notes}`:""}\n\nPlease confirm these measurements before stitching.`;
    window.open(`https://wa.me/917565053878?text=${encodeURIComponent(text)}`,"_blank","noopener");
  });

  const designFile = document.getElementById("designFile");
  const designDropZone = document.getElementById("designDropZone");
  const designPreview = document.getElementById("designPreview");
  const designPreviewImg = document.getElementById("designPreviewImg");
  const designFileName = document.getElementById("designFileName");
  const designFileInfo = document.getElementById("designFileInfo");
  let selectedDesignFile = null;
  function setDesignFile(file) {
    if (!file) return;
    if (!/^image\/(jpeg|png|webp)$/.test(file.type)) return alert("Please choose a JPG, PNG or WEBP image.");
    if (file.size > 12 * 1024 * 1024) return alert("Please choose an image smaller than 12 MB.");
    selectedDesignFile = file;
    designFileName.textContent = file.name;
    designFileInfo.textContent = `${(file.size/1024/1024).toFixed(2)} MB • ${file.type.replace("image/","").toUpperCase()}`;
    designPreviewImg.src = URL.createObjectURL(file);
    designPreview.classList.add("show");
  }
  designFile.addEventListener("change", e => setDesignFile(e.target.files[0]));
  ["dragenter","dragover"].forEach(evt => designDropZone.addEventListener(evt,e=>{e.preventDefault();designDropZone.classList.add("drag")}));
  ["dragleave","drop"].forEach(evt => designDropZone.addEventListener(evt,e=>{e.preventDefault();designDropZone.classList.remove("drag")}));
  designDropZone.addEventListener("drop", e => setDesignFile(e.dataTransfer.files[0]));
  function designMessage() {
    const name = document.getElementById("designName").value.trim();
    const garment = document.getElementById("designGarment").value;
    const notes = document.getElementById("designNotes").value.trim();
    return `Hello New Star Shadab Tailor,\n\nI want stitching based on a reference design.\nName: ${name || "Customer"}\nGarment: ${garment}${notes?`\nInstructions: ${notes}`:""}\n\nI have selected a reference image and will attach/share it with this message.`;
  }
  document.getElementById("designWhatsApp").addEventListener("click", () => window.open(`https://wa.me/917565053878?text=${encodeURIComponent(designMessage())}`,"_blank","noopener"));
  document.getElementById("shareDesign").addEventListener("click", async () => {
    if (!selectedDesignFile) return alert("Please choose a design image first.");
    try {
      if (navigator.canShare && navigator.canShare({files:[selectedDesignFile]})) {
        await navigator.share({title:"Tailoring Design - New Star Shadab Tailor",text:designMessage(),files:[selectedDesignFile]});
      } else {
        window.open(`https://wa.me/917565053878?text=${encodeURIComponent(designMessage())}`,"_blank","noopener");
      }
    } catch(e) { if (e.name !== "AbortError") window.open(`https://wa.me/917565053878?text=${encodeURIComponent(designMessage())}`,"_blank","noopener"); }
  });

  const trackId = document.getElementById("trackId");
  const trackResult = document.getElementById("trackResult");
  function showTracking() {
    const id = trackId.value.trim().toUpperCase();
    if (!id) return alert("Please enter a booking or order ID.");
    const booking = getBookings().find(b => String(b.id).toUpperCase() === id);
    if (booking) {
      const date = new Date(`${booking.date}T00:00:00`).toLocaleDateString("en-IN",{day:"2-digit",month:"short",year:"numeric"});
      trackResult.innerHTML = `<h4>${booking.id}</h4><p>${booking.service} • ${date} • ${booking.time}</p><div class="track-steps"><div class="track-step done">Request Created</div><div class="track-step">Confirmed</div><div class="track-step">Cutting / Stitching</div><div class="track-step">Finishing</div><div class="track-step">Ready</div></div><div class="tool-actions"><a class="tool-action secondary" target="_blank" rel="noopener" href="https://wa.me/917565053878?text=${encodeURIComponent(`Hello New Star Shadab Tailor, please update me about booking/order ${booking.id}.`)}">💬 Ask for Latest Status</a></div>`;
    } else {
      trackResult.innerHTML = `<h4>Order not found on this device</h4><p>This browser does not have a matching booking record. If the shop gave you this ID separately, ask for the latest status on WhatsApp.</p><div class="tool-actions"><a class="tool-action primary" target="_blank" rel="noopener" href="https://wa.me/917565053878?text=${encodeURIComponent(`Hello New Star Shadab Tailor, please check the status of my order/booking ID: ${id}`)}">💬 Check on WhatsApp</a></div>`;
    }
    trackResult.classList.add("show");
  }
  document.getElementById("trackButton").addEventListener("click", showTracking);
  trackId.addEventListener("keydown", e => { if (e.key === "Enter") showTracking(); });
})();
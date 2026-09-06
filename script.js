document.getElementById("year").textContent = new Date().getFullYear();

const menuBtn = document.getElementById("menuBtn");
const nav = document.getElementById("nav");
menuBtn.addEventListener("click", () => nav.classList.toggle("open"));
document.querySelectorAll("#nav a").forEach(a => a.addEventListener("click", () => nav.classList.remove("open")));

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
    `DESCRIPTION:${escapeIcs(`Booking ID: ${latestBooking.id}\\nService: ${latestBooking.service}\\nCustomer: ${latestBooking.name}`)}`,
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

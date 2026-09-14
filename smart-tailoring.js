(function(){
  var host=document.getElementById("appointment");
  if(!host||document.getElementById("smart-tailoring")) return;

  var prices={Shirt:450,Pant:550,Kunrta:600,Suit:2500,Blazer:1800,Sherwani:3500,"School Uniform":650,Alteration:200};
  prices.Kurta=prices.Kunrta; delete prices.Kunrta;
  var styles=["Classic Fit","Slim Fit","Relaxed Fit","Wedding Premium","Traditional","Custom Reference"];

  function options(arr){return arr.map(function(x){return "<option>"+x+"</option>";}).join("");}
  function estimate(g,q){return (prices[g]||0)*Math.max(1,parseInt(q||1,10));}

  var section=document.createElement("section");
  section.id="smart-tailoring";
  section.className="smart-tailoring-section";
  section.innerHTML=
    '<div class="smart-tailoring-inner">'+
      '<div class="smart-tailoring-head"><span class="smart-badge">NEW CUSTOMER FEATURES</span><h2>Plan Your Stitching Online</h2><p>Check an estimated stitching price, choose a style and send your request on WhatsApp.</p></div>'+
      '<div class="smart-tabs"><button class="smart-tab active" data-tab="price">₹ Price Calculator</button><button class="smart-tab" data-tab="styles">👔 Style Catalogue</button><button class="smart-tab" data-tab="order">📲 Quick Order</button></div>'+
      '<div class="smart-panel active" id="smart-price"><div class="smart-card"><h3>Stitching Price Calculator</h3><p class="sub">Indicative stitching estimate before final shop confirmation.</p><div class="calc-grid"><label class="smart-field">Garment<select id="calcGarment">'+options(Object.keys(prices))+'</select></label><label class="smart-field">Quantity<input id="calcQty" type="number" min="1" max="20" value="1"></label><label class="smart-field">Preferred Style<select id="calcStyle">'+options(styles)+'</select></label></div><div class="price-box"><div><span>Estimated stitching total</span><strong id="calcTotal">₹450</strong></div><button class="smart-btn gold" id="priceToOrder" type="button">Use in Quick Order →</button></div><p class="price-note">Final price may vary for fabric, lining, embroidery, complex alterations or special customization.</p></div></div>'+
      '<div class="smart-panel" id="smart-styles"><div class="smart-card"><h3>Style Catalogue</h3><p class="sub">Choose the fitting/style you prefer.</p><div class="style-grid">'+styles.map(function(s,i){return '<button class="style-option'+(i===0?' selected':'')+'" data-style="'+s+'" type="button"><span class="style-emoji">'+(["👔","✨","🧵","🤵","🪡","📸"][i])+'</span><strong>'+s+'</strong><small>'+(["Balanced everyday fit","Modern close fit","Comfort-first cut","Special occasion finish","Classic ethnic finish","Use your own reference image"][i])+'</small></button>';}).join("")+'</div><div class="order-summary">Selected style: <strong id="chosenStyle">Classic Fit</strong></div><div class="smart-actions"><button class="smart-btn gold" id="styleToOrder" type="button">Continue to Quick Order →</button><a class="smart-btn dark" href="#customer-tools">Upload Design</a></div></div></div>'+
      '<div class="smart-panel" id="smart-order"><div class="smart-card"><h3>WhatsApp Quick Order</h3><p class="sub">Prepare a complete stitching request in seconds.</p><div class="quick-order-grid"><label class="smart-field">Your Name<input id="qoName" type="text"></label><label class="smart-field">Mobile Number<input id="qoPhone" type="tel" maxlength="10"></label><label class="smart-field">Garment<select id="qoGarment">'+options(Object.keys(prices))+'</select></label><label class="smart-field">Style<select id="qoStyle">'+options(styles)+'</select></label><label class="smart-field">Quantity<input id="qoQty" type="number" min="1" max="20" value="1"></label><label class="smart-field full">Notes<textarea id="qoNotes" rows="4" placeholder="Fitting, collar, colour, occasion or other request"></textarea></label></div><div class="order-summary" id="qoSummary">Estimated stitching: <strong>₹450</strong></div><div class="smart-actions"><button class="smart-btn gold" id="sendQuickOrder" type="button">📲 Send on WhatsApp</button><a class="smart-btn dark" href="#appointment">📅 Book Fitting</a><a class="smart-btn dark" href="#customer-tools">📦 Track Booking</a></div></div></div>'+
    '</div>';

  host.parentNode.insertBefore(section,host);

  var tabs=section.querySelectorAll(".smart-tab");
  var panels=section.querySelectorAll(".smart-panel");
  function show(name){
    tabs.forEach(function(t){t.classList.toggle("active",t.getAttribute("data-tab")===name);});
    panels.forEach(function(p){p.classList.toggle("active",p.id==="smart-"+name);});
  }
  tabs.forEach(function(t){t.addEventListener("click",function(){show(t.getAttribute("data-tab"));});});

  var cg=document.getElementById("calcGarment"), cq=document.getElementById("calcQty"), cs=document.getElementById("calcStyle");
  function updateCalc(){document.getElementById("calcTotal").textContent="₹"+estimate(cg.value,cq.value).toLocaleString("en-IN");}
  [cg,cq,cs].forEach(function(el){el.addEventListener("input",updateCalc);});

  section.querySelectorAll(".style-option").forEach(function(btn){
    btn.addEventListener("click",function(){
      section.querySelectorAll(".style-option").forEach(function(x){x.classList.remove("selected");});
      btn.classList.add("selected");
      document.getElementById("chosenStyle").textContent=btn.getAttribute("data-style");
      cs.value=btn.getAttribute("data-style");
    });
  });

  var qg=document.getElementById("qoGarment"), qq=document.getElementById("qoQty"), qs=document.getElementById("qoStyle");
  function updateOrder(){document.getElementById("qoSummary").innerHTML='Estimated stitching: <strong>₹'+estimate(qg.value,qq.value).toLocaleString("en-IN")+"</strong>";}
  [qg,qq].forEach(function(el){el.addEventListener("input",updateOrder);});

  document.getElementById("priceToOrder").addEventListener("click",function(){qg.value=cg.value;qq.value=cq.value;qs.value=cs.value;updateOrder();show("order");});
  document.getElementById("styleToOrder").addEventListener("click",function(){qs.value=document.getElementById("chosenStyle").textContent;show("order");});

  document.getElementById("sendQuickOrder").addEventListener("click",function(){
    var name=document.getElementById("qoName").value.trim();
    var phone=document.getElementById("qoPhone").value.trim();
    var notes=document.getElementById("qoNotes").value.trim();
    if(!name){alert("Please enter your name.");return;}
    if(phone && !/^[0-9]{10}$/.test(phone)){alert("Please enter a valid 10-digit mobile number.");return;}
    var total=estimate(qg.value,qq.value);
    var msg="Hello New Star Shadab Tailor,%0A%0AI want to place a stitching request.%0AName: "+encodeURIComponent(name)+(phone?"%0AMobile: "+phone:"")+"%0AGarment: "+encodeURIComponent(qg.value)+"%0AStyle: "+encodeURIComponent(qs.value)+"%0AQuantity: "+qq.value+"%0AEstimated stitching: ₹"+total.toLocaleString("en-IN")+(notes?"%0ANotes: "+encodeURIComponent(notes):"")+"%0A%0APlease confirm final price, fitting and delivery date.";
    location.href="https://wa.me/917565053878?text="+msg;
  });

  var nav=document.getElementById("nav");
  if(nav && !nav.querySelector('a[href="#smart-tailoring"]')){
    var link=document.createElement("a");
    link.href="#smart-tailoring";
    link.textContent="Price & Styles";
    nav.insertBefore(link,nav.querySelector('a[href="#appointment"]'));
  }
})();
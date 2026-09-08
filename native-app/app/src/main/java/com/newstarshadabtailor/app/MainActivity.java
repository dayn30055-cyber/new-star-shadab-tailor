package com.newstarshadabtailor.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private LinearLayout root, content, nav;
    private SharedPreferences prefs;
    private final int BG=Color.rgb(8,12,18), CARD=Color.rgb(18,24,32), CARD2=Color.rgb(24,31,41), GOLD=Color.rgb(212,175,55), TEXT=Color.rgb(245,247,250), MUTED=Color.rgb(158,168,181);
    private final String PHONE="917565053878";

    @Override public void onCreate(Bundle b){
        super.onCreate(b); prefs=getSharedPreferences("nsst",MODE_PRIVATE);
        getWindow().setStatusBarColor(BG); getWindow().setNavigationBarColor(BG);
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true);
        content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(18),dp(18),dp(18),dp(28));
        scroll.addView(content,new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scroll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1));
        nav=new LinearLayout(this); nav.setOrientation(LinearLayout.HORIZONTAL); nav.setPadding(dp(7),dp(6),dp(7),dp(8)); nav.setBackgroundColor(Color.rgb(13,18,25));
        root.addView(nav,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        addNav("Home","home"); addNav("Book","book"); addNav("Orders","orders"); addNav("Measure","measure"); addNav("More","more");
        setContentView(root); showHome();
    }

    private void addNav(String label,String page){
        Button x=new Button(this); x.setText(label); x.setTextSize(11); x.setAllCaps(false); x.setTextColor(TEXT); x.setTypeface(Typeface.DEFAULT_BOLD); x.setBackgroundColor(Color.TRANSPARENT); x.setPadding(dp(2),dp(8),dp(2),dp(8));
        x.setOnClickListener(v->{ if(page.equals("home"))showHome(); else if(page.equals("book"))showBooking(); else if(page.equals("orders"))showOrders(); else if(page.equals("measure"))showMeasurements(); else showMore(); });
        nav.addView(x,new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1));
    }

    private void clear(){ content.removeAllViews(); }
    private void top(String title,String sub){ TextView m=text("NSST  •  NEW STAR SHADAB TAILOR",11,true,GOLD); m.setLetterSpacing(.12f); content.addView(m,margin(0,0,0,7)); content.addView(text(title,27,true,TEXT),match()); TextView s=text(sub,13,false,MUTED); s.setPadding(0,dp(4),0,dp(18)); content.addView(s,match()); }

    private void showHome(){
        clear(); top("Welcome to NSST","Your premium tailoring dashboard.");
        LinearLayout hero=card(22); TextView tag=text("YOUR PERSONAL TAILORING HUB",11,true,GOLD); tag.setLetterSpacing(.14f); hero.addView(tag,match());
        TextView h=text("Perfect fit.\nBetter experience.",28,true,TEXT); h.setPadding(0,dp(8),0,dp(8)); hero.addView(h,match());
        hero.addView(text("Book stitching, save measurements, manage order history and contact the shop from one app.",14,false,MUTED),margin(0,0,0,15));
        Button book=button("Book New Stitching",true); book.setOnClickListener(v->showBooking()); hero.addView(book,match()); content.addView(hero,margin(0,0,0,18));
        section("Quick Actions","Fast access to the most useful NSST tools.");
        LinearLayout a=new LinearLayout(this); a.setOrientation(LinearLayout.HORIZONTAL); a.addView(quick("My Orders","Order history",v->showOrders()),weight(1,0,0,6,0)); a.addView(quick("Price List","View services",v->showPriceList()),weight(1,6,0,0,0)); content.addView(a,margin(0,0,0,12));
        LinearLayout b=new LinearLayout(this); b.setOrientation(LinearLayout.HORIZONTAL); b.addView(quick("Gallery","Browse styles",v->showGallery()),weight(1,0,0,6,0)); b.addView(quick("Measurements","Saved sizes",v->showMeasurements()),weight(1,6,0,0,0)); content.addView(b,margin(0,0,0,12));
        LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.HORIZONTAL); c.addView(quick("WhatsApp","Chat now",v->openWhatsApp("Assalamualaikum, mujhe NSST tailoring service ke baare me jankari chahiye.")),weight(1,0,0,6,0)); c.addView(quick("Call","Talk to shop",v->startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:+"+PHONE)))),weight(1,6,0,0,0)); content.addView(c,margin(0,0,0,20));
        section("Popular Services","Craftsmanship for every occasion."); service("Shirt Stitching","Clean collar, balanced shoulder and premium fitting."); service("Pant Stitching","Sharp fall and tailored comfort."); service("Suit & Blazer","Structured premium occasion tailoring."); service("Kurta & Traditional","Elegant festive and formal fitting."); service("Alteration","Accurate fitting corrections and finishing.");
    }

    private void showBooking(){
        clear(); top("Book Stitching","Create a booking and send it directly to NSST on WhatsApp.");
        EditText name=input("Your name",InputType.TYPE_CLASS_TEXT), phone=input("Phone number",InputType.TYPE_CLASS_PHONE), service=input("Service — Shirt, Pant, Suit, Kurta...",InputType.TYPE_CLASS_TEXT), date=input("Preferred delivery date",InputType.TYPE_CLASS_TEXT), note=input("Notes / fitting preference",InputType.TYPE_CLASS_TEXT);
        name.setText(prefs.getString("profile_name","")); phone.setText(prefs.getString("profile_phone",""));
        content.addView(name,field()); content.addView(phone,field()); content.addView(service,field()); content.addView(date,field()); content.addView(note,margin(0,0,0,14));
        Button send=button("Save & Send Booking on WhatsApp",true); send.setOnClickListener(v->{ String n=name.getText().toString().trim(),p=phone.getText().toString().trim(),s=service.getText().toString().trim(),d=date.getText().toString().trim(),no=note.getText().toString().trim(); if(n.isEmpty()||p.isEmpty()||s.isEmpty()){Toast.makeText(this,"Please fill name, phone and service.",Toast.LENGTH_SHORT).show();return;} addOrder(s,d,"Booking Requested"); String msg="Assalamualaikum NSST, mujhe stitching booking karni hai.\n\nName: "+n+"\nPhone: "+p+"\nService: "+s+"\nPreferred Delivery: "+d+"\nNotes: "+no; openWhatsApp(msg); }); content.addView(send,match());
    }

    private void addOrder(String service,String delivery,String status){
        String safeS=cleanPart(service), safeD=cleanPart(delivery), safeSt=cleanPart(status); String created=new SimpleDateFormat("dd MMM yyyy, hh:mm a",Locale.getDefault()).format(new Date()); String row=safeS+"~"+safeD+"~"+safeSt+"~"+created; String old=prefs.getString("orders",""); String next=row+(old.isEmpty()?"":"||"+old); String[] arr=next.split("\\|\\|"); if(arr.length>10){ StringBuilder sb=new StringBuilder(); for(int i=0;i<10;i++){ if(i>0)sb.append("||"); sb.append(arr[i]); } next=sb.toString(); } prefs.edit().putString("orders",next).apply();
    }
    private String cleanPart(String s){ return s.replace("~","-").replace("||","-"); }

    private void showOrders(){
        clear(); top("My Orders","Your saved NSST booking history on this phone."); String raw=prefs.getString("orders","");
        if(raw.isEmpty()){ LinearLayout e=card(20); e.addView(text("No orders yet",20,true,TEXT),match()); TextView t=text("Create your first stitching booking to start order tracking.",13,false,MUTED); t.setPadding(0,dp(6),0,dp(12)); e.addView(t,match()); Button b=button("Book Now",true); b.setOnClickListener(v->showBooking()); e.addView(b,match()); content.addView(e,match()); return; }
        String[] orders=raw.split("\\|\\|"); for(int i=0;i<orders.length;i++){ String[] f=orders[i].split("~",-1); String s=f.length>0?f[0]:"Order", d=f.length>1?f[1]:"", st=f.length>2?f[2]:"Booking Requested", created=f.length>3?f[3]:""; LinearLayout c=card(18); c.addView(text("ORDER "+(i+1),10,true,GOLD),match()); TextView title=text(s,20,true,TEXT); title.setPadding(0,dp(7),0,dp(7)); c.addView(title,match()); c.addView(label("Status",st),match()); c.addView(label("Delivery",d.isEmpty()?"Not specified":d),match()); c.addView(label("Booked",created),match()); Button ask=button("Ask Status on WhatsApp",false); ask.setOnClickListener(v->openWhatsApp("Assalamualaikum NSST, meri order status check karni hai. Service: "+s)); c.addView(ask,margin(0,dp(10),0,0)); content.addView(c,margin(0,0,0,12)); }
    }

    private void showMeasurements(){
        clear(); top("Measurements","Save key measurements for quicker future bookings."); String[] keys={"m_chest","m_waist","m_shoulder","m_sleeve","m_shirt","m_pant","m_hip","m_inseam"}; String[] hints={"Chest","Waist","Shoulder","Sleeve","Shirt length","Pant length","Hip","Inseam"}; EditText[] fields=new EditText[keys.length];
        for(int i=0;i<keys.length;i++){ fields[i]=input(hints[i],InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); fields[i].setText(prefs.getString(keys[i],"")); content.addView(fields[i],i==keys.length-1?margin(0,0,0,14):field()); }
        Button save=button("Save Measurements",true); save.setOnClickListener(v->{ SharedPreferences.Editor e=prefs.edit(); for(int i=0;i<keys.length;i++)e.putString(keys[i],fields[i].getText().toString()); e.apply(); Toast.makeText(this,"Measurements saved.",Toast.LENGTH_SHORT).show(); }); content.addView(save,match());
    }

    private void showMore(){ clear(); top("More","Profile, pricing, gallery and shop tools."); content.addView(quick("My Profile","Save customer details",v->showProfile()),margin(0,0,0,10)); content.addView(quick("Price List","Service pricing overview",v->showPriceList()),margin(0,0,0,10)); content.addView(quick("Design Gallery","Browse tailoring categories",v->showGallery()),margin(0,0,0,10)); content.addView(quick("Shop / Admin","Owner tools preview",v->showAdmin()),match()); }

    private void showProfile(){ clear(); top("My Profile","Save details for faster booking."); EditText name=input("Full name",InputType.TYPE_CLASS_TEXT), phone=input("Phone number",InputType.TYPE_CLASS_PHONE), city=input("City / Area",InputType.TYPE_CLASS_TEXT); name.setText(prefs.getString("profile_name","")); phone.setText(prefs.getString("profile_phone","")); city.setText(prefs.getString("profile_city","")); content.addView(name,field()); content.addView(phone,field()); content.addView(city,margin(0,0,0,14)); Button save=button("Save Profile",true); save.setOnClickListener(v->{prefs.edit().putString("profile_name",name.getText().toString().trim()).putString("profile_phone",phone.getText().toString().trim()).putString("profile_city",city.getText().toString().trim()).apply();Toast.makeText(this,"Profile saved.",Toast.LENGTH_SHORT).show();}); content.addView(save,margin(0,0,0,12)); Button wa=button("Contact NSST on WhatsApp",false); wa.setOnClickListener(v->openWhatsApp("Assalamualaikum NSST")); content.addView(wa,margin(0,0,0,10)); Button call=button("Call NSST",false); call.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:+"+PHONE)))); content.addView(call,match()); }

    private void showPriceList(){ clear(); top("Price List","Current rates can be confirmed directly with NSST."); price("Shirt Stitching","Price on request"); price("Pant Stitching","Price on request"); price("Suit & Blazer","Price on request"); price("Kurta & Traditional","Price on request"); price("Alteration","Price on request"); Button ask=button("Ask Latest Prices on WhatsApp",true); ask.setOnClickListener(v->openWhatsApp("Assalamualaikum NSST, mujhe latest stitching price list chahiye.")); content.addView(ask,margin(0,10,0,0)); }
    private void price(String name,String value){ LinearLayout c=card(16); LinearLayout r=new LinearLayout(this); r.setOrientation(LinearLayout.HORIZONTAL); TextView a=text(name,15,true,TEXT), b=text(value,13,true,GOLD); r.addView(a,new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1)); r.addView(b,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)); c.addView(r,match()); content.addView(c,margin(0,0,0,10)); }

    private void showGallery(){ clear(); top("Design Gallery","Browse tailoring categories and choose your next look."); gallery("Premium Shirts","Formal • Casual • Slim Fit"); gallery("Tailored Trousers","Classic • Modern • Comfort Fit"); gallery("Suit & Blazer","Wedding • Formal • Occasion"); gallery("Kurta & Traditional","Festive • Classic • Elegant"); gallery("Alteration & Refit","Resize • Length • Finishing"); Button share=button("Ask for Design Photos on WhatsApp",true); share.setOnClickListener(v->openWhatsApp("Assalamualaikum NSST, mujhe latest design photos dekhni hain.")); content.addView(share,margin(0,10,0,0)); }
    private void gallery(String title,String sub){ LinearLayout c=card(18); TextView k=text("NSST COLLECTION",10,true,GOLD); k.setLetterSpacing(.12f); c.addView(k,match()); TextView t=text(title,18,true,TEXT); t.setPadding(0,dp(6),0,dp(4)); c.addView(t,match()); c.addView(text(sub,12,false,MUTED),match()); content.addView(c,margin(0,0,0,10)); }

    private void showAdmin(){ clear(); top("Shop / Admin","Owner-side tools preview for NSST."); adminCard("Orders","View customer bookings saved on this device"); adminCard("Customers","Customer database will be added with cloud sync"); adminCard("Measurements","Manage customer measurements in the future admin system"); adminCard("Order Status","Next cloud version can update Cutting, Stitching, Trial, Ready and Delivered status"); LinearLayout note=card(18); note.addView(text("CLOUD ADMIN NEXT",11,true,GOLD),match()); TextView t=text("Real admin login, live customer database and notifications need a secure online backend. This V3 keeps customer data locally on the phone.",13,false,MUTED); t.setPadding(0,dp(7),0,0); note.addView(t,match()); content.addView(note,margin(0,6,0,0)); }
    private void adminCard(String title,String sub){ LinearLayout c=card(16); c.addView(text(title,16,true,TEXT),match()); TextView t=text(sub,12,false,MUTED); t.setPadding(0,dp(4),0,0); c.addView(t,match()); content.addView(c,margin(0,0,0,10)); }

    private LinearLayout quick(String title,String sub,View.OnClickListener l){ LinearLayout c=card(15); c.setOnClickListener(l); c.setClickable(true); c.addView(text(title,16,true,TEXT),match()); TextView s=text(sub,12,false,MUTED); s.setPadding(0,dp(4),0,0); c.addView(s,match()); return c; }
    private TextView label(String a,String b){ TextView t=text(a+"  •  "+b,12,false,MUTED); t.setPadding(0,dp(3),0,dp(3)); return t; }
    private void openWhatsApp(String m){ startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/"+PHONE+"?text="+Uri.encode(m)))); }
    private void section(String t,String s){ content.addView(text(t,21,true,TEXT),match()); TextView x=text(s,13,false,MUTED); x.setPadding(0,dp(4),0,dp(12)); content.addView(x,match()); }
    private void service(String t,String d){ LinearLayout c=card(16); c.addView(text(t,16,true,TEXT),match()); TextView x=text(d,12,false,MUTED); x.setPadding(0,dp(5),0,0); c.addView(x,match()); content.addView(c,margin(0,0,0,10)); }
    private LinearLayout card(int p){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(p),dp(p),dp(p),dp(p)); GradientDrawable g=new GradientDrawable(); g.setColor(CARD); g.setCornerRadius(dp(20)); g.setStroke(dp(1),Color.rgb(39,48,61)); c.setBackground(g); c.setElevation(dp(2)); return c; }
    private EditText input(String h,int type){ EditText e=new EditText(this); e.setHint(h); e.setHintTextColor(Color.rgb(112,124,140)); e.setTextColor(TEXT); e.setTextSize(14); e.setSingleLine(true); e.setInputType(type); e.setPadding(dp(16),dp(13),dp(16),dp(13)); GradientDrawable g=new GradientDrawable(); g.setColor(CARD2); g.setCornerRadius(dp(14)); g.setStroke(dp(1),Color.rgb(48,59,73)); e.setBackground(g); return e; }
    private Button button(String l,boolean primary){ Button b=new Button(this); b.setText(l); b.setTextSize(14); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT_BOLD); b.setTextColor(primary?Color.rgb(20,18,12):TEXT); b.setPadding(dp(14),dp(11),dp(14),dp(11)); GradientDrawable g=new GradientDrawable(); g.setColor(primary?GOLD:Color.rgb(28,35,45)); g.setCornerRadius(dp(14)); g.setStroke(dp(1),primary?GOLD:Color.rgb(58,69,83)); b.setBackground(g); b.setStateListAnimator(null); return b; }
    private TextView text(String v,int s,boolean bold,int c){ TextView t=new TextView(this); t.setText(v); t.setTextSize(s); t.setTextColor(c); if(bold)t.setTypeface(Typeface.DEFAULT_BOLD); return t; }
    private LinearLayout.LayoutParams match(){ return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); }
    private LinearLayout.LayoutParams margin(int l,int t,int r,int b){ LinearLayout.LayoutParams p=match(); p.setMargins(dp(l),dp(t),dp(r),dp(b)); return p; }
    private LinearLayout.LayoutParams weight(float w,int l,int t,int r,int b){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,w); p.setMargins(dp(l),dp(t),dp(r),dp(b)); return p; }
    private LinearLayout.LayoutParams field(){ return margin(0,0,0,10); }
    private int dp(int v){ return Math.round(v*getResources().getDisplayMetrics().density); }
}

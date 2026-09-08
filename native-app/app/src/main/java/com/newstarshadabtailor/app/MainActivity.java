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

public class MainActivity extends Activity {
    private LinearLayout root;
    private LinearLayout content;
    private LinearLayout nav;
    private SharedPreferences prefs;

    private final int BG = Color.rgb(8, 12, 18);
    private final int CARD = Color.rgb(18, 24, 32);
    private final int CARD2 = Color.rgb(24, 31, 41);
    private final int GOLD = Color.rgb(212, 175, 55);
    private final int TEXT = Color.rgb(245, 247, 250);
    private final int MUTED = Color.rgb(158, 168, 181);
    private final String PHONE = "917565053878";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("nsst", MODE_PRIVATE);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(28));
        scroll.addView(content, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setPadding(dp(8), dp(7), dp(8), dp(9));
        nav.setBackgroundColor(Color.rgb(13, 18, 25));
        root.addView(nav, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addNav("Home", "home");
        addNav("Book", "book");
        addNav("Orders", "orders");
        addNav("Measure", "measure");
        addNav("Profile", "profile");

        setContentView(root);
        showHome();
    }

    private void addNav(String label, String page) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(11);
        b.setAllCaps(false);
        b.setTextColor(TEXT);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setPadding(dp(4), dp(8), dp(4), dp(8));
        b.setOnClickListener(v -> {
            if (page.equals("home")) showHome();
            else if (page.equals("book")) showBooking();
            else if (page.equals("orders")) showOrders();
            else if (page.equals("measure")) showMeasurements();
            else showProfile();
        });
        nav.addView(b, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
    }

    private void clearContent() { content.removeAllViews(); }

    private void topBrand(String pageTitle, String subtitle) {
        TextView mini = text("NSST  •  NEW STAR SHADAB TAILOR", 11, true, GOLD);
        mini.setLetterSpacing(0.12f);
        content.addView(mini, matchWrapMargin(0, 0, 0, 7));
        content.addView(text(pageTitle, 27, true, TEXT), matchWrap());
        TextView sub = text(subtitle, 13, false, MUTED);
        sub.setPadding(0, dp(4), 0, dp(18));
        content.addView(sub, matchWrap());
    }

    private void showHome() {
        clearContent();
        topBrand("Welcome to NSST", "Premium tailoring, now easier to manage from your phone.");

        LinearLayout hero = card(22);
        TextView tag = text("YOUR PERSONAL TAILORING HUB", 11, true, GOLD);
        tag.setLetterSpacing(0.14f);
        hero.addView(tag, matchWrap());
        TextView h = text("Perfect fit.\nOne tap away.", 28, true, TEXT);
        h.setPadding(0, dp(8), 0, dp(8));
        hero.addView(h, matchWrap());
        hero.addView(text("Book stitching, save measurements, check orders and contact the shop from one place.", 14, false, MUTED), matchWrapMargin(0, 0, 0, 15));
        Button book = actionButton("Book New Stitching", true);
        book.setOnClickListener(v -> showBooking());
        hero.addView(book, matchWrap());
        content.addView(hero, matchWrapMargin(0, 0, 0, 18));

        section("Quick Actions", "Everything you need, right here.");
        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(quickCard("My Orders", "Track status", v -> showOrders()), weightMargin(1,0,0,6,0));
        row1.addView(quickCard("Measurements", "Saved sizes", v -> showMeasurements()), weightMargin(1,6,0,0,0));
        content.addView(row1, matchWrapMargin(0,0,0,12));
        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(quickCard("WhatsApp", "Chat now", v -> openWhatsApp("Assalamualaikum, mujhe NSST tailoring service ke baare me jankari chahiye.")), weightMargin(1,0,0,6,0));
        row2.addView(quickCard("Call", "Talk to shop", v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+" + PHONE)))), weightMargin(1,6,0,0,0));
        content.addView(row2, matchWrapMargin(0,0,0,20));

        section("Popular Services", "Craftsmanship for every occasion.");
        service("Shirt Stitching", "Clean collar, balanced shoulder and premium fitting.");
        service("Pant Stitching", "Sharp fall and tailored comfort.");
        service("Suit & Blazer", "Structured premium occasion tailoring.");
        service("Kurta & Traditional", "Elegant festive and formal fitting.");
        service("Alteration", "Accurate fitting corrections and finishing.");
    }

    private void showBooking() {
        clearContent();
        topBrand("Book Stitching", "Send your requirement directly to NSST on WhatsApp.");
        EditText name = input("Your name", InputType.TYPE_CLASS_TEXT);
        EditText phone = input("Phone number", InputType.TYPE_CLASS_PHONE);
        EditText service = input("Service — Shirt, Pant, Suit, Kurta...", InputType.TYPE_CLASS_TEXT);
        EditText date = input("Preferred delivery date", InputType.TYPE_CLASS_TEXT);
        EditText note = input("Notes / fitting preference", InputType.TYPE_CLASS_TEXT);
        String savedName = prefs.getString("profile_name", "");
        String savedPhone = prefs.getString("profile_phone", "");
        name.setText(savedName); phone.setText(savedPhone);
        content.addView(name, fieldMargin());
        content.addView(phone, fieldMargin());
        content.addView(service, fieldMargin());
        content.addView(date, fieldMargin());
        content.addView(note, matchWrapMargin(0,0,0,14));
        Button send = actionButton("Send Booking on WhatsApp", true);
        send.setOnClickListener(v -> {
            String n=name.getText().toString().trim(), p=phone.getText().toString().trim(), s=service.getText().toString().trim();
            if(n.isEmpty()||p.isEmpty()||s.isEmpty()){ Toast.makeText(this,"Please fill name, phone and service.",Toast.LENGTH_SHORT).show(); return; }
            String d=date.getText().toString().trim(), no=note.getText().toString().trim();
            prefs.edit().putString("last_order_service", s).putString("last_order_status", "Booking Requested").putString("last_order_date", d).apply();
            String msg="Assalamualaikum NSST, mujhe stitching booking karni hai.\n\nName: "+n+"\nPhone: "+p+"\nService: "+s+"\nPreferred Delivery: "+d+"\nNotes: "+no;
            openWhatsApp(msg);
        });
        content.addView(send, matchWrap());
    }

    private void showOrders() {
        clearContent();
        topBrand("My Orders", "Your latest stitching request and order status.");
        String service = prefs.getString("last_order_service", "No order yet");
        String status = prefs.getString("last_order_status", "Create a booking to start tracking");
        String date = prefs.getString("last_order_date", "—");
        LinearLayout c = card(20);
        c.addView(text("LATEST ORDER", 11, true, GOLD), matchWrap());
        TextView s = text(service, 22, true, TEXT); s.setPadding(0,dp(8),0,dp(8)); c.addView(s,matchWrap());
        c.addView(labelValue("Status", status), matchWrap());
        c.addView(labelValue("Delivery", date.isEmpty()?"Not specified":date), matchWrap());
        content.addView(c, matchWrapMargin(0,0,0,16));
        Button contact = actionButton("Ask Order Status on WhatsApp", false);
        contact.setOnClickListener(v -> openWhatsApp("Assalamualaikum NSST, meri order status check karni hai. Service: "+service));
        content.addView(contact, matchWrap());
    }

    private void showMeasurements() {
        clearContent();
        topBrand("Measurements", "Save your measurements on this phone for faster future bookings.");
        EditText chest=input("Chest", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText waist=input("Waist", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText shoulder=input("Shoulder", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText sleeve=input("Sleeve", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText shirt=input("Shirt length", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText pant=input("Pant length", InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        chest.setText(prefs.getString("m_chest","")); waist.setText(prefs.getString("m_waist","")); shoulder.setText(prefs.getString("m_shoulder",""));
        sleeve.setText(prefs.getString("m_sleeve","")); shirt.setText(prefs.getString("m_shirt","")); pant.setText(prefs.getString("m_pant",""));
        content.addView(chest,fieldMargin()); content.addView(waist,fieldMargin()); content.addView(shoulder,fieldMargin()); content.addView(sleeve,fieldMargin()); content.addView(shirt,fieldMargin()); content.addView(pant,matchWrapMargin(0,0,0,14));
        Button save=actionButton("Save Measurements",true);
        save.setOnClickListener(v->{
            prefs.edit().putString("m_chest",chest.getText().toString()).putString("m_waist",waist.getText().toString()).putString("m_shoulder",shoulder.getText().toString()).putString("m_sleeve",sleeve.getText().toString()).putString("m_shirt",shirt.getText().toString()).putString("m_pant",pant.getText().toString()).apply();
            Toast.makeText(this,"Measurements saved on this phone.",Toast.LENGTH_SHORT).show();
        });
        content.addView(save,matchWrap());
    }

    private void showProfile() {
        clearContent();
        topBrand("My Profile", "Save basic details for quicker booking.");
        EditText name=input("Full name",InputType.TYPE_CLASS_TEXT);
        EditText phone=input("Phone number",InputType.TYPE_CLASS_PHONE);
        EditText city=input("City / Area",InputType.TYPE_CLASS_TEXT);
        name.setText(prefs.getString("profile_name","")); phone.setText(prefs.getString("profile_phone","")); city.setText(prefs.getString("profile_city",""));
        content.addView(name,fieldMargin()); content.addView(phone,fieldMargin()); content.addView(city,matchWrapMargin(0,0,0,14));
        Button save=actionButton("Save Profile",true);
        save.setOnClickListener(v->{ prefs.edit().putString("profile_name",name.getText().toString().trim()).putString("profile_phone",phone.getText().toString().trim()).putString("profile_city",city.getText().toString().trim()).apply(); Toast.makeText(this,"Profile saved.",Toast.LENGTH_SHORT).show(); });
        content.addView(save,matchWrapMargin(0,0,0,18));
        Button whatsapp=actionButton("Contact NSST on WhatsApp",false); whatsapp.setOnClickListener(v->openWhatsApp("Assalamualaikum NSST")); content.addView(whatsapp,matchWrapMargin(0,0,0,10));
        Button call=actionButton("Call NSST",false); call.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:+"+PHONE)))); content.addView(call,matchWrap());
    }

    private LinearLayout quickCard(String title,String sub,View.OnClickListener click){
        LinearLayout c=card(15); c.setOnClickListener(click); c.setClickable(true);
        c.addView(text(title,16,true,TEXT),matchWrap()); TextView s=text(sub,12,false,MUTED); s.setPadding(0,dp(4),0,0); c.addView(s,matchWrap()); return c;
    }

    private TextView labelValue(String label,String value){ TextView t=text(label+"  •  "+value,13,false,MUTED); t.setPadding(0,dp(4),0,dp(4)); return t; }

    private void openWhatsApp(String message) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/"+PHONE+"?text="+Uri.encode(message)))); }

    private void section(String title,String subtitle){ content.addView(text(title,21,true,TEXT),matchWrap()); TextView s=text(subtitle,13,false,MUTED); s.setPadding(0,dp(4),0,dp(12)); content.addView(s,matchWrap()); }

    private void service(String title,String desc){ LinearLayout c=card(16); c.addView(text(title,16,true,TEXT),matchWrap()); TextView d=text(desc,12,false,MUTED); d.setPadding(0,dp(5),0,0); c.addView(d,matchWrap()); content.addView(c,matchWrapMargin(0,0,0,10)); }

    private LinearLayout card(int padding){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(padding),dp(padding),dp(padding),dp(padding)); GradientDrawable bg=new GradientDrawable(); bg.setColor(CARD); bg.setCornerRadius(dp(20)); bg.setStroke(dp(1),Color.rgb(39,48,61)); c.setBackground(bg); c.setElevation(dp(2)); return c; }

    private EditText input(String hint,int inputType){ EditText e=new EditText(this); e.setHint(hint); e.setHintTextColor(Color.rgb(112,124,140)); e.setTextColor(TEXT); e.setTextSize(14); e.setSingleLine(true); e.setInputType(inputType); e.setPadding(dp(16),dp(13),dp(16),dp(13)); GradientDrawable bg=new GradientDrawable(); bg.setColor(CARD2); bg.setCornerRadius(dp(14)); bg.setStroke(dp(1),Color.rgb(48,59,73)); e.setBackground(bg); return e; }

    private Button actionButton(String label,boolean primary){ Button b=new Button(this); b.setText(label); b.setTextSize(14); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT_BOLD); b.setTextColor(primary?Color.rgb(20,18,12):TEXT); b.setPadding(dp(14),dp(11),dp(14),dp(11)); GradientDrawable bg=new GradientDrawable(); bg.setColor(primary?GOLD:Color.rgb(28,35,45)); bg.setCornerRadius(dp(14)); bg.setStroke(dp(1),primary?GOLD:Color.rgb(58,69,83)); b.setBackground(bg); b.setStateListAnimator(null); return b; }

    private TextView text(String value,int size,boolean bold,int color){ TextView t=new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); if(bold)t.setTypeface(Typeface.DEFAULT_BOLD); return t; }
    private LinearLayout.LayoutParams matchWrap(){ return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); }
    private LinearLayout.LayoutParams matchWrapMargin(int l,int t,int r,int b){ LinearLayout.LayoutParams p=matchWrap(); p.setMargins(dp(l),dp(t),dp(r),dp(b)); return p; }
    private LinearLayout.LayoutParams weightMargin(float weight,int l,int t,int r,int b){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,weight); p.setMargins(dp(l),dp(t),dp(r),dp(b)); return p; }
    private LinearLayout.LayoutParams fieldMargin(){ return matchWrapMargin(0,0,0,10); }
    private int dp(int value){ return Math.round(value*getResources().getDisplayMetrics().density); }
}

package com.newstarshadabtailor.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout content, nav;
    SharedPreferences prefs;
    int BG=Color.rgb(7,10,15), SURFACE=Color.rgb(17,22,30), SURFACE2=Color.rgb(24,31,41), GOLD=Color.rgb(212,175,55), TEXT=Color.rgb(247,248,250), MUTED=Color.rgb(158,168,181), GREEN=Color.rgb(80,190,135), RED=Color.rgb(220,95,95);
    String PHONE="917565053878";

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        prefs=getSharedPreferences("nsst",0);
        migrateLegacy();
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true);
        content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(18),dp(18),dp(18),dp(30));
        scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        nav=new LinearLayout(this); nav.setPadding(dp(6),dp(5),dp(6),dp(7)); nav.setBackgroundColor(Color.rgb(11,15,21));
        addNav("Home"); addNav("Book"); addNav("Orders"); addNav("Measure"); addNav("More");
        root.addView(nav,new LinearLayout.LayoutParams(-1,dp(68))); setContentView(root); home();
    }

    void migrateLegacy(){
        SharedPreferences.Editor e=prefs.edit(); boolean changed=false;
        String[][] map={{"profile_name","name"},{"profile_phone","phone"},{"profile_city","city"},{"m_chest","chest"},{"m_waist","waist"},{"m_shoulder","shoulder"},{"m_sleeve","sleeve"},{"m_shirt","shirt"},{"m_pant","pant"}};
        for(String[] m:map){ if(prefs.getString(m[1],"").isEmpty()&&!prefs.getString(m[0],"").isEmpty()){e.putString(m[1],prefs.getString(m[0],"")); changed=true;} }
        if(changed)e.apply();
    }

    void addNav(String label){ Button b=button(label,false); b.setTextSize(11); b.setOnClickListener(v->go(((Button)v).getText().toString())); nav.addView(b,new LinearLayout.LayoutParams(0,-1,1)); }
    void go(String x){ if(x.equals("Book"))book(); else if(x.equals("Orders"))orders(); else if(x.equals("Measure"))measure(); else if(x.equals("More"))more(); else home(); }
    void clear(){content.removeAllViews();}

    void top(String title,String sub){
        content.addView(text("NSST  •  PREMIUM TAILORING",11,true,GOLD));
        content.addView(text(title,29,true,TEXT),margin(0,8,0,0));
        content.addView(text(sub,13,false,MUTED),margin(0,5,0,18));
    }

    void home(){
        clear(); top("Crafted for your perfect fit","New Star Shadab Tailor • Jaunpur • V5");
        LinearLayout hero=card(true); hero.addView(text("NSST SIGNATURE",10,true,GOLD)); hero.addView(text("Tailoring, redesigned.",30,true,TEXT),margin(0,9,0,6)); hero.addView(text("Premium booking, smart measurements, style planning, order tracking and AI-ready tailoring tools in one elegant app.",14,false,MUTED));
        LinearLayout stats=row(); stats.addView(stat("V5","Premium"),weight()); stats.addView(stat("11+","Measurements"),weight()); stats.addView(stat("24/7","Smart tools"),weight()); hero.addView(stats,margin(0,16,0,0));
        Button primary=button("Book Premium Stitching",true); primary.setOnClickListener(v->book()); hero.addView(primary,margin(0,16,0,0)); content.addView(hero,margin(0,0,0,18));

        section("Smart Studio","Professional tools designed around your tailoring journey.");
        LinearLayout r=row(); r.addView(tile("AI Style Advisor","Personalized style ideas",v->aiStyle()),weightGap()); r.addView(tile("Design Gallery","Explore signature looks",v->gallery()),weightGap()); content.addView(r,margin(0,0,0,10));
        r=row(); r.addView(tile("Style Request","Build your garment",v->style()),weightGap()); r.addView(tile("Fit Profile","Save your preferences",v->fit()),weightGap()); content.addView(r,margin(0,0,0,10));
        r=row(); r.addView(tile("Appointment","Measurement / trial",v->appointment()),weightGap()); r.addView(tile("Delivery Planner","Plan for your event",v->planner()),weightGap()); content.addView(r,margin(0,0,0,18));

        section("Your tailoring","Everything important, one tap away.");
        r=row(); r.addView(tile("My Orders","Track booking progress",v->orders()),weightGap()); r.addView(tile("Measurements","Detailed size profile",v->measure()),weightGap()); content.addView(r,margin(0,0,0,10));
        r=row(); r.addView(tile("Price List","Current services",v->prices()),weightGap()); r.addView(tile("WhatsApp NSST","Speak to the shop",v->wa("Assalamualaikum NSST, mujhe tailoring service ki jankari chahiye.")),weightGap()); content.addView(r);
    }

    LinearLayout stat(String big,String small){ LinearLayout x=new LinearLayout(this); x.setOrientation(LinearLayout.VERTICAL); x.setPadding(dp(6),dp(10),dp(6),dp(10)); x.addView(text(big,17,true,TEXT)); x.addView(text(small,10,false,MUTED)); return x; }

    void book(){
        clear(); top("Book Stitching","Create a professional booking request for NSST.");
        EditText name=input("Customer name",1), phone=input("Phone number",3), service=input("Garment / service",1), delivery=input("Preferred delivery date",1), fit=input("Fit: Slim / Regular / Relaxed",1), notes=input("Style / fitting notes",1);
        name.setText(prefs.getString("name","")); phone.setText(prefs.getString("phone",""));
        for(EditText e:new EditText[]{name,phone,service,delivery,fit,notes}) content.addView(e,field());
        Button save=button("Save Booking & Send on WhatsApp",true); save.setOnClickListener(v->{
            if(name.getText().length()==0||phone.getText().length()==0||service.getText().length()==0){toast("Name, phone and garment are required.");return;}
            prefs.edit().putString("name",name.getText().toString()).putString("phone",phone.getText().toString()).apply();
            addOrder(service.getText().toString(),delivery.getText().toString(),"Booking Received");
            wa("Assalamualaikum NSST, New Premium Booking\nName: "+name.getText()+"\nPhone: "+phone.getText()+"\nGarment: "+service.getText()+"\nPreferred Delivery: "+delivery.getText()+"\nFit: "+fit.getText()+"\nNotes: "+notes.getText());
        }); content.addView(save);
    }

    void addOrder(String garment,String date,String status){
        String created=new SimpleDateFormat("dd MMM yyyy",Locale.getDefault()).format(new Date());
        String row=safe(garment)+"~"+safe(date)+"~"+safe(status)+"~"+created;
        String old=prefs.getString("orders",""); String merged=row+(old.isEmpty()?"":"||"+old);
        String[] all=merged.split("\\|\\|"); if(all.length>25){StringBuilder b=new StringBuilder(); for(int i=0;i<25;i++){if(i>0)b.append("||");b.append(all[i]);} merged=b.toString();}
        prefs.edit().putString("orders",merged).apply();
    }
    String safe(String s){return s.replace("~","-").replace("||","-");}

    void orders(){
        clear(); top("My Orders","Saved booking history with professional status tracking.");
        LinearLayout legend=card(false); legend.addView(text("ORDER JOURNEY",10,true,GOLD)); legend.addView(text("Received  →  Measurement  →  Cutting  →  Stitching  →  Finishing  →  Ready  →  Delivered",12,false,MUTED),margin(0,7,0,0)); content.addView(legend,margin(0,0,0,14));
        String raw=prefs.getString("orders",""); if(raw.isEmpty()){empty("No bookings yet","Your future NSST bookings will appear here.");return;}
        String[] os=raw.split("\\|\\|");
        for(int i=0;i<os.length&&i<15;i++){
            String[] f=os[i].split("~",-1); String garment=f.length>0?f[0]:"Garment", delivery=f.length>1?f[1]:"", status=f.length>2?f[2]:"Booking Received", created=f.length>3?f[3]:"";
            LinearLayout x=card(false); LinearLayout head=row(); head.addView(text("ORDER "+String.format(Locale.getDefault(),"%02d",i+1),10,true,GOLD),new LinearLayout.LayoutParams(0,-2,1)); head.addView(chip(status)); x.addView(head);
            x.addView(text(garment,20,true,TEXT),margin(0,10,0,5)); x.addView(text("Requested • "+created,11,false,MUTED)); x.addView(text("Delivery • "+(delivery.isEmpty()?"To be confirmed":delivery),12,false,MUTED),margin(0,4,0,0));
            Button ask=button("Check Status on WhatsApp",false); ask.setOnClickListener(v->wa("Assalamualaikum NSST, meri "+garment+" booking ka current status check karna hai.")); x.addView(ask,margin(0,12,0,0)); content.addView(x,margin(0,0,0,11));
        }
    }

    TextView chip(String s){ TextView t=text(s,10,true,GREEN); t.setGravity(Gravity.CENTER); t.setPadding(dp(10),dp(6),dp(10),dp(6)); t.setBackground(background(Color.rgb(17,42,33),99,Color.rgb(44,91,70))); return t; }

    void measure(){
        clear(); top("Measurements","Create a detailed fitting profile. Values can be in inches or cm.");
        String unit=prefs.getString("measure_unit","inches");
        LinearLayout unitCard=card(false); unitCard.addView(text("CURRENT UNIT",10,true,GOLD)); unitCard.addView(text(unit.toUpperCase(Locale.getDefault()),18,true,TEXT),margin(0,6,0,0)); Button toggle=button("Switch inches / cm",false); toggle.setOnClickListener(v->{prefs.edit().putString("measure_unit",unit.equals("inches")?"cm":"inches").apply();measure();}); unitCard.addView(toggle,margin(0,10,0,0)); content.addView(unitCard,margin(0,0,0,12));
        String[] keys={"chest","waist","shoulder","sleeve","shirt","pant","hip","inseam","neck","thigh","bottom"};
        String[] labels={"Chest","Waist","Shoulder","Sleeve","Shirt length","Pant length","Hip / Seat","Inseam","Neck","Thigh","Bottom opening"};
        EditText[] fields=new EditText[keys.length];
        for(int i=0;i<keys.length;i++){fields[i]=input(labels[i]+" ("+unit+")",8194);fields[i].setText(prefs.getString(keys[i],""));content.addView(fields[i],field());}
        Button save=button("Save Measurement Profile",true); save.setOnClickListener(v->{SharedPreferences.Editor e=prefs.edit();for(int i=0;i<keys.length;i++)e.putString(keys[i],fields[i].getText().toString());e.apply();toast("Measurement profile saved.");}); content.addView(save);
    }

    void aiStyle(){
        clear(); top("AI Style Advisor","Personalized tailoring ideas with a privacy-first, backend-ready design.");
        LinearLayout note=card(true); note.addView(text("AI STUDIO • BETA",10,true,GOLD)); note.addView(text("Smart recommendations now. Secure cloud AI ready next.",17,true,TEXT),margin(0,7,0,4)); note.addView(text("No secret AI key is stored inside the APK. This V5 advisor gives on-device tailored suggestions while the secure cloud endpoint remains ready for a future backend connection.",12,false,MUTED)); content.addView(note,margin(0,0,0,14));
        EditText occasion=input("Occasion — Wedding / Eid / Office / Daily",1), garment=input("Garment — Shirt / Suit / Kurta / Pant",1), color=input("Preferred colour",1), style=input("Style — Classic / Modern / Minimal / Bold",1);
        for(EditText e:new EditText[]{occasion,garment,color,style})content.addView(e,field());
        Button generate=button("Generate Style Recommendation",true); generate.setOnClickListener(v->{
            String o=occasion.getText().toString().trim(), g=garment.getText().toString().trim(), c=color.getText().toString().trim(), s=style.getText().toString().trim();
            if(g.isEmpty())g="outfit"; if(o.isEmpty())o="special occasion"; if(c.isEmpty())c="deep navy or charcoal"; if(s.isEmpty())s="modern classic";
            showAdvice(o,g,c,s);
        }); content.addView(generate);
    }

    void showAdvice(String occasion,String garment,String color,String style){
        final String g=garment,o=occasion,c=color,s=style;
        String fit=s.toLowerCase().contains("classic")?"balanced regular fit with clean structure":"clean tailored fit with comfortable movement";
        String details=g.toLowerCase().contains("shirt")?"semi-spread collar, neat placket and refined cuffs":g.toLowerCase().contains("kurta")?"clean neckline, subtle detailing and elegant side profile":g.toLowerCase().contains("suit")||g.toLowerCase().contains("blazer")?"structured shoulders, clean lapel line and minimal pocket treatment":"clean finishing with proportionate pocket and hem details";
        String result="For your "+o+", choose a "+s+" "+g+" in "+c+". Recommended fit: "+fit+". Details: "+details+". Keep accessories restrained so the tailoring remains the focus.";
        LinearLayout x=card(false);x.addView(text("PERSONAL STYLE BRIEF",10,true,GOLD));x.addView(text(result,14,false,TEXT),margin(0,9,0,0));Button send=button("Send This Brief to NSST",false);send.setOnClickListener(v->wa("Assalamualaikum NSST, Style Advisor Brief\nOccasion: "+o+"\nGarment: "+g+"\nColour: "+c+"\nStyle: "+s+"\nRecommendation: "+result));x.addView(send,margin(0,12,0,0));content.addView(x,margin(0,14,0,0));
    }

    void gallery(){
        clear(); top("Design Gallery","Signature style directions to discuss with NSST.");
        String[][] items={{"Signature Shirt","Semi-spread collar • clean cuff • tailored fit"},{"Executive Trouser","Flat front • clean break • modern taper"},{"Classic Two-Piece Suit","Structured shoulder • notch lapel • timeless profile"},{"Premium Blazer","Smart casual • refined lapel • elegant pocket line"},{"Modern Kurta","Minimal neckline • balanced length • clean finish"},{"Wedding Formal","Rich silhouette • event-ready finishing • custom details"}};
        for(String[] it:items){LinearLayout x=card(false);x.addView(text("NSST COLLECTION",9,true,GOLD));x.addView(text(it[0],19,true,TEXT),margin(0,7,0,4));x.addView(text(it[1],12,false,MUTED));Button b=button("Request This Style",false);String title=it[0];b.setOnClickListener(v->wa("Assalamualaikum NSST, mujhe Design Gallery ka '"+title+"' style discuss karna hai."));x.addView(b,margin(0,11,0,0));content.addView(x,margin(0,0,0,10));}
    }

    void style(){
        clear(); top("Custom Style Request","Turn your idea into a clear tailoring brief.");
        EditText garment=input("Garment",1),fit=input("Fit — Slim / Regular / Relaxed",1),fabric=input("Fabric / colour",1),collar=input("Collar / neck style",1),pocket=input("Pocket style",1),details=input("Cuff, buttons, lining, other details",1);
        for(EditText e:new EditText[]{garment,fit,fabric,collar,pocket,details})content.addView(e,field());
        Button send=button("Send Professional Style Brief",true);send.setOnClickListener(v->wa("Assalamualaikum NSST, Custom Style Request\nGarment: "+garment.getText()+"\nFit: "+fit.getText()+"\nFabric/Colour: "+fabric.getText()+"\nCollar/Neck: "+collar.getText()+"\nPocket: "+pocket.getText()+"\nDetails: "+details.getText()));content.addView(send);
    }

    void fit(){
        clear(); top("Fit Profile","Save preferences once and reuse them for future bookings.");
        String[] keys={"fitShirt","fitPant","fitCollar","fitSleeve","fitRise"}; String[] labels={"Shirt fit preference","Pant fit preference","Collar preference","Sleeve preference","Pant rise preference"};
        EditText[] f=new EditText[keys.length]; for(int i=0;i<keys.length;i++){f[i]=input(labels[i],1);f[i].setText(prefs.getString(keys[i],""));content.addView(f[i],field());}
        Button save=button("Save Fit Profile",true);save.setOnClickListener(v->{SharedPreferences.Editor e=prefs.edit();for(int i=0;i<keys.length;i++)e.putString(keys[i],f[i].getText().toString());e.apply();toast("Fit profile saved.");});content.addView(save);
    }

    void appointment(){
        clear(); top("Book Appointment","Request a measurement, trial or pickup appointment.");
        EditText type=input("Appointment — Measurement / Trial / Pickup",1),date=input("Preferred date",1),time=input("Preferred time",1),note=input("Notes",1);for(EditText e:new EditText[]{type,date,time,note})content.addView(e,field());
        Button send=button("Request Appointment",true);send.setOnClickListener(v->wa("Assalamualaikum NSST, Appointment Request\nType: "+type.getText()+"\nDate: "+date.getText()+"\nTime: "+time.getText()+"\nNotes: "+note.getText()));content.addView(send);
    }

    void planner(){
        clear(); top("Delivery Planner","Plan stitching around your event and trial date.");
        EditText event=input("Event — Wedding / Eid / Office / Other",1),date=input("Event date",1),garment=input("Garment needed",1),trial=input("Preferred trial date",1);for(EditText e:new EditText[]{event,date,garment,trial})content.addView(e,field());
        Button send=button("Check Timeline with NSST",true);send.setOnClickListener(v->wa("Assalamualaikum NSST, Delivery Planning\nEvent: "+event.getText()+"\nEvent Date: "+date.getText()+"\nGarment: "+garment.getText()+"\nPreferred Trial: "+trial.getText()+"\nPlease confirm a safe stitching timeline."));content.addView(send);
    }

    void more(){
        clear(); top("More","Your profile, preferences and premium NSST tools.");
        content.addView(tile("My Profile","Customer details for faster bookings",v->profile()),field());
        content.addView(tile("AI Style Advisor","Generate a personalized style brief",v->aiStyle()),field());
        content.addView(tile("Design Gallery","Browse signature tailoring directions",v->gallery()),field());
        content.addView(tile("Fit Profile","Personal fitting preferences",v->fit()),field());
        content.addView(tile("Appointment","Measurement, trial or pickup",v->appointment()),field());
        content.addView(tile("Delivery Planner","Plan around your event",v->planner()),field());
        content.addView(tile("Price List","Service categories and latest-rate contact",v->prices()),field());
        content.addView(tile("Refer NSST","Share the shop with friends",v->share()),field());
        content.addView(tile("About NSST V5","App and privacy information",v->about()));
    }

    void profile(){
        clear(); top("My Profile","Save customer details for faster future bookings.");
        EditText name=input("Full name",1),phone=input("Phone",3),city=input("Area / City",1);name.setText(prefs.getString("name",""));phone.setText(prefs.getString("phone",""));city.setText(prefs.getString("city",""));for(EditText e:new EditText[]{name,phone,city})content.addView(e,field());
        Button save=button("Save Profile",true);save.setOnClickListener(v->{prefs.edit().putString("name",name.getText().toString()).putString("phone",phone.getText().toString()).putString("city",city.getText().toString()).apply();toast("Profile saved.");});content.addView(save);
    }

    void prices(){
        clear(); top("Price List","Rates may vary by garment, fabric and finishing. Confirm current prices directly with NSST.");
        String[][] services={{"Shirt Stitching","Custom fit • collar • cuff options"},{"Pant Stitching","Fit • rise • taper customization"},{"Suit & Blazer","Formal tailoring and finishing"},{"Kurta & Traditional","Custom traditional silhouettes"},{"Alteration","Fit correction and adjustments"}};
        for(String[] s:services){LinearLayout x=card(false);x.addView(text(s[0],17,true,TEXT));x.addView(text(s[1],11,false,MUTED),margin(0,4,0,0));x.addView(text("Price on request",12,true,GOLD),margin(0,8,0,0));content.addView(x,margin(0,0,0,9));}
        Button ask=button("Ask Latest Prices on WhatsApp",true);ask.setOnClickListener(v->wa("Assalamualaikum NSST, mujhe latest stitching price list chahiye."));content.addView(ask);
    }

    void about(){
        clear(); top("About NSST V5","Premium customer app for New Star Shadab Tailor.");
        LinearLayout x=card(false);x.addView(text("VERSION 5.0.0",10,true,GOLD));x.addView(text("Privacy-first by design",18,true,TEXT),margin(0,7,0,5));x.addView(text("Profile, measurements and booking history are stored locally on your device in this version. WhatsApp opens externally for shop communication. AI cloud credentials are not embedded in the APK.",12,false,MUTED));content.addView(x);
    }

    void share(){Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,"New Star Shadab Tailor (NSST), Jaunpur — Premium tailoring. Contact: +91 75650 53878");startActivity(Intent.createChooser(i,"Share NSST"));}
    void wa(String message){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/"+PHONE+"?text="+Uri.encode(message))));}catch(Exception e){toast("Unable to open WhatsApp link.");}}

    void section(String a,String b){content.addView(text(a,20,true,TEXT));content.addView(text(b,12,false,MUTED),margin(0,4,0,11));}
    void empty(String a,String b){LinearLayout x=card(false);x.addView(text(a,18,true,TEXT));x.addView(text(b,12,false,MUTED),margin(0,5,0,0));content.addView(x);}
    LinearLayout row(){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);return r;}
    LinearLayout tile(String a,String b,View.OnClickListener l){LinearLayout x=card(false);x.setOnClickListener(l);x.setClickable(true);x.addView(text(a,15,true,TEXT));x.addView(text(b,11,false,MUTED),margin(0,4,0,0));return x;}
    LinearLayout card(boolean accent){LinearLayout x=new LinearLayout(this);x.setOrientation(LinearLayout.VERTICAL);x.setPadding(dp(16),dp(16),dp(16),dp(16));x.setBackground(background(accent?Color.rgb(20,24,31):SURFACE,20,accent?GOLD:Color.rgb(41,50,63)));return x;}
    EditText input(String hint,int type){EditText e=new EditText(this);e.setHint(hint);e.setHintTextColor(Color.rgb(112,125,143));e.setTextColor(TEXT);e.setTextSize(14);e.setSingleLine();e.setInputType(type);e.setPadding(dp(15),dp(14),dp(15),dp(14));e.setBackground(background(SURFACE,14,Color.rgb(45,56,70)));return e;}
    Button button(String s,boolean primary){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTypeface(Typeface.DEFAULT_BOLD);b.setTextColor(primary?Color.rgb(28,23,10):TEXT);b.setBackground(background(primary?GOLD:SURFACE2,14,primary?GOLD:Color.rgb(58,70,87)));return b;}
    TextView text(String s,int size,boolean bold,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT_BOLD);t.setLineSpacing(0,1.08f);return t;}
    GradientDrawable background(int fill,int radius,int stroke){GradientDrawable g=new GradientDrawable();g.setColor(fill);g.setCornerRadius(dp(radius));g.setStroke(dp(1),stroke);return g;}
    LinearLayout.LayoutParams margin(int l,int t,int r,int b){LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(-1,-2);q.setMargins(dp(l),dp(t),dp(r),dp(b));return q;}
    LinearLayout.LayoutParams field(){return margin(0,0,0,10);}
    LinearLayout.LayoutParams weight(){return new LinearLayout.LayoutParams(0,-2,1);}
    LinearLayout.LayoutParams weightGap(){LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(0,-2,1);q.setMargins(dp(5),0,dp(5),0);return q;}
    int dp(int v){return Math.round(v*getResources().getDisplayMetrics().density);}
    void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}

package com.newstarshadabtailor.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(30));
        root.setBackgroundColor(Color.rgb(245, 246, 248));
        scroll.addView(root, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView brand = text("NEW STAR\nSHADAB TAILOR", 26, true, Color.rgb(17, 24, 39));
        brand.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(brand, matchWrap());

        TextView tagline = text("Premium Tailoring. Perfect Fit.", 22, true, Color.rgb(17, 24, 39));
        tagline.setPadding(0, dp(20), 0, dp(8));
        root.addView(tagline, matchWrap());

        TextView intro = text("Professional stitching and alteration services with clean finishing and reliable fitting.", 15, false, Color.DKGRAY);
        intro.setPadding(0, 0, 0, dp(18));
        root.addView(intro, matchWrap());

        Button whatsapp = button("WhatsApp");
        whatsapp.setOnClickListener(v -> {
            String msg = Uri.encode("Assalamualaikum, mujhe tailoring service ke baare me jankari chahiye.");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/?text=" + msg)));
        });
        root.addView(whatsapp, matchWrapMargin(0, 0, 0, 10));

        Button call = button("Call Now");
        call.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917565053878"))));
        root.addView(call, matchWrapMargin(0, 0, 0, 18));

        section("Our Services");
        service("Shirt Stitching", "Perfect fitting with clean finishing");
        service("Pant Stitching", "Classic and modern trouser fitting");
        service("Suit & Blazer", "Premium tailoring for special occasions");
        service("Alteration", "Fast and accurate fitting corrections");

        section("Book a Stitching Request");
        EditText name = input("Your name", InputType.TYPE_CLASS_TEXT);
        EditText phone = input("Phone number", InputType.TYPE_CLASS_PHONE);
        EditText service = input("Service required (Shirt, Pant, Suit...)", InputType.TYPE_CLASS_TEXT);
        root.addView(name, matchWrapMargin(0, 0, 0, 8));
        root.addView(phone, matchWrapMargin(0, 0, 0, 8));
        root.addView(service, matchWrapMargin(0, 0, 0, 12));

        Button submit = button("Submit Request");
        submit.setOnClickListener(v -> {
            if (name.getText().toString().trim().isEmpty() || phone.getText().toString().trim().isEmpty() || service.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill name, phone and service.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Booking request prepared successfully.", Toast.LENGTH_LONG).show();
            }
        });
        root.addView(submit, matchWrap());

        TextView footer = text("New Star Shadab Tailor\nJaunpur, Uttar Pradesh", 13, true, Color.GRAY);
        footer.setGravity(Gravity.CENTER_HORIZONTAL);
        footer.setPadding(0, dp(28), 0, 0);
        root.addView(footer, matchWrap());

        setContentView(scroll);
    }

    private void section(String title) {
        TextView t = text(title, 21, true, Color.rgb(17, 24, 39));
        t.setPadding(0, dp(16), 0, dp(10));
        root.addView(t, matchWrap());
    }

    private void service(String title, String desc) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        card.setBackgroundColor(Color.WHITE);
        card.addView(text(title, 17, true, Color.rgb(17, 24, 39)), matchWrap());
        TextView d = text(desc, 13, false, Color.DKGRAY);
        d.setPadding(0, dp(4), 0, 0);
        card.addView(d, matchWrap());
        root.addView(card, matchWrapMargin(0, 0, 0, 9));
    }

    private EditText input(String hint, int inputType) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(15);
        e.setSingleLine(true);
        e.setInputType(inputType);
        e.setPadding(dp(12), dp(10), dp(12), dp(10));
        e.setBackgroundColor(Color.WHITE);
        return e;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(15);
        b.setAllCaps(false);
        return b;
    }

    private TextView text(String value, int size, boolean bold, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        return t;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams matchWrapMargin(int l, int t, int r, int b) {
        LinearLayout.LayoutParams p = matchWrap();
        p.setMargins(dp(l), dp(t), dp(r), dp(b));
        return p;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}

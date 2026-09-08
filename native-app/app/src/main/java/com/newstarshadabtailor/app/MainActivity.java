package com.newstarshadabtailor.app;

import android.app.Activity;
import android.content.Intent;
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
    private final int BG = Color.rgb(10, 14, 20);
    private final int CARD = Color.rgb(18, 24, 32);
    private final int GOLD = Color.rgb(212, 175, 55);
    private final int TEXT = Color.rgb(245, 247, 250);
    private final int MUTED = Color.rgb(166, 174, 187);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(22), dp(20), dp(34));
        root.setBackgroundColor(BG);
        scroll.addView(root, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView eyebrow = text("BESPOKE MENSWEAR • JAUNPUR", 11, true, GOLD);
        eyebrow.setLetterSpacing(0.18f);
        eyebrow.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(eyebrow, matchWrapMargin(0, 4, 0, 10));

        TextView brand = text("NEW STAR\nSHADAB TAILOR", 30, true, TEXT);
        brand.setGravity(Gravity.CENTER_HORIZONTAL);
        brand.setLineSpacing(0, 0.92f);
        root.addView(brand, matchWrap());

        TextView divider = text("◆", 13, true, GOLD);
        divider.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(divider, matchWrapMargin(0, 8, 0, 18));

        LinearLayout hero = card(22);
        TextView premium = text("PREMIUM TAILORING", 12, true, GOLD);
        premium.setLetterSpacing(0.16f);
        hero.addView(premium, matchWrap());

        TextView headline = text("Crafted for your\nperfect fit.", 27, true, TEXT);
        headline.setPadding(0, dp(8), 0, dp(8));
        hero.addView(headline, matchWrap());

        TextView intro = text("Precision stitching, refined finishing and personal fitting for shirts, trousers, suits, blazers and special occasions.", 14, false, MUTED);
        intro.setLineSpacing(dp(3), 1f);
        hero.addView(intro, matchWrapMargin(0, 0, 0, 16));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button whatsapp = actionButton("WhatsApp", true);
        Button call = actionButton("Call Now", false);
        actions.addView(whatsapp, weightMargin(1, 0, 0, 6, 0));
        actions.addView(call, weightMargin(1, 6, 0, 0, 0));
        hero.addView(actions, matchWrap());

        whatsapp.setOnClickListener(v -> openWhatsApp("Assalamualaikum, mujhe tailoring service ke baare me jankari chahiye."));
        call.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917565053878"))));
        root.addView(hero, matchWrapMargin(0, 0, 0, 22));

        section("Our Signature Services", "Classic craftsmanship with a modern finish.");
        service("01", "Shirt Stitching", "Clean collar, precise shoulder and comfortable custom fit.");
        service("02", "Pant Stitching", "Sharp fall, balanced waist and tailored trouser silhouette.");
        service("03", "Suit & Blazer", "Premium occasion tailoring with structured finishing.");
        service("04", "Kurta & Traditional", "Elegant traditional fitting for festive and formal wear.");
        service("05", "Alteration", "Accurate corrections for a better, cleaner fit.");

        LinearLayout promise = card(18);
        promise.addView(text("THE NEW STAR STANDARD", 11, true, GOLD), matchWrap());
        TextView p = text("Precision • Clean Finishing • Personal Fit", 17, true, TEXT);
        p.setPadding(0, dp(8), 0, dp(5));
        promise.addView(p, matchWrap());
        promise.addView(text("Every garment is handled with attention to proportion, comfort and finishing.", 13, false, MUTED), matchWrap());
        root.addView(promise, matchWrapMargin(0, 14, 0, 22));

        section("Book Your Stitching", "Send your requirement directly on WhatsApp.");
        EditText name = input("Your name", InputType.TYPE_CLASS_TEXT);
        EditText phone = input("Phone number", InputType.TYPE_CLASS_PHONE);
        EditText service = input("Service required — Shirt, Pant, Suit...", InputType.TYPE_CLASS_TEXT);
        root.addView(name, matchWrapMargin(0, 0, 0, 10));
        root.addView(phone, matchWrapMargin(0, 0, 0, 10));
        root.addView(service, matchWrapMargin(0, 0, 0, 14));

        Button submit = actionButton("Send Booking on WhatsApp", true);
        submit.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String ph = phone.getText().toString().trim();
            String s = service.getText().toString().trim();
            if (n.isEmpty() || ph.isEmpty() || s.isEmpty()) {
                Toast.makeText(this, "Please fill name, phone and service.", Toast.LENGTH_SHORT).show();
                return;
            }
            String msg = "Assalamualaikum, New Star Shadab Tailor.\n\nName: " + n + "\nPhone: " + ph + "\nService: " + s + "\n\nMujhe booking karni hai.";
            openWhatsApp(msg);
        });
        root.addView(submit, matchWrapMargin(0, 0, 0, 20));

        LinearLayout footerCard = card(18);
        TextView footerBrand = text("NEW STAR SHADAB TAILOR", 16, true, TEXT);
        footerBrand.setGravity(Gravity.CENTER_HORIZONTAL);
        footerCard.addView(footerBrand, matchWrap());
        TextView footer = text("Premium men's tailoring • Jaunpur, Uttar Pradesh\nCall: 75650 53878", 12, false, MUTED);
        footer.setGravity(Gravity.CENTER_HORIZONTAL);
        footer.setPadding(0, dp(6), 0, 0);
        footerCard.addView(footer, matchWrap());
        root.addView(footerCard, matchWrapMargin(0, 0, 0, 8));

        setContentView(scroll);
    }

    private void openWhatsApp(String message) {
        String msg = Uri.encode(message);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/917565053878?text=" + msg)));
    }

    private void section(String title, String subtitle) {
        TextView t = text(title, 21, true, TEXT);
        root.addView(t, matchWrap());
        TextView s = text(subtitle, 13, false, MUTED);
        s.setPadding(0, dp(4), 0, dp(12));
        root.addView(s, matchWrap());
    }

    private void service(String number, String title, String desc) {
        LinearLayout row = card(16);
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView badge = text(number, 12, true, GOLD);
        badge.setGravity(Gravity.CENTER);
        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setColor(Color.rgb(31, 38, 48));
        badgeBg.setCornerRadius(dp(12));
        badgeBg.setStroke(dp(1), Color.rgb(74, 66, 41));
        badge.setBackground(badgeBg);
        row.addView(badge, new LinearLayout.LayoutParams(dp(48), dp(48)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.setPadding(dp(14), 0, 0, 0);
        copy.addView(text(title, 16, true, TEXT), matchWrap());
        TextView d = text(desc, 12, false, MUTED);
        d.setPadding(0, dp(4), 0, 0);
        d.setLineSpacing(dp(2), 1f);
        copy.addView(d, matchWrap());
        row.addView(copy, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        root.addView(row, matchWrapMargin(0, 0, 0, 10));
    }

    private LinearLayout card(int padding) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(padding), dp(padding), dp(padding), dp(padding));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(CARD);
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), Color.rgb(37, 46, 58));
        card.setBackground(bg);
        card.setElevation(dp(2));
        return card;
    }

    private EditText input(String hint, int inputType) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(Color.rgb(115, 126, 141));
        e.setTextColor(TEXT);
        e.setTextSize(14);
        e.setSingleLine(true);
        e.setInputType(inputType);
        e.setPadding(dp(16), dp(13), dp(16), dp(13));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(CARD);
        bg.setCornerRadius(dp(14));
        bg.setStroke(dp(1), Color.rgb(42, 52, 65));
        e.setBackground(bg);
        return e;
    }

    private Button actionButton(String label, boolean primary) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setTextColor(primary ? Color.rgb(20, 18, 12) : TEXT);
        b.setPadding(dp(14), dp(11), dp(14), dp(11));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(primary ? GOLD : Color.rgb(28, 35, 45));
        bg.setCornerRadius(dp(14));
        bg.setStroke(dp(1), primary ? GOLD : Color.rgb(56, 67, 81));
        b.setBackground(bg);
        b.setStateListAnimator(null);
        return b;
    }

    private TextView text(String value, int size, boolean bold, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
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

    private LinearLayout.LayoutParams weightMargin(float weight, int l, int t, int r, int b) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight);
        p.setMargins(dp(l), dp(t), dp(r), dp(b));
        return p;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}

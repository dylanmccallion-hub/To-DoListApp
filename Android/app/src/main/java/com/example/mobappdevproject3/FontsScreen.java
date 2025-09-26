package com.example.mobappdevproject3;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

public class FontsScreen extends AppCompatActivity {

    private Button confirmButton;
    private Button returnButton;
    private RadioGroup radioGroup;
    private RadioButton arialButton;
    private RadioButton comicsansButton;
    private RadioButton cursiveButton;
    private RadioButton timesButton;
    private RadioButton choiceSelected;
    SharedPreferences sp_database;

    private String name;
    private String font;
    private String fontString;
    private Boolean isWhite;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts_screen);

        constraintLayout = findViewById(R.id.consLayoutFontScreen);

        Typeface arial = ResourcesCompat.getFont(this, R.font.arial);
        Typeface comicSans = ResourcesCompat.getFont(this, R.font.comicsans);
        Typeface cursive = ResourcesCompat.getFont(this, R.font.cursive);
        Typeface timesNew = ResourcesCompat.getFont(this, R.font.times);

        sp_database = getApplicationContext().getSharedPreferences("table_store_text", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_database.edit();

        name = sp_database.getString("key_saved_text", "black");
        font = sp_database.getString("key_font_text", "arial");

        if (name.equals("white")) {
            isWhite = true;
            constraintLayout.setBackgroundColor(Color.WHITE);
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }

        if(font.equals("comic")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), comicSans);
        } else if(font.equals("cursive")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), cursive);
        } else if(font.equals("times")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), timesNew);
        } else {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), arial);
        }

        confirmButton = findViewById(R.id.confirmButton);
        returnButton = findViewById(R.id.returnButton);

        radioGroup = findViewById(R.id.radioGroup);
        arialButton = findViewById(R.id.arialButton);
        comicsansButton = findViewById(R.id.comicSansButton);
        cursiveButton = findViewById(R.id.cursiveButton);
        timesButton = findViewById(R.id.timesNewButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                choiceSelected = findViewById(selectedId);
                FancyToast.makeText(FontsScreen.this,choiceSelected.getText(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                sp_database = getApplicationContext().getSharedPreferences("table_store_text", MODE_PRIVATE);

                if(arialButton.isChecked()) {
                    applyFontToChildViews(getWindow().findViewById(android.R.id.content), arial);
                    fontString = "arial";
                    editor.putString("key_font_text", fontString);
                    editor.commit();
                }

                if(comicsansButton.isChecked()) {
                    applyFontToChildViews(getWindow().findViewById(android.R.id.content), comicSans);
                    fontString = "comic";
                    editor.putString("key_font_text", fontString);
                    editor.commit();
                }

                if(cursiveButton.isChecked()) {
                    applyFontToChildViews(getWindow().findViewById(android.R.id.content), cursive);
                    fontString = "cursive";
                    editor.putString("key_font_text", fontString);
                    editor.commit();
                }

                if(timesButton.isChecked()) {
                    applyFontToChildViews(getWindow().findViewById(android.R.id.content), timesNew);
                    fontString = "times";
                    editor.putString("key_font_text", fontString);
                    editor.commit();
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FontsScreen.this, Screen2.class);
                startActivity(i);
            }
        });

    }

    private void applyFontToChildViews(View view, Typeface font) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTypeface(font);
                } else if (child instanceof ViewGroup) {
                    applyFontToChildViews(child, font);
                }
            }
        }
    }

}
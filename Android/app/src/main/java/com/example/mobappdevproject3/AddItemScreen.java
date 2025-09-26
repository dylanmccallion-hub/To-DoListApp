package com.example.mobappdevproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemScreen extends AppCompatActivity {

    private EditText itemName;
    private EditText itemDescription;
    private CalendarView calendarView;
    private String selectedDate;

    private Button submitButton;

    private String name;
    private String font;

    private SharedPreferences sp_database;
    private ConstraintLayout constraintLayout;
    boolean isWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_screen);

        constraintLayout = findViewById(R.id.consLayoutAddItemScreen);

        sp_database = getApplicationContext().getSharedPreferences("table_store_text", MODE_PRIVATE);

        Typeface arial = ResourcesCompat.getFont(this, R.font.arial);
        Typeface comicSans = ResourcesCompat.getFont(this, R.font.comicsans);
        Typeface cursive = ResourcesCompat.getFont(this, R.font.cursive);
        Typeface timesNew = ResourcesCompat.getFont(this, R.font.times);

        name = sp_database.getString("key_saved_text", "black");
        font = sp_database.getString("key_font_text", "arial");

        if (name.equals("white")) {
            isWhite = true;
            constraintLayout.setBackgroundColor(Color.WHITE);
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }

        if (font.equals("comic")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), comicSans);
        } else if (font.equals("cursive")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), cursive);
        } else if (font.equals("times")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), timesNew);
        } else {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), arial);
        }

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        calendarView = findViewById(R.id.calendarDueDate);
        submitButton = findViewById(R.id.submitItemButton);

        calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemName != null && itemDescription != null && selectedDate != null) {
                    showSuccessDialog();
                } else {
                    showErrorDialog();
                }
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

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemScreen.this,  R.style.AlertDialogTheme);
        View view = LayoutInflater.from(AddItemScreen.this).inflate(
                R.layout.layout_success_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Success!");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Item added to list!");
        ((Button) view.findViewById(R.id.buttonAction)).setText("Return to list");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent i = new Intent(AddItemScreen.this, Screen2.class);
                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                String dueDate = selectedDate;
                i.putExtra("ItemName", name);
                i.putExtra("ItemDesc", description);
                i.putExtra("DueDate", dueDate);
                startActivity(i);
            }
        });

        if(alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemScreen.this,  R.style.AlertDialogTheme);
        View view = LayoutInflater.from(AddItemScreen.this).inflate(
                R.layout.layout_error_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Error!");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Please ensure all fields have values before submitting.");
        ((Button) view.findViewById(R.id.buttonAction)).setText("Dismiss");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_error);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

//    private void showWarningDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,  R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.layout_warning_dialog,
//                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
//        );
//        builder.setView(view);
//        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.warning_title));
//        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.dummy_text));
//        ((Button) view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.yes));
//        ((Button) view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.no));
//        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);
//
//        final AlertDialog alertDialog = builder.create();
//
//        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//                Toast.makeText(MainActivity.this,"Yes", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//                Toast.makeText(MainActivity.this,"No", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if(alertDialog.getWindow() !=null){
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        alertDialog.show();
//
//    }
}
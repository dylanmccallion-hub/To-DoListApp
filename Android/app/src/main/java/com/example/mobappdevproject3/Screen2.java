package com.example.mobappdevproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.format.DateFormat;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Screen2 extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> listItems;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sp_database;
    private boolean isWhite = false;

    private String txt = "black";

    private CustomAdapter listAdapter;

    private String name;
    private String font;
    private String itemName;
    private String itemDesc;
    private String dueDate;
    private String subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        constraintLayout = findViewById(R.id.consLayoutScreen2);

        sp_database = getApplicationContext().getSharedPreferences("table_store_text", MODE_PRIVATE);
        name = sp_database.getString("key_saved_text", "black");
        font = sp_database.getString("key_font_text", "arial");
        SharedPreferences.Editor editor = sp_database.edit();

        Typeface arial = ResourcesCompat.getFont(this, R.font.arial);
        Typeface comicSans = ResourcesCompat.getFont(this, R.font.comicsans);
        Typeface cursive = ResourcesCompat.getFont(this, R.font.cursive);
        Typeface timesNew = ResourcesCompat.getFont(this, R.font.times);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) % 100;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = sdf.format(calendar.getTime());

        if (minute < 10) {
            String min = Integer.toString(minute);
            String zero = "0";
            min = zero + min;
            subtitle = dayOfWeek + " " + day + "/" + month + "/" + year + "\n" + hour + ":" + min;
        } else {
            subtitle = dayOfWeek + " " + day + "/" + month + "/" + year + "\n" + hour + ":" + minute;
        }

        Toolbar toolbar = findViewById(R.id.toolbarHomepage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TO DO LIST");
        getSupportActionBar().setSubtitle(subtitle);

        listView = findViewById(R.id.listView);

        listItems = MySharedPreferences.getArrayList(getApplicationContext());

        Collections.sort(listItems);

        listAdapter = new CustomAdapter(this, R.layout.row, listItems);

        if (name.equals("white")) {
            isWhite = true;
            constraintLayout.setBackgroundColor(Color.WHITE);
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }

        if (font.equals("comic")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), comicSans);
            listAdapter.customFont = comicSans;
        } else if (font.equals("cursive")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), cursive);
            listAdapter.customFont = cursive;
        } else if (font.equals("times")) {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), timesNew);
            listAdapter.customFont = timesNew;
        } else {
            applyFontToChildViews(getWindow().findViewById(android.R.id.content), arial);
            listAdapter.customFont = arial;
        }

        try {
            Bundle extras = getIntent().getExtras();
            itemName = extras.getString("ItemName");
            itemDesc = extras.getString("ItemDesc");
            dueDate = extras.getString("DueDate");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (itemName != null && itemDesc != null && dueDate != null) {
            listItems.add(itemName + "\n" + itemDesc + "\nDue Date: " + dueDate);
        }
        listView.setAdapter(listAdapter);
        MySharedPreferences.saveArrayList(getApplicationContext(), listItems);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.addIcon) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return true;
                    }
                    Intent i = new Intent(Screen2.this, AddItemScreen.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.darkModeToggle) {
                    sp_database = getApplicationContext().getSharedPreferences("table_store_text", MODE_PRIVATE);
                    if (isWhite == false) {
                        constraintLayout.setBackgroundColor(Color.WHITE);
                        txt = "white";
                        isWhite = true;
                    } else {
                        constraintLayout.setBackgroundColor(Color.BLACK);
                        txt = "black";
                        isWhite = false;
                    }
                    editor.putString("key_saved_text", txt);
                    editor.commit();
                    FancyToast.makeText(Screen2.this, "Switching mode...", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
                } else if (item.getItemId() == R.id.changeFont) {
                    Intent i = new Intent(Screen2.this, FontsScreen.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.sortAlphabetically) {
                    FancyToast.makeText(Screen2.this, "Sorted Alphabetically!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    Collections.sort(listItems);
                    listView.setAdapter(listAdapter);
                } else if (item.getItemId() == R.id.sortReverse) {
                    FancyToast.makeText(Screen2.this, "Sorted Reverse Alphabetically!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    Collections.reverse(listItems);
                    listView.setAdapter(listAdapter);
                } else if (item.getItemId() == R.id.sortDate) {
                    FancyToast.makeText(Screen2.this, "Sorted By Due Date!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    sortByDueDate(listItems);

                    listView.setAdapter(listAdapter);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                notificationBuilder(listItems.get(position), 1);

                FancyToast.makeText(Screen2.this, "Notification Posted!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showWarningDialog(position);
                return true;
            }
        });
    }

    public static void sortByDueDate(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(String str1, String str2) {
                try {
                    Date date1 = dateFormat.parse(str1.substring(str1.indexOf("Due Date: ") + "Due Date: ".length()));
                    Date date2 = dateFormat.parse(str2.substring(str2.indexOf("Due Date: ") + "Due Date: ".length()));
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing exception
                }
                return 0;
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

    private void showWarningDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Screen2.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Screen2.this).inflate(
                R.layout.layout_warning_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Remove List Item");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Remove item from list?");
        ((Button) view.findViewById(R.id.buttonYes)).setText("Yes");
        ((Button) view.findViewById(R.id.buttonNo)).setText("No");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                listItems.remove(position);
                listAdapter.notifyDataSetChanged();
                MySharedPreferences.saveArrayList(getApplicationContext(), listItems);
                FancyToast.makeText(Screen2.this, "Item Removed", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                //Toast.makeText(Screen2.this,"Yes", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //Toast.makeText(Screen2.this,"No", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel01";
            String description = "channelDesc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notificationBuilder(String title, int notificationId) {
        // Create an explicit intent for an Activity in your app.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Screen2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel01")
                .setSmallIcon(R.drawable.list_icon)
                .setContentTitle(title)
                .setContentText("Due Today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
}
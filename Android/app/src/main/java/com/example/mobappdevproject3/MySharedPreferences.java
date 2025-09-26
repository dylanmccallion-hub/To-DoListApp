package com.example.mobappdevproject3;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MySharedPreferences {

    private static final String PREF_NAME = "my_pref";
    private static final String KEY_ARRAY_LIST = "array_list";

    public static void saveArrayList(Context context, ArrayList<String> arrayList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString(KEY_ARRAY_LIST, json);
        editor.apply();
    }

    public static ArrayList<String> getArrayList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_ARRAY_LIST, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Return empty ArrayList if data is null
        }
    }
}

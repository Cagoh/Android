package cs50.caleb.receiptocr3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context){

        super(context,UserActivity.profileName + ".db",null,DATABASE_VERSION);


    }
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        String createTableStatement = "CREATE TABLE history " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "user_name TEXT," +
                "merchant_name TEXT," +
                "date DATE," +
                "time TIME," +
                "spent DOUBLE," +
                "raw_data TEXT)";

        db.execSQL(createTableStatement);


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }



    public boolean addReceipt(String jsonString, String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("jsonString",jsonString);

        // Get the current date and time
        LocalDateTime currentDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
        }

        // Format the date and time using a formatter
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = currentDateTime.format(formatter);
        }

        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formattedDate = currentDateTime.format(formatter);
        }

        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            formattedTime = currentDateTime.format(formatter);
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Check whether the OCR can read the message successfully
        String success = "";
        try {
            success =  jsonObject.getString("success");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (success == "false")
            return false;

        String merchantNameValue;
        try {
            merchantNameValue = jsonObject.getJSONArray("receipts").getJSONObject(0).getString("merchant_name");
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //return false;
        }

        String dateValue;
        try {
            dateValue = jsonObject.getJSONArray("receipts").getJSONObject(0).getString("date");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Double spentValue;
        try {
            spentValue = jsonObject.getJSONArray("receipts").getJSONObject(0).getDouble("total");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Insert values into database
        ContentValues values = new ContentValues();
        values.put("user_name", userName);
        values.put("merchant_name", merchantNameValue);
        values.put("date", formattedDate);
        values.put("time", formattedTime);
        values.put("spent", spentValue);
        values.put("raw_data", jsonString);
        db.insert("history", null, values);



        return true;
    }

    public boolean submitReceipt(String userName, String merchantNameValue, String formattedDate, String formattedTime, double spentValue) {
        Log.d("submitReceipt","");
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("user_name", userName);
        values.put("merchant_name", merchantNameValue);
        values.put("date", formattedDate);
        values.put("time", formattedTime);
        values.put("spent", spentValue);
        values.put("raw_data", "");
        db.insert("history", null, values);


        return true;
    }

}
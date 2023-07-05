package cs50.caleb.receiptocr3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private TextView totalSpentTextView;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<History> historyArrayList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    TextView textViewProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_history);

        textViewProfileName = findViewById(R.id.profile_name);
        textViewProfileName.setText("Profile: " + UserActivity.profileName);

        // Add data to historyArrayList from sqlite database
        showHistory();

        totalSpentTextView = findViewById(R.id.totalSpentTextView);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyAdapter = new HistoryAdapter(this, historyArrayList);
        recyclerView.setAdapter(historyAdapter);

        startDatePicker = findViewById(R.id.start_date_picker);
        endDatePicker = findViewById(R.id.end_date_picker);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        startDatePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            Log.d("startDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener()", "");
            loadHistory();
        });

        endDatePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            Log.d("endDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener()", "");
            loadHistory();
        });

        loadHistory();
        //addData();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_history);





        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
                    finish();
                    return true;
                case R.id.navigation_receipt:
                    startActivity(new Intent(HistoryActivity.this, ReceiptActivity.class));
                    finish();
                    return true;
                case R.id.navigation_user:
                    startActivity(new Intent(HistoryActivity.this, UserActivity.class));
                    finish();
                    return true;
            }
            return false;
        });

    }

    private void loadHistory() {
        historyArrayList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyArrayList);
        recyclerView.setAdapter(historyAdapter);
        Log.d("loadHistory()","");
        int startYear = startDatePicker.getYear();
        int startMonth = startDatePicker.getMonth() + 1;
        int startDay = startDatePicker.getDayOfMonth();
        String startDateString = String.format("%04d-%02d-%02d", startYear, startMonth, startDay);

        int endYear = endDatePicker.getYear();
        int endMonth = endDatePicker.getMonth() + 1;
        int endDay = endDatePicker.getDayOfMonth();
        String endDateString = String.format("%04d-%02d-%02d", endYear, endMonth, endDay);

        Log.d("startDateString",startDateString);

        Log.d("endDateString",endDateString);


        DatabaseHelper databaseHelper = new DatabaseHelper(HistoryActivity.this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM history WHERE date BETWEEN ? AND ? ORDER BY date DESC, time DESC", new String[]{startDateString, endDateString});
        Log.d("cursor",cursor.toString());
        Log.d("cursor count", String.valueOf(cursor.getCount()));

        // Get the rows of database and add it to the Array
        double totalSpent = 0.0;
        while (cursor.moveToNext()) {
            Log.d("while...","");
            int id = cursor.getInt(0);
            String userName = cursor.getString(1);
            String merchantName = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            double spent = cursor.getDouble(5);
            String jsonString = cursor.getString(6);

            historyArrayList.add(new History(id, userName, merchantName, date, time, spent, jsonString));
            totalSpent += spent;


        }

        cursor.close();
        database.close();
        historyAdapter.notifyDataSetChanged();

        totalSpentTextView.setText(String.format("Total spent: $%.2f", totalSpent));
    }


    void showHistory() {

    }

    void addData() {
        historyArrayList.add(new History(1 ,"Admin","Admin","Admin","Admin",1.0,"Admin"));
        historyArrayList.add(new History(2 ,"Admin","Admin","Admin","Admin",1.0,"Admin"));
        historyArrayList.add(new History(3 ,"Admin","Admin","Admin","Admin",1.0,"Admin"));
        historyArrayList.add(new History(4 ,"Admin","Admin","Admin","Admin",1.0,"Admin"));

        historyAdapter.notifyDataSetChanged();
    }






}


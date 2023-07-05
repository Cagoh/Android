package cs50.caleb.receiptocr3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReceiptActivityManually extends AppCompatActivity {

    private EditText edtTxtMerchantName, edtTxtSpent;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private BottomNavigationView bottomNavigationView;
    TextView textViewProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_receipt_manually);

        textViewProfileName = findViewById(R.id.profile_name);
        textViewProfileName.setText("Profile: " + UserActivity.profileName);



        edtTxtMerchantName = findViewById(R.id.edtTxtMerchantName);
        edtTxtSpent = findViewById(R.id.edtTxtSpent);

        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);



    }

    public void submitReceipt(View view) {
        //String merchantName = edtTxtMerchantName.getText().toString();

        String merchantName = "";
        if (TextUtils.isEmpty(edtTxtMerchantName.getText().toString())) {
            Toast.makeText(this, "Merchant name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            merchantName = edtTxtMerchantName.getText().toString();
        }


        // Get the selected date from the DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        // Get the selected time from the TimePicker
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Create a Date object with the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);
        Date dateTime = calendar.getTime();

        // Format the date and time using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dateTime);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(dateTime);

        double spent = 0;

        try {
            spent = Double.parseDouble(edtTxtSpent.getText().toString());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedValue = decimalFormat.format(spent);
            double roundedValue = Double.parseDouble(formattedValue);
            boolean hasTwoDecimalPlaces = spent == roundedValue;

            if (hasTwoDecimalPlaces) {
                // Value has two decimal places
                DatabaseHelper databaseHelper = new DatabaseHelper(ReceiptActivityManually.this);
                databaseHelper.submitReceipt(UserActivity.profileName, merchantName, formattedDate, formattedTime, spent);
                recreate();
            } else {
                Toast.makeText(this, "Invalid money spent", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid money spent", Toast.LENGTH_SHORT).show();
        }







    }
}

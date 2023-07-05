package cs50.caleb.receiptocr3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UserActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Spinner profileSpinner;

    private EditText edtTxtName;

    public static String profileName;

    private Spinner selectProfileSpinner;
    private Spinner deleteProfileSpinner;
    TextView textViewProfileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_user);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_user);

        textViewProfileName = findViewById(R.id.profile_name);
        textViewProfileName.setText("Profile: " + UserActivity.profileName);

        // First before showing the users, scan the file name in databases folder and get every fi
        // file name
        // get a reference to the databases directory
        File databasesDir = getApplicationContext().getDatabasePath("dummy").getParentFile();


        // create a set to hold the unique file names
        Set<String> fileNames = new LinkedHashSet<>();


        File[] files = databasesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // get the file name without the extension
                    String fileName = file.getName();
                    int pos = fileName.lastIndexOf(".");
                    if (pos > 0) {
                        fileName = fileName.substring(0, pos);
                    }
                    // add the file name to the list
                    fileNames.add(fileName);
                }
            }
        }

        profileSpinner = findViewById(R.id.profile_spinner);
        deleteProfileSpinner = findViewById(R.id.delete_profile_spinner);

        // create an adapter with the file names and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(fileNames));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapter);
        deleteProfileSpinner.setAdapter(adapter);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(UserActivity.this, HomeActivity.class));
                        finish();
                        return true;
                    case R.id.navigation_receipt:
                        startActivity(new Intent(UserActivity.this, ReceiptActivity.class));
                        finish();
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(UserActivity.this, HistoryActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void selectProfile(View view) {
        selectProfileSpinner = findViewById(R.id.profile_spinner);
        profileName = selectProfileSpinner.getSelectedItem().toString();
        textViewProfileName.setText("Profile: " + UserActivity.profileName);

    }
    public void createProfile(View view) {
        edtTxtName = findViewById(R.id.nameEditText);

        if (TextUtils.isEmpty(edtTxtName.getText().toString())) {
            Toast.makeText(this, "The profile name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            profileName = edtTxtName.getText().toString();
            DatabaseHelper databaseHelper = new DatabaseHelper(UserActivity.this);


            // to create profileName.db
            databaseHelper.getReadableDatabase();
            recreate();

            //send to sql to create "name".db
        }


    }

    public void deleteProfile(View view) {
        deleteProfileSpinner = findViewById(R.id.delete_profile_spinner);
        String deleteProfile = deleteProfileSpinner.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete " + deleteProfile + "?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            File databasesDir = getApplicationContext().getDatabasePath("dummy").getParentFile();

            String fName;
            File[] files = databasesDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // get the file name without the extension
                        fName = file.getName();
                        if ((deleteProfile + ".db").equals(fName)) {
                            file.delete();
                        }

                        if ((deleteProfile + ".db-journal").equals(fName)) {
                            file.delete();
                        }
                    }
                }
            }
            if (deleteProfile == UserActivity.profileName) {
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                finish();
                //recreate();
            }

        });
        builder.setNegativeButton("No", (dialog, id) -> {
            // User cancelled the dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


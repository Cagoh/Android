package cs50.caleb.receiptocr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Spinner selectProfileSpinner;
    private EditText nameEditText;
    //TextView textViewProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        selectProfileSpinner = findViewById(R.id.profile_spinner);

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

        selectProfileSpinner = findViewById(R.id.profile_spinner);

        // create an adapter with the file names and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(fileNames));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectProfileSpinner.setAdapter(adapter);


    }

    public void selectProfile(View view) {
        selectProfileSpinner = findViewById(R.id.profile_spinner);


            UserActivity.profileName = selectProfileSpinner.getSelectedItem().toString();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();


    }

    public void createProfile(View view) {
        nameEditText = findViewById(R.id.nameEditText);

        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Log.d("if","");
            Toast.makeText(this, "The profile name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("else","");
            UserActivity.profileName = nameEditText.getText().toString();
            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
            databaseHelper.getReadableDatabase();
            recreate();
        }


    }
}

package cs50.caleb.receiptocr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    TextView textViewProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_home);

        textViewProfileName = findViewById(R.id.profile_name);
        textViewProfileName.setText("Profile: " + UserActivity.profileName);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_receipt:
                    startActivity(new Intent(HomeActivity.this, ReceiptActivity.class));
                    finish();
                    return true;
                case R.id.navigation_history:
                    startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                    finish();
                    return true;
                case R.id.navigation_user:
                    startActivity(new Intent(HomeActivity.this, UserActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }
}

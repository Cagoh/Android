package com.caleb.cardviewrecyclerviewsqlite1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FragmentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private PersonFragment personFragment = new PersonFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My App");
        actionBar.setSubtitle("Version 1.0");
        actionBar.setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        BadgeDrawable badgeDrawablePerson = bottomNavigationView.getOrCreateBadge(R.id.person);
        badgeDrawablePerson.setVisible(true);
        badgeDrawablePerson.setNumber(8);

        BadgeDrawable badgeDrawableHome = bottomNavigationView.getOrCreateBadge(R.id.home);
        badgeDrawableHome.setVisible(true);
        badgeDrawableHome.setNumber(8);

        BadgeDrawable badgeDrawableSettings = bottomNavigationView.getOrCreateBadge(R.id.settings);
        badgeDrawableSettings.setVisible(true);
        badgeDrawableSettings.setNumber(8);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        badgeDrawableHome.setVisible(false);
                        badgeDrawableHome.setNumber(0);
                        return true;
                    case R.id.person:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,personFragment).commit();
                        badgeDrawablePerson.setVisible(false);
                        badgeDrawablePerson.setNumber(0);
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                        badgeDrawableSettings.setVisible(false);
                        badgeDrawableSettings.setNumber(0);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            // Handle search button click

            return true;
        }

        if (id == R.id.action_settings) {
            // Handle settings button click
            BadgeDrawable badgeDrawableSettings = bottomNavigationView.getOrCreateBadge(R.id.settings);

            getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();

            badgeDrawableSettings.setVisible(false);
            badgeDrawableSettings.setNumber(0);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

}





package com.caleb.cardviewrecyclerviewsqlite1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PersonAdapter personAdapter;
    private List<Person> personList;

    private Button btnSubmit;
    private ImageButton refreshBtn;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);

        // RecyclerView scroll vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // RecyclerView scroll HORIZONTAL
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Load data from SQLite local storage and populate the RecyclerView
        //personList = loadPersonsFromSQLite(); // Replace this with your own method to load data from SQLite





        // Load data from JSON APIs Request and insert into sqlite database
        loadDataFromJsonApi();
        //refreshRecyclerView();
        //sleep

        // Load data App directly and populate the RecyclerView
        //personList = loadPersonsFromList(); // Replace this with your own method to load data from SQLite


        // WHY can't load straight away to populate to recyclerview?
        personList = loadPersonsFromSQLite();
        //refreshRecyclerView();
        for (Person person : personList) {
            Log.i("person.getName", person.getName());
            Log.i("person.getName", String.valueOf(person.getAge()));
            Log.i("person.getName", person.getGender());
        }

        // Create and set the adapter
        personAdapter = new PersonAdapter(personList, getContext());
        recyclerView.setAdapter(personAdapter);

        refreshRecyclerView();

        // Call the printAllPersons() method to print out the updated data in the database
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        sqLiteHelper.printAllPersons();

        //personAdapter.notifyDataSetChanged()

        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        refreshBtn = rootView.findViewById(R.id.refreshButton);



        // Set click listener for the submit button

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (v.getId() == R.id.btnSubmit) {
                    // Get input data from EditText views
                    EditText etName = rootView.findViewById(R.id.etName);
                    EditText etAge = rootView.findViewById(R.id.etAge);
                    RadioGroup rgGender = rootView.findViewById(R.id.rgGender);

                    String name = etName.getText().toString().trim();
                    String ageStr = etAge.getText().toString().trim();
                    String gender = "";

                    // Validate name
                    if (name.isEmpty()) {
                        etName.setError("Please enter a name");
                        error = true;
                        //return;
                    }

                    // Validate age
                    int age = 0;
                    try {
                        age = Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        etAge.setError("Please enter a valid age");
                        //return;
                    }

                    // Validate gender
                    if (rgGender.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
                        //return;
                    } else {
                        gender = rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "Male" : "Female";
                    }
                    if (error)
                        return;

                    // Insert data into SQLite
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                    sqLiteHelper.insertPerson(name, age, gender);

                    // Refresh RecyclerView
                    refreshRecyclerView();
                }
            }
        });



        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and set the adapter
                personList = loadPersonsFromSQLite();
                personAdapter = new PersonAdapter(personList, getContext());
                recyclerView.setAdapter(personAdapter);

                personAdapter.notifyDataSetChanged();
            }
        });







        return rootView;
    }

    private List<Person> loadPersonsFromList() {
        // Replace this with your own implementation to fetch data from SQLite local storage
        // and create a list of Person objects
        List<Person> persons = new ArrayList<>();
        // Example data for demonstration
        persons.add(new Person(1, "John", 25, "Male"));
        persons.add(new Person(2, "Alice", 30, "Female"));
        persons.add(new Person(3, "Bob", 22, "Male"));
        persons.add(new Person(4, "Eve", 28, "Female"));
        return persons;
    }

    private List<Person> loadPersonsFromSQLite() {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());

        List<Person> persons = sqLiteHelper.getAllPersons();

        return persons;
    }



    private void refreshRecyclerView() {
        // Load data from SQLite and update RecyclerView
        personList = loadPersonsFromSQLite();
        personAdapter.setData(personList); // assuming you have a method to update data in your PersonAdapter
        personAdapter.notifyDataSetChanged();
    }


    // Load data from JSON APIs Request and populate the RecyclerView
    private void loadDataFromJsonApi() {
        //String url = "https://example.com/file.json"; // Replace with the URL of your .json file
        String url = "https://cagoh.github.io/data/JSON/persons.json"; // Replace with the URL of your .json file
        //https://cagoh.github.io/data/JSON/persons.json
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle JSON response using JSONHelper class
                        try {
                            JSONArray jsonArray = JSONHelper.getJSONArray(response, "data"); // Replace with the key of your JSON array
                            if (jsonArray != null) {
                                // Parse JSON data and update UI
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String itemName = JSONHelper.getString(jsonObject, "name"); // Replace with the key of the item name
                                    int itemAge = JSONHelper.getInt(jsonObject, "age"); // Replace with the key of the item age
                                    String itemGender = JSONHelper.getString(jsonObject, "gender"); // Replace with the key of the item gender

//                                    // Update your UI with the parsed data
//                                    // For example, you can display the parsed data in a TextView
//                                    TextView textView = new TextView(MainActivity.this);
//                                    textView.setText("Name: " + itemName + "\nAge: " + itemAge + "\nGender: " + itemGender);
//                                    // Add the TextView to your UI
//                                    // ...

                                    Log.i("itemName", itemName);
                                    Log.i("itemAge", String.valueOf(itemAge));
                                    Log.i("itemGender", itemGender);


                                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                                    sqLiteHelper.insertPerson( itemName, itemAge, itemGender);

                                    //(new PersonAdapter()).notifyDataSetChanged();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}

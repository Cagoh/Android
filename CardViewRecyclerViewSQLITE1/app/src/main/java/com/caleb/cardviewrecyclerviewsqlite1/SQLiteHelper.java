package com.caleb.cardviewrecyclerviewsqlite1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "persons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_GENDER + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    public void insertPerson(String name, int age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the data to be inserted
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);

        // Insert the data into the persons table
        db.insert(TABLE_NAME, null, values);

        // Close the database
        db.close();
    }

    public void removePerson(String name, int age, String gender) {
    //public void removePerson(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.i("position = ", String.valueOf(position));

        // Define the WHERE clause for the delete query
        String whereClause = COLUMN_NAME + " = ? AND " + COLUMN_AGE + " = ? AND " + COLUMN_GENDER + " = ?";
        //String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { name, String.valueOf(age), gender };
        //String[] whereArgs = { String.valueOf(position+1) };

        // Perform the delete operation
        int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);

        // Close the database
        db.close();

        if (rowsDeleted > 0) {
            // Rows deleted successfully
            // You can perform any additional actions here, if needed
        }
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch all rows from the persons table
        String selectAllQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        // Loop through the cursor and create Person objects
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                persons.add(new Person(id, name, age, gender));
            } while (cursor.moveToNext());
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return persons;
    }

    public void printAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve (null for all columns)
        String[] projection = null;

        // Define the selection (WHERE) clause and its arguments (null for all rows)
        String selection = null;
        String[] selectionArgs = null;

        // Define any other query parameters, such as groupBy, having, orderBy, etc. (null for default)
        String groupBy = null;
        String having = null;
        String orderBy = null;

        // Perform the query and obtain the Cursor object
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, groupBy, having, orderBy);

        // Iterate through the cursor and print out the results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));

                // Print out the retrieved values
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
                System.out.println("Id: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);



            } while (cursor.moveToNext());
        }

        // Close the cursor and the database
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

}

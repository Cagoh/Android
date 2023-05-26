package com.caleb.cardviewrecyclerviewsqlite1;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private static List<Person> personList;
    private Context context;

    public PersonAdapter(List<Person> personList, Context context) {

        this.personList = personList;
        this.context = context;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.tvName.setText("Name:  " + person.getName());
        holder.tvAge.setText("Age:    " + String.valueOf(person.getAge()));
        holder.tvGender.setText("Gender: " +person.getGender());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvGender;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvGender = itemView.findViewById(R.id.tvGender);
/*
            // Add an OnClickListener to the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the data associated with the clicked item
                        Person person = personList.get(position);
                        String name = person.getName();
                        int age = person.getAge();
                        String gender = person.getGender();

                        // Remove the item from the list
                        personList.remove(position);

                        // Notify the adapter that the item has been removed
                        notifyItemRemoved(position);

                        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);

                        // Call the removePerson() method with the data to delete the corresponding row from the database
                        sqLiteHelper.removePerson(name, age, gender);

                        // Call the printAllPersons() method to print out the updated data in the database
                        Log.i("Executing", "sqLiteHelper.printAllPersons()");
                        sqLiteHelper.printAllPersons();
                    }
                }
            });
 */

            // Add an OnClickListener to the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an AlertDialog.Builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Get the position of the clicked item
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        // Get the data associated with the clicked item
                                        Person person = personList.get(position);
                                        String name = person.getName();
                                        int age = person.getAge();
                                        String gender = person.getGender();

                                        // Remove the item from the list
                                        personList.remove(position);

                                        // Notify the adapter that the item has been removed
                                        notifyItemRemoved(position);

                                        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);

                                        // Call the removePerson() method with the data to delete the corresponding row from the database
                                        sqLiteHelper.removePerson(name, age, gender);

                                        // Call the printAllPersons() method to print out the updated data in the database
                                        Log.i("Executing", "sqLiteHelper.printAllPersons()");
                                        sqLiteHelper.printAllPersons();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, dismiss the dialog
                                    dialog.dismiss();
                                }
                            });

                    // Show the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }
    }

    public void setData(List<Person> updatedPersonList) {
        personList.clear();
        personList.addAll(updatedPersonList);
        notifyDataSetChanged();
    }
}

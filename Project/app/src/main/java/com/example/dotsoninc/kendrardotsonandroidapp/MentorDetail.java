package com.example.dotsoninc.kendrardotsonandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MentorDetail extends AppCompatActivity {

    private long courseId;
    private long mentorId;
    private final DBentity db = new DBentity(this);
    protected Intent phoneIntent;
    protected Intent emailIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent secondIntent = getIntent( );
        phoneIntent = new Intent(this, Phone.class);
        emailIntent = new Intent(this, Email.class);

        courseId = secondIntent.getLongExtra("CourseID",0);

        mentorId = secondIntent.getLongExtra("MentorID",0);

        if (mentorId == 0){
            setVisibleFields(mentorId, true);
        }
        else {
            Mentor mentor = new Mentor();
            mentor = db.getMentor(mentorId);

            TextView setText = (TextView) findViewById(R.id.name);
            setText.setText(mentor.getName());

            setVisibleFields(mentorId, false);
        }

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMentor();
            }
        });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMentor();
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMentor();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMentor();
            }
        });

        Button emailButton = (Button) findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmail();
            }
        });

        Button phoneButton = (Button) findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhone();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id){

            case R.id.go_home:
                intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.go_back:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVisibleFields(long id, boolean save){

        Group viewCourse=(Group)findViewById(R.id.viewCourse);
        Group editCourse=(Group)findViewById(R.id.editCourse);

        Button saveButton = (Button)findViewById(R.id.saveButton);
        Button updateButton = (Button)findViewById(R.id.updateButton);
        Button editButton = (Button)findViewById(R.id.editButton);
        Button emailButton = (Button)findViewById(R.id.emailButton);
        Button phoneButton = (Button)findViewById(R.id.phoneButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);

        if (id == 0){//set editable fields visible
            viewCourse.setVisibility(View.INVISIBLE);
            editCourse.setVisibility(View.VISIBLE);

            if (save) {
                saveButton.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.INVISIBLE);}
            else {
                saveButton.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
            }

            editButton.setVisibility((View.GONE));
            emailButton.setVisibility(View.GONE);
            phoneButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

        }
        else//set editable fields invisible
        {
            viewCourse.setVisibility(View.VISIBLE);
            editCourse.setVisibility(View.INVISIBLE);

            editButton.setVisibility((View.VISIBLE));
            emailButton.setVisibility(View.VISIBLE);
            phoneButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);
            saveButton.setVisibility((View.GONE));        }
    }

    public void editMentor(){
        //retrieve current values from term field
        TextView value = (TextView)findViewById(R.id.name);
        String name = value.getText().toString();

        //set edit fields visible
        setVisibleFields(0, false);

        //set edit fields to term values ready to be edited
        EditText editValue = (EditText)findViewById(R.id.edit_name);
        editValue.setText(name, TextView.BufferType.EDITABLE);

    }

    public void updateMentor(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_name);
        String name = editField.getText().toString();

        //update the database with new values
        db.updateMentor(mentorId, name);

        //set term fields to visible
        setVisibleFields(1, false);

        //set term fields to new edited values
        TextView saveValue = (TextView)findViewById(R.id.name);
        saveValue.setText(name);

    }

    public void saveMentor(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_name);
        String name = editField.getText().toString();

        //add the new mentor
        mentorId = db.addMentor(courseId, name);

        //set edit fields not visible
        setVisibleFields(1, true);

        //set term fields to new values
        TextView termField = (TextView)findViewById(R.id.name);
        termField.setText(name);
    }

    public void deleteMentor(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.DeleteQuestion));

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.deleteMentor(mentorId);

                //set field to blanks
                TextView termField = (TextView)findViewById(R.id.name);
                termField.setText(getResources().getString(R.string.DeleteMsg));

            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }


    public void openEmail(){

        emailIntent.putExtra(Email.EMAIL_COLUMN_MENTOR_ID, mentorId);
        startActivity(emailIntent);

    }

    public void openPhone(){

        phoneIntent.putExtra(Phone.PHONE_COLUMN_MENTOR_ID, mentorId);
        startActivity(phoneIntent);

    }

}

package com.example.dotsoninc.kendrardotsonandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import java.util.Arrays;


public class CourseDetail extends AppCompatActivity {

    private long courseId;
    private long termId;
    private long noteId;
    private final DBentity db = new DBentity(this);
    private Spinner spinner;
    protected Intent mentorIntent;
    private Intent noteIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent secondIntent = getIntent( );
        mentorIntent = new Intent(this, Mentor.class);
        noteIntent = new Intent(this, Note.class);

        courseId = secondIntent.getLongExtra("CourseID",0);

        termId = secondIntent.getLongExtra("TermID",0);

        if (courseId == 0){
            setVisibleFields(courseId, true);
        }
        else {
            String msg;
            Course course = new Course();
            course = db.getCourse(courseId);

            if (course.getStartAlert() == 1){
                msg = "The start date for this course is " + course.getStartDate();
                setAlert(msg);
            }

            if (course.getEndAlert() == 1){
                msg = "The end date for this course is " + course.getStartDate();
                setAlert(msg);
            }
            TextView setText = (TextView) findViewById(R.id.title);
            setText.setText(course.getName());

            setText = (TextView) findViewById(R.id.start_date);
            String label = course.getStartDate();
            setText.setText(label);

            setText = (TextView) findViewById(R.id.end_date);
            label = course.getEndDate();
            setText.setText(label);

            setText = (TextView)findViewById(R.id.status);
            label = course.getStatus();
            setText.setText(label);

            setText = (TextView)findViewById(R.id.share);
            label = course.getShareType();
            setText.setText(label);

            setText = (TextView) findViewById(R.id.start_alert_status);
            int alert = course.getStartAlert();
            if (alert == 1){label = getResources().getString(R.string.On);}
            else{label = getResources().getString(R.string.Off);};
            setText.setText(label);

            setText = (TextView) findViewById(R.id.end_alert_status);
            alert = course.getEndAlert();
            if (alert == 1){label = getResources().getString(R.string.On);}
            else{label = getResources().getString(R.string.Off);};
            setText.setText(label);

            setVisibleFields(courseId, false);

        }

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }
        });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCourse();
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCourse();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }
        });

        Button mentorButton = (Button) findViewById(R.id.mentorButton);
        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMentor();
            }
        });

        Button noteButton = (Button) findViewById(R.id.noteButton);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNote();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        Button mentorButton = (Button)findViewById(R.id.mentorButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button noteButton = (Button)findViewById(R.id.noteButton);

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
            mentorButton.setVisibility(View.GONE);
            noteButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

        }
        else//set editable fields invisible
        {
            viewCourse.setVisibility(View.VISIBLE);
            editCourse.setVisibility(View.INVISIBLE);

            editButton.setVisibility((View.VISIBLE));
            mentorButton.setVisibility(View.VISIBLE);
            noteButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);
            saveButton.setVisibility((View.GONE));        }
    }

    public void editCourse(){
        //retrieve current values from term field
        TextView value = (TextView)findViewById(R.id.title);
        String courseValue = value.getText().toString();
        value = (TextView)findViewById(R.id.start_date);
        String startValue = value.getText().toString();
        value = (TextView)findViewById(R.id.end_date);
        String endValue = value.getText().toString();
        value = (TextView)findViewById(R.id.status);
        String status = value.getText().toString();
        value = (TextView)findViewById(R.id.share);
        String share = value.getText().toString();
        value = (TextView)findViewById(R.id.start_alert_status);
        String startAlert = value.getText().toString();
        value = (TextView)findViewById(R.id.end_alert_status);
        String endAlert = value.getText().toString();

        //set edit fields visible
        setVisibleFields(0, false);

        //set edit fields to term values ready to be edited
        EditText editValue = (EditText)findViewById(R.id.edit_title);
        editValue.setText(courseValue, TextView.BufferType.EDITABLE);
        editValue = (EditText)findViewById(R.id.edit_start_date);
        editValue.setText(startValue, TextView.BufferType.EDITABLE);
        editValue = (EditText)findViewById(R.id.edit_end_date);
        editValue.setText(endValue, TextView.BufferType.EDITABLE);
        spinner = (Spinner)findViewById(R.id.edit_status);
        int index = Arrays.asList((getResources().getStringArray(R.array.status_list))).indexOf(status);
        spinner.setSelection(index);
        spinner = (Spinner)findViewById(R.id.edit_share);
        index = Arrays.asList((getResources().getStringArray(R.array.share_list))).indexOf(share);
        spinner.setSelection(index);
        CheckBox alertValue = (CheckBox) findViewById(R.id.edit_start_alert);
        alertValue.setChecked(false);
        if (startAlert.equals("On")){
            alertValue.setChecked(true);
        }
        alertValue = (CheckBox) findViewById(R.id.edit_end_alert);
        alertValue.setChecked(false);
        if (endAlert.equals("On")){
            alertValue.setChecked(true);
        }
    }

    public void updateCourse(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_title);
        String courseValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_start_date);
        String startValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_end_date);
        String endValue = editField.getText().toString();
        if (db.isValidDate(startValue) && db.isValidDate(endValue)) {

            spinner = (Spinner) findViewById(R.id.edit_status);
            String statusValue = spinner.getSelectedItem().toString();
            spinner = (Spinner) findViewById(R.id.edit_share);
            String shareValue = spinner.getSelectedItem().toString();

            int startAlertValue = 0;
            int endAlertValue = 0;
            CheckBox alertValue = (CheckBox) findViewById(R.id.edit_start_alert);
            if (alertValue.isChecked()) {
                startAlertValue = 1;
            }
            alertValue = (CheckBox) findViewById(R.id.edit_end_alert);
            if (alertValue.isChecked()) {
                endAlertValue = 1;
            }

            //update the database with new values
            db.updateCourse(courseId, courseValue, statusValue, shareValue, startValue, endValue, startAlertValue, endAlertValue);

            //set term fields to visible
            setVisibleFields(1, false);

            //set term fields to new edited values
            TextView saveValue = (TextView) findViewById(R.id.title);
            saveValue.setText(courseValue);
            saveValue = (TextView) findViewById(R.id.start_date);
            saveValue.setText(startValue);
            saveValue = (TextView) findViewById(R.id.end_date);
            saveValue.setText(endValue);

            saveValue = (TextView) findViewById(R.id.status);
            saveValue.setText(statusValue);
            saveValue = (TextView) findViewById(R.id.share);
            saveValue.setText(shareValue);
            saveValue = (TextView) findViewById(R.id.end_alert_status);
            if (startAlertValue == 1) {
                saveValue.setText(getResources().getString(R.string.On));
            } else {
                saveValue.setText(getResources().getString(R.string.Off));
            }
            saveValue = (TextView) findViewById(R.id.start_alert_status);
            if (endAlertValue == 1) {
                saveValue.setText(getResources().getString(R.string.On));
            } else {
                saveValue.setText(getResources().getString(R.string.Off));
            }
        }
        else{//an invalid date was entered
            setAlert(getResources().getString(R.string.invalid_date));
        }
    }

    public void saveCourse(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_title);
        String courseValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_start_date);
        String startValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_end_date);
        String endValue = editField.getText().toString();
        if (db.isValidDate(startValue) && db.isValidDate(endValue)) {

            spinner = (Spinner) findViewById(R.id.edit_status);
            String statusValue = spinner.getSelectedItem().toString();
            spinner = (Spinner) findViewById(R.id.edit_share);
            String shareValue = spinner.getSelectedItem().toString();

            int startAlertValue = 0;
            int endAlertValue = 0;
            CheckBox alertValue = (CheckBox) findViewById(R.id.edit_start_alert);
            if (alertValue.isChecked()) {
                startAlertValue = 1;
            }
            alertValue = (CheckBox) findViewById(R.id.edit_end_alert);
            if (alertValue.isChecked()) {
                endAlertValue = 1;
            }


            //add the new course
            courseId = db.addCourse(termId, courseValue, statusValue, shareValue, startValue, endValue, startAlertValue, endAlertValue);

            //set edit fields not visible
            setVisibleFields(1, true);

            //set term fields to new values
            TextView termField = (TextView) findViewById(R.id.title);
            termField.setText(courseValue);
            termField = (TextView) findViewById(R.id.start_date);
            termField.setText(startValue);
            termField = (TextView) findViewById(R.id.end_date);
            termField.setText(endValue);
            termField = (TextView) findViewById(R.id.status);
            termField.setText(statusValue);
            termField = (TextView) findViewById(R.id.share);
            termField.setText(shareValue);
            termField = (TextView) findViewById(R.id.end_alert_status);
            if (alertValue.isChecked()) {
                termField.setText(getResources().getString(R.string.On));
            } else {
                termField.setText(getResources().getString(R.string.Off));
            }
            termField = (TextView) findViewById(R.id.start_alert_status);
            if (alertValue.isChecked()) {
                termField.setText(getResources().getString(R.string.On));
            } else {
                termField.setText(getResources().getString(R.string.Off));
            }
        }
        else{//an invalid date was entered
            setAlert(getResources().getString(R.string.invalid_date));
        }
    }

    public void deleteCourse(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.DeleteQuestion));

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.deleteCourse(courseId);

                //set fields to blanks
                TextView termField = (TextView)findViewById(R.id.title);
                termField.setText(getResources().getString(R.string.DeleteMsg));
                termField.setTextColor(getResources().getColor(R.color.colorAccent));
                termField = (TextView)findViewById(R.id.start_date);
                termField.setText("");
                termField = (TextView)findViewById(R.id.end_date);
                termField.setText("");
                termField = (TextView)findViewById(R.id.status);
                termField.setText("");
                termField = (TextView)findViewById(R.id.share);
                termField.setText("");
                termField = (TextView)findViewById(R.id.end_alert_status);
                termField.setText("");
                termField = (TextView)findViewById(R.id.start_alert_status);
                termField.setText("");
                termField = (TextView)findViewById(R.id.start_date_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.end_date_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.status_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.share_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.end_alert_status_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.start_alert_status_label);
                termField.setText("");
            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setAlert(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

    public void openMentor(){

        mentorIntent.putExtra(Mentor.MENTOR_COLUMN_COURSE_ID, courseId);
        startActivity(mentorIntent);

    }

    public void openNote(){

        noteIntent.putExtra(Note.NOTE_COLUMN_COURSE_ID, courseId);
        startActivity(noteIntent);

    }


}

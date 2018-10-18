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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;


public class AssessmentDetail extends AppCompatActivity {

    private long courseId;
    private long assessId;
    private final DBentity db = new DBentity(this);
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent secondIntent = getIntent( );

        courseId = secondIntent.getLongExtra("CourseID",0);

        assessId = secondIntent.getLongExtra("AssessID",0);

        if (assessId == 0){
            setVisibleFields(assessId, true);
        }
        else {
            Assessment assess = new Assessment();
            assess = db.getAssess(assessId);

            if (assess.getGoalAlert() == 1){
                String msg = getResources().getString(R.string.due_date_msg) + " " + assess.getDueDate();
                setAlert(msg);
            }

            TextView setText = (TextView) findViewById(R.id.title);
            setText.setText(assess.getName());

            setText = (TextView) findViewById(R.id.type);
            String label = assess.getType();
            setText.setText(label);

            setText = (TextView) findViewById(R.id.due_date);
            label = assess.getDueDate();
            setText.setText(label);

           setText = (TextView) findViewById(R.id.goal_alert_status);
            int alert = assess.getGoalAlert();
            if (alert == 1){label = getResources().getString(R.string.On);}
            else{label = getResources().getString(R.string.Off);};
            setText.setText(label);


            setVisibleFields(assessId, false);

        }

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAssess();
            }
        });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAssess();
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAssess();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAssess();
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

        Group viewAssess=(Group)findViewById(R.id.viewAssess);
        Group editAssess=(Group)findViewById(R.id.editAssess);

        Button saveButton = (Button)findViewById(R.id.saveButton);
        Button updateButton = (Button)findViewById(R.id.updateButton);
        Button editButton = (Button)findViewById(R.id.editButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);

        if (id == 0){//set editable fields visible
            viewAssess.setVisibility(View.INVISIBLE);
            editAssess.setVisibility(View.VISIBLE);

            if (save) {
                saveButton.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.INVISIBLE);}
            else {
                saveButton.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
            }

            editButton.setVisibility((View.GONE));
            deleteButton.setVisibility(View.GONE);

        }
        else//set editable fields invisible
        {
            viewAssess.setVisibility(View.VISIBLE);
            editAssess.setVisibility(View.INVISIBLE);

            editButton.setVisibility((View.VISIBLE));
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);
            saveButton.setVisibility((View.GONE));        }
    }

    public void editAssess(){
        //retrieve current values from term field
        TextView value = (TextView)findViewById(R.id.title);
        String assessValue = value.getText().toString();

        value = (TextView)findViewById(R.id.type);
        String typeValue = value.getText().toString();

        value = (TextView)findViewById(R.id.due_date);
        String dueValue = value.getText().toString();

        value = (TextView)findViewById(R.id.goal_alert_status);
        String goalAlert = value.getText().toString();

        //set edit fields visible
        setVisibleFields(0, false);

        //set edit fields to term values ready to be edited
        EditText editValue = (EditText)findViewById(R.id.edit_title);
        editValue.setText(assessValue, TextView.BufferType.EDITABLE);

        spinner = (Spinner)findViewById(R.id.edit_type);
        int index = Arrays.asList((getResources().getStringArray(R.array.assess_type))).indexOf(typeValue);
        spinner.setSelection(index);

        editValue = (EditText)findViewById(R.id.edit_due_date);
        editValue.setText(dueValue, TextView.BufferType.EDITABLE);

        CheckBox alertValue = (CheckBox) findViewById(R.id.edit_goal_alert);
        alertValue.setChecked(false);
        if (goalAlert.equals("On")){
            alertValue.setChecked(true);
        }

    }

    public void updateAssess(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_title);
        String courseValue = editField.getText().toString();

        spinner = (Spinner)findViewById(R.id.edit_type);
        String typeValue = spinner.getSelectedItem().toString();

        editField = (EditText)findViewById(R.id.edit_due_date);
        String dueValue = editField.getText().toString();

        //check to make sure editField is a valid date
        if (db.isValidDate(dueValue)){

            //dueValue = db.convertStringDate(dueValue);

            int goalAlertValue = 0;

            CheckBox alertValue = (CheckBox) findViewById(R.id.edit_goal_alert);
            if (alertValue.isChecked()){
                goalAlertValue = 1;
            }


            //update the database with new values
            db.updateAssess(assessId, courseId, courseValue, typeValue, dueValue, goalAlertValue);

            //set term fields to visible
            setVisibleFields(1, false);

            //set term fields to new edited values
            TextView saveValue = (TextView)findViewById(R.id.title);
            saveValue.setText(courseValue);

            saveValue = (TextView)findViewById(R.id.type);
            saveValue.setText(typeValue);

            saveValue = (TextView)findViewById(R.id.due_date);
            saveValue.setText(dueValue);

            saveValue = (TextView)findViewById(R.id.goal_alert_status);
            if (goalAlertValue == 1){
                saveValue.setText(getResources().getString(R.string.On));
            }else{
                saveValue.setText(getResources().getString(R.string.Off));
            }
        }
        else{//an invalid date was entered
            setAlert(getResources().getString(R.string.invalid_date));
        }
    }

    public void saveAssess(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_title);
        String assessValue = editField.getText().toString();

        spinner = (Spinner)findViewById(R.id.edit_type);
        String typeValue = spinner.getSelectedItem().toString();

        editField = (EditText)findViewById(R.id.edit_due_date);
        String dueValue = editField.getText().toString();

        if (db.isValidDate(dueValue)) {

            int goalAlertValue = 0;

            CheckBox alertValue = (CheckBox) findViewById(R.id.edit_goal_alert);
            if (alertValue.isChecked()) {
                goalAlertValue = 1;
            }


            //add the new course
            assessId = db.addAssess(courseId, assessValue, dueValue, typeValue, goalAlertValue);

            //set edit fields not visible
            setVisibleFields(1, true);

            //set term fields to new values
            TextView saveValue = (TextView) findViewById(R.id.title);
            saveValue.setText(assessValue);
            saveValue = (TextView) findViewById(R.id.type);
            saveValue.setText(typeValue);
            saveValue = (TextView) findViewById(R.id.due_date);
            saveValue.setText(dueValue);

            saveValue = (TextView) findViewById(R.id.goal_alert_status);
            if (goalAlertValue == 1) {
                saveValue.setText(getResources().getString(R.string.On));
            } else {
                saveValue.setText(getResources().getString(R.string.Off));
            }
        }
        else{//an invalid date was entered
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_date), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAssess(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.DeleteQuestion));

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                db.deleteAssess(assessId);

                //set fields to blanks
                TextView termField = (TextView)findViewById(R.id.title);
                termField.setText(getResources().getString(R.string.DeleteMsg));
                termField.setTextColor(getResources().getColor(R.color.colorAccent));
                termField = (TextView)findViewById(R.id.type);
                termField.setText("");
                termField = (TextView)findViewById(R.id.due_date);
                termField.setText("");
                termField = (TextView)findViewById(R.id.goal_alert_status);
                termField.setText("");
                termField = (TextView)findViewById(R.id.type_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.due_date_label);
                termField.setText("");
                termField = (TextView)findViewById(R.id.goal_alert_status_label);
                termField.setText("");

            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setAlert(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}

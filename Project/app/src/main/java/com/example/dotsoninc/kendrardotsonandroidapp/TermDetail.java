package com.example.dotsoninc.kendrardotsonandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class TermDetail extends AppCompatActivity {

    private Long termId;
    private DBentity db = new DBentity(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent secondIntent = getIntent( );

        termId = secondIntent.getLongExtra(Term.TERM_COLUMN_ID,0);

        if (termId == 0){
            setVisibleFields(termId, true);
        }
        else {
            Term term = new Term(db.getTerm(termId));

            TextView setText = (TextView) findViewById(R.id.title);
            setText.setText(term.getName());

            setText = (TextView) findViewById(R.id.start_date);
            String label = term.getStartDate();
            setText.setText(label);

            setText = (TextView) findViewById(R.id.end_date);
            label = term.getEndDate();
            setText.setText(label);

            setVisibleFields(termId, false);

        }

        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTerm();
                }
            });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateTerm();
                }
            });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveTerm();
                }
            });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTerm();
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

        Group viewTerm=(Group)findViewById(R.id.viewTerm);
        Group editTerm=(Group)findViewById(R.id.editTerm);

        Button saveButton = (Button)findViewById(R.id.saveButton);
        Button updateButton = (Button)findViewById(R.id.updateButton);
        Button editButton = (Button)findViewById(R.id.editButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);

        if (id == 0){//set editable fields visible
            viewTerm.setVisibility(View.INVISIBLE);
            editTerm.setVisibility(View.VISIBLE);

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
            viewTerm.setVisibility(View.VISIBLE);
            editTerm.setVisibility(View.INVISIBLE);

            editButton.setVisibility((View.VISIBLE));
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);
            saveButton.setVisibility((View.GONE));
        }
    }

    public void editTerm(){
        //retrieve current values from term field
        TextView value = (TextView)findViewById(R.id.title);
        String termValue = value.getText().toString();
        value = (TextView)findViewById(R.id.start_date);
        String startValue = value.getText().toString();
        value = (TextView)findViewById(R.id.end_date);
        String endValue = value.getText().toString();


        //set edit fields visible
        setVisibleFields(0, false);

        //set edit fields to term values ready to be edited
        EditText editValue = (EditText)findViewById(R.id.edit_title);
        editValue.setText(termValue, TextView.BufferType.EDITABLE);
        editValue = (EditText)findViewById(R.id.edit_start_date);
        editValue.setText(startValue, TextView.BufferType.EDITABLE);
        editValue = (EditText)findViewById(R.id.edit_end_date);
        editValue.setText(endValue, TextView.BufferType.EDITABLE);
    }

    public void updateTerm(){
        //retrieve edited fields
        EditText value = (EditText)findViewById(R.id.edit_title);
        String termValue = value.getText().toString();
        value = (EditText)findViewById(R.id.edit_start_date);
        String startValue = value.getText().toString();
        value = (EditText)findViewById(R.id.edit_end_date);
        String endValue = value.getText().toString();

        if (db.isValidDate(startValue) && db.isValidDate(endValue)) {

            //update the database with new values
            db.updateTerm(termId, termValue, startValue, endValue);

            //set term fields to visible
            setVisibleFields(1, false);

            //set term fields to new edited values
            TextView saveValue = (TextView) findViewById(R.id.title);
            saveValue.setText(termValue);
            saveValue = (TextView) findViewById(R.id.start_date);
            saveValue.setText(startValue);
            saveValue = (TextView) findViewById(R.id.end_date);
            saveValue.setText(endValue);
        }
        else{//an invalid date was entered
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_date), Toast.LENGTH_LONG).show();
        }
    }

    public void saveTerm(){
        //retrieve edited fields
        EditText editField = (EditText)findViewById(R.id.edit_title);
        String termValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_start_date);
        String startValue = editField.getText().toString();
        editField = (EditText)findViewById(R.id.edit_end_date);
        String endValue = editField.getText().toString();

        if (db.isValidDate(startValue) && db.isValidDate(endValue)) {

            //add the new term
            termId = db.addTerm(termValue, startValue, endValue);

            //set edit fields not visible
            setVisibleFields(1, true);

            //set term fields to new values
            TextView termField = (TextView) findViewById(R.id.title);
            termField.setText(termValue);
            termField = (TextView) findViewById(R.id.start_date);
            termField.setText(startValue);
            termField = (TextView) findViewById(R.id.end_date);
            termField.setText(endValue);
        }
        else{//an invalid date was entered
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_date), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTerm(){

        Cursor cursor = db.getAllCourse(termId);
        if (cursor.getCount() > 0){
            Toast.makeText(getApplicationContext(),R.string.deleteTerm, Toast.LENGTH_LONG).show();

        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.DeleteQuestion));

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    //put message box here verifying delete request
                    db.deleteTerm(termId);

                    //set fields to blanks
                    TextView termField = (TextView) findViewById(R.id.title);
                    termField.setText(getResources().getString(R.string.DeleteMsg));
                    termField = (TextView) findViewById(R.id.start_date);
                    termField.setText("");
                    termField = (TextView) findViewById(R.id.end_date);
                    termField.setText("");

                }
            });


            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}

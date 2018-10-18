package com.example.dotsoninc.kendrardotsonandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Phone extends AppCompatActivity{
    private long id;
    private long mentorId;
    private String phone;
    private ListAdapter myAdapter;
    private ListView list;
    private Cursor dataset;
    private DBentity db = new DBentity(this);


    public static final String PHONE_TABLE_NAME = "phone";
    public static final String PHONE_COLUMN_ID = "_id";
    public static final String PHONE_COLUMN_MENTOR_ID = "mentorID";
    public static final String PHONE_COLUMN_NUMBER = "phoneNo";

    public static final String CREATE_PHONE = "CREATE TABLE IF NOT EXISTS " + PHONE_TABLE_NAME +
            " (" + PHONE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            PHONE_COLUMN_MENTOR_ID + " INTEGER DEFAULT 0, " +
            PHONE_COLUMN_NUMBER + " TEXT)";

    public Phone(){

    }

    public Phone(long id, long mentorId, String phone){
        this.id = 0;
        this.mentorId = mentorId;
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Intent secondIntent = getIntent( );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.phone_list_view);


        mentorId = secondIntent.getLongExtra(Phone.PHONE_COLUMN_MENTOR_ID,0);

        if (mentorId == 0){

        }
        else {

            dataset = db.getAllPhone(mentorId);
            startManagingCursor(dataset);

            String[] columns = new String[]{Phone.PHONE_COLUMN_NUMBER};
            int[] rows = new int[]{R.id.phone};

            myAdapter = new SimpleCursorAdapter(this, R.layout.content_phone, dataset, columns, rows);

            list.setAdapter(myAdapter);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_add, menu);
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

            case R.id.add:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.addRecord));


                final EditText input = new EditText(this);
                builder.setView(input);

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

                builder.setPositiveButton(getResources().getString(R.string.action_add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        phone = input.getText().toString();
                        db.addPhone(mentorId, phone);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMentorId() {
        return mentorId;
    }

    public void setMentorId(long mentorId) {
        this.mentorId = mentorId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void deletePhone(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.DeleteQuestion));

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        final View parent = (View) v.getParent();

        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final int pos = list.getPositionForView(parent);
                dataset.moveToPosition(pos);

                long phoneId =  dataset.getLong(dataset.getColumnIndex(Phone.PHONE_COLUMN_ID));
                db.deletePhone( phoneId);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }
}

package com.example.dotsoninc.kendrardotsonandroidapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import java.util.ArrayList;

public class Mentor extends AppCompatActivity{
    private long id;
    private Intent mentorDetailIntent;
    private Intent emailIntent;
    private Intent phoneIntent;
    private ListView list;
    private Cursor dataset;
    private long courseId;
    private long mentorId;
    private String name;
    private ArrayList<Phone> phone;
    private ArrayList<Email> email;
    private DBentity db = new DBentity(this);

    public static final String MENTOR_TABLE_NAME = "mentor";
    public static final String MENTOR_COLUMN_ID = "_id";
    public static final String MENTOR_COLUMN_NAME = "name";
    public static final String MENTOR_COLUMN_COURSE_ID = "courseID";

    public static final String CREATE_MENTOR = "CREATE TABLE IF NOT EXISTS " + MENTOR_TABLE_NAME +
            " (" + MENTOR_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            MENTOR_COLUMN_NAME + " TEXT, " +
            MENTOR_COLUMN_COURSE_ID + " INTEGER DEFAULT 0)";


    public Mentor(){

    }

    public Mentor(long id, long courseId, String name){
        this.id = id;
        this.courseId = courseId;
        this.name = name;

    }


    public Mentor(long id, long courseId, String name, ArrayList<Email> emails, ArrayList<Phone> phones){
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.email = emails;
        this.phone = phones;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);
        Intent secondIntent = getIntent( );
        mentorDetailIntent = new Intent(this, MentorDetail.class);
        emailIntent = new Intent(this, Email.class);
        phoneIntent = new Intent(this, Phone.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.mentor_list_view);


        courseId = secondIntent.getLongExtra(Mentor.MENTOR_COLUMN_COURSE_ID,0);

        if (courseId == 0){

        }
        else {

            dataset = db.getAllMentor(courseId);
            startManagingCursor(dataset);

            String[] columns = new String[]{Mentor.MENTOR_COLUMN_NAME};
            int[] rows = new int[]{R.id.name};

            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.content_mentor, dataset, columns, rows);

            list.setAdapter(myAdapter);

            AdapterView.OnItemClickListener listclick = new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    mentorDetailIntent.putExtra("MentorID", id);
                    mentorDetailIntent.putExtra("CourseID", courseId);
                    startActivity(mentorDetailIntent);
                }
            };

            list.setOnItemClickListener(listclick);

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
                mentorDetailIntent.putExtra("CourseID", courseId);
                mentorDetailIntent.putExtra("MentorID", 0);
                startActivity(mentorDetailIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getCourseId() {return courseId; }

    public void setCourseId(long courseId) {this.courseId = courseId; }

    public long getMentorId() { return mentorId; }

    public void setMentorId(long mentorId) {
        this.mentorId = mentorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Phone> getPhone() {
        return phone;
    }

    public void setEmail(Cursor email) {
        ArrayList<Email> list = new ArrayList<Email>();
        Email emailObj = new Email();
        while(email.moveToNext()){
            emailObj = new Email(email.getLong(email.getColumnIndex(Email.EMAIL_COLUMN_ID)), email.getLong(email.getColumnIndex(Email.EMAIL_COLUMN_MENTOR_ID)), email.getString(email.getColumnIndex(Email.EMAIL_COLUMN_ADDRESS))  );
            list.add(emailObj);
        }
        this.email = list;
    }

    public void setPhone(Cursor phone) {
        ArrayList<Phone> list = new ArrayList<Phone>();
        Phone emailObj = new Phone();
        while(phone.moveToNext()){
            emailObj = new Phone(phone.getLong(phone.getColumnIndex(Phone.PHONE_COLUMN_ID)), phone.getLong(phone.getColumnIndex(Phone.PHONE_COLUMN_MENTOR_ID)), phone.getString(phone.getColumnIndex(Phone.PHONE_COLUMN_NUMBER))  );
            list.add(emailObj);
        }
        this.phone = list;
    }

    public ArrayList<Email> getEmail(){
        return email;
    }


    public void openEmail(View v){
        View parent = (View) v.getParent();

        final int pos = list.getPositionForView(parent);
        dataset.moveToPosition(pos);

        emailIntent.putExtra(Email.EMAIL_COLUMN_MENTOR_ID, dataset.getLong(dataset.getColumnIndex(Mentor.MENTOR_COLUMN_ID)));
        startActivity(emailIntent);
    }

    public void openPhone(View v){
        View parent = (View) v.getParent();

        final int pos = list.getPositionForView(parent);
        dataset.moveToPosition(pos);

        phoneIntent.putExtra(Phone.PHONE_COLUMN_MENTOR_ID, dataset.getLong(dataset.getColumnIndex(Mentor.MENTOR_COLUMN_ID)));
        startActivity(phoneIntent);
    }


}

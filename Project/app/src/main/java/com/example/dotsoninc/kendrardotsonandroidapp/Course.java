package com.example.dotsoninc.kendrardotsonandroidapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.ArrayList;



public class Course extends AppCompatActivity {

    private long id;
    private long termId;
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String shareType;
    private int startAlert;
    private int endAlert;
    private ListAdapter myAdapter;
    private Intent courseDetailIntent;
    private Intent assessIntent;
    private ListView list;
    private Cursor dataset;
    private DBentity db = new DBentity(this);

    public static final String COURSE_TABLE_NAME = "course";
    public static final String COURSE_COLUMN_ID = "_id";
    public static final String COURSE_COLUMN_TERM_ID = "termID";
    public static final String COURSE_COLUMN_TITLE = "title";
    public static final String COURSE_COLUMN_START_DATE = "startDate";
    public static final String COURSE_COLUMN_END_DATE = "endDate";
    public static final String COURSE_COLUMN_STATUS = "status";
    public static final String COURSE_COLUMN_START_ALERT = "startAlert";
    public static final String COURSE_COLUMN_END_ALERT = "endAlert";
    public static final String COURSE_COLUMN_SHARE_TYPE = "shareType";



    public static final String CREATE_COURSE = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE_NAME +
            " (" + COURSE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COURSE_COLUMN_TERM_ID + " INTEGER, " +
            COURSE_COLUMN_TITLE + " TEXT, " +
            COURSE_COLUMN_START_DATE + " TEXT, " +
            COURSE_COLUMN_END_DATE + " TEXT, " +
            COURSE_COLUMN_STATUS + " TEXT, " +
            COURSE_COLUMN_SHARE_TYPE + " TEXT, " +
            COURSE_COLUMN_START_ALERT + " INTEGER DEFAULT 0, " + //0 = off
            COURSE_COLUMN_END_ALERT + " INTEGER DEFAULT 0)";  //0 = off



    public Course(){

    }

    public Course(long termId, String title, String startDate, String endDate, String status, String shareType){
        this.id = 0;
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.startAlert = 0;
        this.endAlert = 0;
        this.shareType = shareType;

    }

    public Course(long id, long termId, String title, String status, String shareType, String startDate, String endDate, int startAlert, int endAlert){
        this.id = id;
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.shareType = shareType;
        this.startAlert = startAlert;
        this.endAlert = endAlert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent secondIntent = getIntent( );
        assessIntent = new Intent(this, Assessment.class);
        courseDetailIntent = new Intent(this, CourseDetail.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.course_list_view);


        termId = secondIntent.getLongExtra(Term.TERM_COLUMN_ID,0);

        if (termId == 0){

        }
        else {


            dataset = db.getAllCourse(termId);
            startManagingCursor(dataset);

            String[] columns = new String[]{Course.COURSE_COLUMN_TITLE, Course.COURSE_COLUMN_START_DATE, Course.COURSE_COLUMN_END_DATE};
            int[] rows = new int[]{R.id.title, R.id.startDate, R.id.endDate};

            myAdapter = new SimpleCursorAdapter(this, R.layout.content_course, dataset, columns, rows);

            list.setAdapter(myAdapter);

            AdapterView.OnItemClickListener listclick = new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    courseDetailIntent.putExtra("CourseID", id);
                    courseDetailIntent.putExtra("TermID", termId);
                    startActivity(courseDetailIntent);
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
                courseDetailIntent.putExtra("CourseID", 0);
                courseDetailIntent.putExtra("TermID", termId);
                startActivity(courseDetailIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
    }

    public long getTermID() {
        return termId;
    }

    public void setTermID(int termID) {
        this.termId = termID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }



    public int getStartAlert() {
        return startAlert;
    }

    public void setStartAlert(int startAlert) {
        this.startAlert = startAlert;
    }

    public int getEndAlert() {
        return endAlert;
    }

    public void setEndAlert(int endAlert) {
        this.endAlert = endAlert;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }



    public void openAssess(View v){
        View parent = (View) v.getParent();
        ListView listview = (ListView)parent.getParent();

        final int pos = listview.getPositionForView(parent);
        dataset.moveToPosition(pos);

        assessIntent.putExtra("CourseID", dataset.getLong(dataset.getColumnIndex(Course.COURSE_COLUMN_ID)));
        startActivity(assessIntent);
    }
}

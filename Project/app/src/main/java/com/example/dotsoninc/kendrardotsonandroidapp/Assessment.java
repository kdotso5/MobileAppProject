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



public class Assessment extends AppCompatActivity {
    private long id;
    private long courseId;
    private String title;
    private String type;
    private String dueDate;
    private int goalAlert;
    private Intent assessDetailIntent;
    private ListAdapter myAdapter;
    private Cursor dataset;
    private ListView list;

    public static final String ASSESS_TABLE_NAME = "assess";
    public static final String ASSESS_COLUMN_ID = "_id";
    public static final String ASSESS_COLUMN_COURSE_ID = "courseID";
    public static final String ASSESS_COLUMN_TITLE = "title";
    public static final String ASSESS_COLUMN_TYPE = "type";
    public static final String ASSESS_COLUMN_DUE_DATE = "dueDate";
    public static final String ASSESS_COLUMN_GOAL_ALERT = "goalAlert";


    public static final String CREATE_ASSESS = "CREATE TABLE IF NOT EXISTS " + ASSESS_TABLE_NAME +
            " (" + ASSESS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
        ASSESS_COLUMN_TITLE + " TEXT, " +
        ASSESS_COLUMN_COURSE_ID + " INTEGER, " +
        ASSESS_COLUMN_TYPE + " TEXT, " +
        ASSESS_COLUMN_DUE_DATE + " TEXT ," +
        ASSESS_COLUMN_GOAL_ALERT + " INTEGER DEFAULT 0)";

    public Assessment(){

    }

    public Assessment(long courseId, String title, String type, String dueDate){
        this.id = 0;
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.goalAlert = 0;
    }

    public Assessment(long id, long courseId, String title, String type, String dueDate, int goalAlert){
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.goalAlert = goalAlert;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Intent secondIntent = getIntent( );
        assessDetailIntent = new Intent(this, AssessmentDetail.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.assess_list_view);


        courseId = secondIntent.getLongExtra("CourseID",0);

        if (courseId == 0){

        }else {

            DBentity db = new DBentity(this);
            dataset = db.getAllAssess(courseId);
            startManagingCursor(dataset);

            String[] columns = new String[]{Assessment.ASSESS_COLUMN_TITLE, Assessment.ASSESS_COLUMN_TYPE, Assessment.ASSESS_COLUMN_DUE_DATE};
            int[] rows = new int[]{R.id.title, R.id.type, R.id.dueDate};

            myAdapter = new SimpleCursorAdapter(this, R.layout.content_assessment, dataset, columns, rows);

            list.setAdapter(myAdapter);

            AdapterView.OnItemClickListener listclick = new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    assessDetailIntent.putExtra("AssessID", id);
                    assessDetailIntent.putExtra("CourseID", courseId);
                    startActivity(assessDetailIntent);
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
                assessDetailIntent.putExtra("AssessID", id);
                assessDetailIntent.putExtra("CourseID", courseId);
                startActivity(assessDetailIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onResume(){
            super.onResume();
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getGoalAlert() { return goalAlert; }

    public void setGoalAlert(int goalAlert) { this.goalAlert = goalAlert; }
}

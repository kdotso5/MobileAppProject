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


public class Term extends AppCompatActivity{

    private long id;
    private String name;
    private String startDate;
    private String endDate;
    private ListView list;
    private Intent termDetailIntent;
    private Intent courseIntent;
    private ListAdapter myAdapter;
    private Cursor dataset;

    public static final String TERM_TABLE_NAME = "term";
    public static final String TERM_COLUMN_ID = "_id";
    public static final String TERM_COLUMN_TITLE = "title";
    public static final String TERM_COLUMN_START_DATE = "startDate";
    public static final String TERM_COLUMN_END_DATE = "endDate";

    public static final String CREATE_TERM = "CREATE TABLE IF NOT EXISTS " + TERM_TABLE_NAME +
            " (" + TERM_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            TERM_COLUMN_TITLE + " TEXT, " +
            TERM_COLUMN_START_DATE + " TEXT, " +
            TERM_COLUMN_END_DATE + " TEXT)";


    public Term(){
    }

    public Term(String name, String startDate, String endDate){
        this.id = 0;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(long id, String name, String startDate, String endDate){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(Term term){
        this.id = term.getId();
        this.name = term.getName();
        this.startDate = term.getStartDate();
        this.endDate = term.getEndDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id){this.id = id;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        termDetailIntent = new Intent(this, TermDetail.class);
        courseIntent = new Intent(this, Course.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.term_list_view);
        DBentity db = new DBentity(this);

        dataset = db.getAllTerms();
        startManagingCursor(dataset);

        String[] columns = new String[]{Term.TERM_COLUMN_TITLE, Term.TERM_COLUMN_START_DATE, Term.TERM_COLUMN_END_DATE};
        int[] rows = new int[]{R.id.title, R.id.startDate, R.id.endDate};

        myAdapter = new SimpleCursorAdapter(this, R.layout.content_term, dataset, columns, rows);

        list.setAdapter(myAdapter);

        AdapterView.OnItemClickListener listclick = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                termDetailIntent.putExtra(Term.TERM_COLUMN_ID, id);
                startActivity(termDetailIntent);
            }
        };

        list.setOnItemClickListener(listclick);

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
                termDetailIntent.putExtra(Term.TERM_COLUMN_ID, 0);
                startActivity(termDetailIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    protected void onStart() {//happens quickly after oncreate then moves into onresume.
        super.onStart();
    }

    protected void onResume() {//activity has focus.  Stays here until it looses focus.  Release any resources started during onresume.
        super.onResume();
    }

    protected void onPause() {//interuptive event has occurred and app has lost focus.  Do not use to save data
        super.onPause();
    }

    protected void onStop() {//shut down/save data/etc.  Release any resources started during onstart.
        //// Called when another screen covers this one.  Saves all state data so no need to reinitialize components.
        super.onStop();

    }

    protected void onRestart() {//called when activity resumes from a stopped state
        super.onRestart();
    }

    protected void onDestroy() {//release all resources
        super.onDestroy();
    }


    public void openTermDetail(View view){
        Intent intent = new Intent(this, TermDetail.class);
        startActivity(intent);
    }


    public void openTermCourses(View view){

    }



    public void openCourses(View v){
        View parent = (View) v.getParent();
        ListView listview = (ListView)parent.getParent();

        final int pos = listview.getPositionForView(parent);
        dataset.moveToPosition(pos);

        courseIntent.putExtra(Term.TERM_COLUMN_ID, dataset.getLong(dataset.getColumnIndex(Term.TERM_COLUMN_ID)));
        startActivity(courseIntent);
    }

}


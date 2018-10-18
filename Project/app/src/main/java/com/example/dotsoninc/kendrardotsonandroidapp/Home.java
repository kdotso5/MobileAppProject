package com.example.dotsoninc.kendrardotsonandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;



public class Home extends AppCompatActivity {





    @Override

    protected void onCreate(Bundle savedInstanceState) {//everything needed when the page opens

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        }

    protected void onStart() {//happens quickly after oncreate then moves into onresume.
        super.onStart();

        DBentity entity = new DBentity(this);
        //entity.deleteTables();

        entity.makeTermTable();
        entity.makeCourseTable();
        entity.makeAssessTable();
        entity.makeNoteTable();
        entity.makeMentorTable();
        entity.makeEmailTable();
        entity.makePhoneTable();

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

    public void onSaveInstanceState() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.Action
        getMenuInflater().inflate(R.menu.menu_home, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;

    }

    public void openTerms(View view){
        Intent intent = new Intent(this, Term.class);
        startActivity(intent);
    }




}

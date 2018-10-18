package com.example.dotsoninc.kendrardotsonandroidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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


public class Note extends AppCompatActivity{
    private long id;
    private long courseId;
    private String note;
    private String date;
    private String[] emailAddr;
    private String smsMsg;
    private Course course;
    private ListView list;
    private Cursor dataset;
    private long noteId;
    private String shareType;
    DBentity db = new DBentity(this);


    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final String NOTE_COLUMN_DATE = "noteDate";
    public static final String NOTE_COLUMN_COURSE_ID = "courseID";
    public static final String NOTE_COLUMN_MEMO = "noteMemo";

    public static final String CREATE_NOTE = "CREATE TABLE IF NOT EXISTS " + NOTE_TABLE_NAME +
            " (" + NOTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            NOTE_COLUMN_COURSE_ID + " INTEGER DEFAULT 0, " +
            NOTE_COLUMN_DATE + " TEXT, " +
            NOTE_COLUMN_MEMO + " TEXT)";


    public Note(){

    }


    public Note(long id, long courseId, String note, String date){
        this.id = id;
        this.courseId = courseId;
        this.note = note;
        this.date = date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent secondIntent = getIntent( );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.note_list_view);

        courseId = secondIntent.getLongExtra(Note.NOTE_COLUMN_COURSE_ID,0);

        if (courseId == 0){

        }
        else {

            dataset = db.getAllNote(courseId);
            startManagingCursor(dataset);

            course = db.getCourse(courseId);

            String[] columns = new String[]{Note.NOTE_COLUMN_DATE, Note.NOTE_COLUMN_MEMO};
            int[] rows = new int[]{R.id.note_date, R.id.note_memo};

            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.content_note, dataset, columns, rows);

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
                builder.setMessage(getResources().getString(R.string.addNote));


                final EditText input = new EditText(this);
                builder.setView(input);

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

                builder.setPositiveButton(getResources().getString(R.string.action_add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String todaysDate = db.getStringDate();
                        note = input.getText().toString();
                        db.addNote(courseId, todaysDate, note);
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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void sendNote(View v){
        View parent = (View) v.getParent();

        final int pos = list.getPositionForView(parent);
        dataset.moveToPosition(pos);

        Note note = db.getNote(dataset.getLong(dataset.getColumnIndex(Note.NOTE_COLUMN_ID)));
        shareType = course.getShareType();
        final String message = note.getDate() + ":  " + note.getNote();
        final String subject = "Note for Course:  " + course.getName();

        switch (shareType) {
            case "None":
                break;

            case "Email":
                //send as Email
                //get email address from user
                AlertDialog.Builder emailBuilder = new AlertDialog.Builder(this);
                emailBuilder.setMessage(getResources().getString(R.string.getEmail));


                final EditText emailInput = new EditText(this);
                emailBuilder.setView(emailInput);

                emailBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

                emailBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        emailAddr = new String[]{emailInput.getText().toString()};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND)
                                .setData(new Uri.Builder().scheme("mailto").build())
                                .setType("text/plain")
                                .putExtra(Intent.EXTRA_EMAIL, emailAddr)
                                .putExtra(Intent.EXTRA_SUBJECT, subject)
                                .putExtra(Intent.EXTRA_TEXT, message);

                        startActivity(Intent.createChooser(emailIntent, "Send email...."));
                        finish();
                    }
                });


                AlertDialog emailAlert = emailBuilder.create();
                emailAlert.show();
                break;

            default:
                break;

        }

    }

    public void deleteNote(View v){

        View parent = (View) v.getParent();

        final int pos = list.getPositionForView(parent);
        dataset.moveToPosition(pos);

        noteId = dataset.getLong(dataset.getColumnIndex(Note.NOTE_COLUMN_ID));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.DeleteQuestion));

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //put message box here verifying delete request
                db.deleteNote(noteId);
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}

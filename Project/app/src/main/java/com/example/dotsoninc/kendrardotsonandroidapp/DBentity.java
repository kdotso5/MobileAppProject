package com.example.dotsoninc.kendrardotsonandroidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import java.text.*;
import java.util.Date;


public class DBentity extends SQLiteOpenHelper {

    private static final String DB_NAME = "DotsonUniv.db";

    private long termId = 0;
    private long courseId = 0;
    private long assessId = 0;
    private long mentorId = 0;




    public DBentity(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        makeTermTable();

        makeAssessTable();

        makeNoteTable();

        makeCourseTable();

        makeMentorTable();

        makePhoneTable();

        makeEmailTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS term");
        onCreate(db);
    }

    public void deleteTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists term");
        db.execSQL("drop table if exists assess");
        db.execSQL("drop table if exists note");
        db.execSQL("drop table if exists course");
        db.execSQL("drop table if exists mentor");
        db.execSQL("drop table if exists phone");
        db.execSQL("drop table if exists email");

    }


    public void makeTermTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(Term.CREATE_TERM);


        /*//the following will be removed after the database has been created and is working correctly.
        ContentValues values = new ContentValues();
        ContentValues assessValues = new ContentValues();
        ContentValues mentorValues = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String todaysDate = sdf.format(new Date());

        values.put(Term.TERM_COLUMN_TITLE, "Term 5");
        values.put(Term.TERM_COLUMN_START_DATE, todaysDate);
        values.put(Term.TERM_COLUMN_END_DATE, todaysDate);

        termId = db.insert(Term.TERM_TABLE_NAME, null, values);

        db.execSQL(Course.CREATE_COURSE);

        int i = 10;

        String shareType = "SMS";


        while (i > 0) {

            if (shareType == "None"){
                shareType = "Email";
            }else{
                shareType = "None";
            }

            values.put(Course.COURSE_COLUMN_TITLE, "Term 4 course " + i);
            values.put(Course.COURSE_COLUMN_TERM_ID, termId);
            values.put(Course.COURSE_COLUMN_STATUS, "Current");
            values.put(Course.COURSE_COLUMN_SHARE_TYPE, shareType);
            values.put(Course.COURSE_COLUMN_START_DATE, todaysDate);
            values.put(Course.COURSE_COLUMN_END_DATE, todaysDate);
            values.put(Course.COURSE_COLUMN_START_ALERT, 0);
            values.put(Course.COURSE_COLUMN_END_ALERT, 0);

            courseId = db.insert(Course.COURSE_TABLE_NAME, null, values);

            String type = "Performance";

            int j = 5;

            while (j > 0){

                if (type == "Performance"){
                    type = "Objective";
                }else{
                    type = "Performance";
                }

                assessValues.put(Assessment.ASSESS_COLUMN_TITLE, "Assessment " + j + " Course " + courseId);
                assessValues.put(Assessment.ASSESS_COLUMN_DUE_DATE, todaysDate);
                assessValues.put(Assessment.ASSESS_COLUMN_COURSE_ID, courseId);
                assessValues.put(Assessment.ASSESS_COLUMN_GOAL_ALERT, 0);
                assessValues.put(Assessment.ASSESS_COLUMN_TYPE, type);

                assessId = addAssess(courseId, "Assessment " , "10/4/18", "Objective", 0);
                //assessId = db.insert(Assessment.ASSESS_TABLE_NAME, null, assessValues);

                j--;
            }

            //add a test mentor
            mentorValues.put(Mentor.MENTOR_COLUMN_NAME, "Kendra Dotson" );
            mentorValues.put(Mentor.MENTOR_COLUMN_COURSE_ID, courseId);

            mentorId = db.insert(Mentor.MENTOR_TABLE_NAME, null, mentorValues);(

            i--;

        }*/
    }

    public Term addTerm(Term term) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Term.TERM_COLUMN_TITLE, term.getName());
        values.put(Term.TERM_COLUMN_START_DATE, term.getStartDate());
        values.put(Term.TERM_COLUMN_END_DATE, term.getEndDate());

        term.setId(db.insert(Term.TERM_TABLE_NAME, null, values));

        return term;
    }

    public long addTerm(String name, String startDate, String endDate) {//String title, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Term.TERM_COLUMN_TITLE, name);
        values.put(Term.TERM_COLUMN_START_DATE, startDate);
        values.put(Term.TERM_COLUMN_END_DATE, endDate);

        return db.insert(Term.TERM_TABLE_NAME, null, values);
    }

    public void updateTerm(long id, String name, String startDate, String endDate) {//String title, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE TERM SET " + Term.TERM_COLUMN_TITLE + " = '" + name + "', " + Term.TERM_COLUMN_START_DATE + " = '" + startDate + "', " + Term.TERM_COLUMN_END_DATE + " = '" + endDate + "' WHERE "
                + Term.TERM_COLUMN_ID + " = " + id;
        db.execSQL(sql);

    }



    public Term getTerm(long termId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Term term;

        Cursor cursor = db.query(Term.TERM_TABLE_NAME, new String[]{Term.TERM_COLUMN_ID, Term.TERM_COLUMN_TITLE, Term.TERM_COLUMN_START_DATE, Term.TERM_COLUMN_END_DATE}, Term.TERM_COLUMN_ID + "=?", new String[]{String.valueOf(termId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            term = new Term(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            term.setId(cursor.getLong(0));
            cursor.close();
            cursor.close();
            return term;
        }

        return null;

    }

    public Cursor getAllTerms() {
        String sql = "SELECT * FROM " + Term.TERM_TABLE_NAME + " ORDER BY " + Term.TERM_COLUMN_START_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteTerm(long termId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Term.TERM_TABLE_NAME, Term.TERM_COLUMN_ID + " = ?",
                new String[]{String.valueOf(termId)});
        db.close();
    }


    public void makeCourseTable() {


        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(Course.CREATE_COURSE);

    }

    public long addCourse(Long termId, String title, String status, String shareType, String startDate, String endDate, int startAlert, int endAlert) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Course.COURSE_COLUMN_TITLE, title);
        values.put(Course.COURSE_COLUMN_TERM_ID, termId);
        values.put(Course.COURSE_COLUMN_STATUS, status );
        values.put(Course.COURSE_COLUMN_SHARE_TYPE, shareType );
        values.put(Course.COURSE_COLUMN_START_DATE, startDate);
        values.put(Course.COURSE_COLUMN_END_DATE, endDate);
        values.put(Course.COURSE_COLUMN_START_ALERT, startAlert);
        values.put(Course.COURSE_COLUMN_END_ALERT, endAlert);

        return db.insert(Course.COURSE_TABLE_NAME, null, values);

    }

    public Course addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(Course.COURSE_COLUMN_TITLE, course.getName());
        values.put(Course.COURSE_COLUMN_TERM_ID, termId);
        values.put(Course.COURSE_COLUMN_STATUS, course.getStatus());
        values.put(Course.COURSE_COLUMN_SHARE_TYPE, course.getShareType());
        values.put(Course.COURSE_COLUMN_START_DATE, course.getStartDate());
        values.put(Course.COURSE_COLUMN_END_DATE, course.getEndDate());

        course.setId(db.insert(Course.COURSE_TABLE_NAME, null, values));

        return course;
    }

    public void updateCourse(long courseId, String name, String status, String shareType, String startDate, String endDate, int startAlert, int endAlert) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE COURSE SET " +
            Course.COURSE_COLUMN_TITLE + " = '" + name + "', " +
            Course.COURSE_COLUMN_STATUS + " = '" + status + "', " +
            Course.COURSE_COLUMN_SHARE_TYPE + " = '" + shareType + "', " +
            Course.COURSE_COLUMN_START_DATE + " = '" + startDate + "', " +
            Course.COURSE_COLUMN_END_DATE + " = '" + endDate + "', " +
            Course.COURSE_COLUMN_START_ALERT + " = " + startAlert + ", " +
            Course.COURSE_COLUMN_END_ALERT + " = " + endAlert +  " WHERE " +
            Course.COURSE_COLUMN_ID + " = " + courseId;
        db.execSQL(sql);

    }

    public Course getCourse(long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Course course;

        Cursor cursor = db.query(Course.COURSE_TABLE_NAME, new String[]{Course.COURSE_COLUMN_ID, Course.COURSE_COLUMN_TERM_ID, Course.COURSE_COLUMN_TITLE, Course.COURSE_COLUMN_STATUS, Course.COURSE_COLUMN_SHARE_TYPE, Course.COURSE_COLUMN_START_DATE, Course.COURSE_COLUMN_END_DATE, Course.COURSE_COLUMN_START_ALERT, Course.COURSE_COLUMN_END_ALERT}, Course.COURSE_COLUMN_ID + "=?", new String[]{String.valueOf(courseId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            course = new Course(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8));

            return course;
        }

        return null;

    }



    public Cursor getAllCourse(long termId) {

        String sql = "SELECT * FROM " + Course.COURSE_TABLE_NAME + " WHERE " + Course.COURSE_COLUMN_TERM_ID + " = " + termId +  " ORDER BY " + Course.COURSE_COLUMN_START_DATE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteCourse(long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Course.COURSE_TABLE_NAME, Course.COURSE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(courseId)});
        db.close();
    }


    public void makeAssessTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(Assessment.CREATE_ASSESS);

            //the following will be removed after the database has been created and is working correctly.
            ContentValues values = new ContentValues();

            java.util.Date today = new java.util.Date();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String todaysDate = df.format(today);

            values.put(Assessment.ASSESS_COLUMN_COURSE_ID, courseId);
            values.put(Assessment.ASSESS_COLUMN_TITLE, "this is an assessment");
            values.put(Assessment.ASSESS_COLUMN_TYPE, "Performance");
            values.put(Assessment.ASSESS_COLUMN_DUE_DATE, todaysDate);
            values.put(Assessment.ASSESS_COLUMN_GOAL_ALERT, 0);

            assessId = db.insert(Assessment.ASSESS_TABLE_NAME, null, values);

    }

    public long addAssess(Long courseId, String title, String dueDate, String type, int goalAlert) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Assessment.ASSESS_COLUMN_TITLE, title);
        values.put(Assessment.ASSESS_COLUMN_COURSE_ID, courseId);
        values.put(Assessment.ASSESS_COLUMN_TYPE, type );
        values.put(Assessment.ASSESS_COLUMN_DUE_DATE, dueDate);
        values.put(Assessment.ASSESS_COLUMN_GOAL_ALERT, goalAlert);

        return db.insert(Assessment.ASSESS_TABLE_NAME, null, values);

    }

    public void updateAssess(long assessId, long courseId, String name, String type, String dueDate, int goalAlert) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Assessment.ASSESS_COLUMN_TITLE, name);
        values.put(Assessment.ASSESS_COLUMN_DUE_DATE, dueDate);
        values.put(Assessment.ASSESS_COLUMN_TYPE, type );
        values.put(Assessment.ASSESS_COLUMN_GOAL_ALERT, goalAlert);

        String criteria = Assessment.ASSESS_COLUMN_ID + " = " + assessId;

        db.update(Assessment.ASSESS_TABLE_NAME, values, criteria, null);

    }


    public Assessment addAssess(Assessment assess) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Assessment.ASSESS_COLUMN_TITLE, assess.getName());
        values.put(Assessment.ASSESS_COLUMN_COURSE_ID, courseId);
        values.put(Assessment.ASSESS_COLUMN_TYPE, assess.getType());
        values.put(Assessment.ASSESS_COLUMN_DUE_DATE, assess.getDueDate());
        values.put(Assessment.ASSESS_COLUMN_GOAL_ALERT, assess.getGoalAlert());

        assess.setId(db.insert(Assessment.ASSESS_TABLE_NAME, null, values));

        return assess;
    }

    public Assessment getAssess(long assessId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Assessment assess;

        Cursor cursor = db.query(Assessment.ASSESS_TABLE_NAME, new String[]{Assessment.ASSESS_COLUMN_ID, Assessment.ASSESS_COLUMN_COURSE_ID, Assessment.ASSESS_COLUMN_TITLE, Assessment.ASSESS_COLUMN_TYPE, Assessment.ASSESS_COLUMN_DUE_DATE, Assessment.ASSESS_COLUMN_GOAL_ALERT}, Assessment.ASSESS_COLUMN_ID + "=?", new String[]{String.valueOf(assessId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            assess = new Assessment(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5));

            return assess;
        }

        return null;

    }

    public Cursor getAllAssess(long courseId) {
        String sql = "SELECT * FROM " + Assessment.ASSESS_TABLE_NAME + " WHERE " + Assessment.ASSESS_COLUMN_COURSE_ID + " = " + courseId + " ORDER BY " + Assessment.ASSESS_COLUMN_TITLE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteAssess(long assessId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Assessment.ASSESS_TABLE_NAME, Assessment.ASSESS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(assessId)});
        db.close();
    }



    public void makeNoteTable() {

        SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(Note.CREATE_NOTE);

    }

    public Note addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Note.NOTE_COLUMN_COURSE_ID, note.getCourseId());
        values.put(Note.NOTE_COLUMN_MEMO, note.getNote());
        values.put(Note.NOTE_COLUMN_DATE, note.getDate());

        note.setId(db.insert(Note.NOTE_TABLE_NAME, null, values));

        return note;

    }

    public long addNote(long courseId, String date, String note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Note.NOTE_COLUMN_DATE, date);
        values.put(Note.NOTE_COLUMN_COURSE_ID, courseId);
        values.put(Note.NOTE_COLUMN_MEMO, note);

        return db.insert(Note.NOTE_TABLE_NAME, null, values);
    }

    public Note getNote(long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Note note;


        Cursor cursor = db.query(Note.NOTE_TABLE_NAME, new String[]{Note.NOTE_COLUMN_ID, Note.NOTE_COLUMN_COURSE_ID, Note.NOTE_COLUMN_MEMO, Note.NOTE_COLUMN_DATE}, Note.NOTE_COLUMN_ID + "=?", new String[]{String.valueOf(noteId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            note = new Note(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3));

            return note;
        }

        return null;


    }

    public Cursor getAllNote(long courseId) {
        String sql = "SELECT * FROM " + Note.NOTE_TABLE_NAME + " WHERE " + Note.NOTE_COLUMN_COURSE_ID + " = " + courseId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteNote(long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Note.NOTE_TABLE_NAME, Note.NOTE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(noteId)});
        db.close();
    }




    public void makeMentorTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        Mentor test = new Mentor();


            db.execSQL(test.CREATE_MENTOR);

    }

    public Mentor addMentor(Mentor mentor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Mentor.MENTOR_COLUMN_COURSE_ID, mentor.getCourseId());
        values.put(Mentor.MENTOR_COLUMN_NAME, mentor.getName());

        mentor.setId(db.insert(Mentor.MENTOR_TABLE_NAME, null, values));

        return mentor;
    }

    public long addMentor(long courseId, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Mentor.MENTOR_COLUMN_COURSE_ID, courseId);
        values.put(Mentor.MENTOR_COLUMN_NAME, name);

        return db.insert(Mentor.MENTOR_TABLE_NAME, null, values);
    }

    public void updateMentor(long mentorId, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Mentor.MENTOR_COLUMN_NAME, name);

        String criteria = Mentor.MENTOR_COLUMN_ID + " = " + mentorId;

        db.update(Mentor.MENTOR_TABLE_NAME, values, criteria, null);

    }


    public Mentor getMentor(long mentorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Mentor mentor;

        Cursor cursor = db.query(Mentor.MENTOR_TABLE_NAME, new String[]{Mentor.MENTOR_COLUMN_ID, Mentor.MENTOR_COLUMN_COURSE_ID, Mentor.MENTOR_COLUMN_NAME}, Mentor.MENTOR_COLUMN_ID + "=?", new String[]{String.valueOf(mentorId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            mentor = new Mentor(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2));

            return mentor;
        }

        return null;

    }

    public Cursor getAllMentor(long courseId) {
        String sql = "SELECT * FROM " + Mentor.MENTOR_TABLE_NAME + " WHERE " + Mentor.MENTOR_COLUMN_COURSE_ID + " = " + courseId +  " ORDER BY " + Mentor.MENTOR_COLUMN_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteMentor(long mentorId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Mentor.MENTOR_TABLE_NAME, Mentor.MENTOR_COLUMN_ID + " = ?",
                new String[]{String.valueOf(mentorId)});
        db.close();
    }





    public void makePhoneTable() {

        SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(Phone.CREATE_PHONE);

        }

    public Phone addPhone(Phone phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Phone.PHONE_COLUMN_MENTOR_ID, phone.getMentorId());
        values.put(Phone.PHONE_COLUMN_NUMBER, phone.getPhone());

        phone.setId(db.insert(Phone.PHONE_TABLE_NAME, null, values));

        return phone;
    }

    public long addPhone(long mentorId, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Phone.PHONE_COLUMN_MENTOR_ID, mentorId);
        values.put(Phone.PHONE_COLUMN_NUMBER, phone);

        return db.insert(Phone.PHONE_TABLE_NAME, null, values);

    }


    public Phone getPhone(long phoneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Phone phone;

        Cursor cursor = db.query(Phone.PHONE_TABLE_NAME, new String[]{Phone.PHONE_COLUMN_ID, Phone.PHONE_COLUMN_MENTOR_ID, Phone.PHONE_COLUMN_NUMBER}, Phone.PHONE_COLUMN_ID + "=?", new String[]{String.valueOf(phoneId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            phone = new Phone(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2));

            return phone;
        }

        return null;

    }

    public Cursor getAllPhone(long mentorId) {
        String sql = "SELECT * FROM " + Phone.PHONE_TABLE_NAME + " WHERE " + Phone.PHONE_COLUMN_MENTOR_ID + " = " + mentorId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deletePhone(long phoneId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Phone.PHONE_TABLE_NAME, Phone.PHONE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(phoneId)});
        db.close();
    }



    public void makeEmailTable() {

        SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(Email.CREATE_EMAIL);

    }

    public Email addEmail(Email email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Email.EMAIL_COLUMN_MENTOR_ID, email.getMentorId());
        values.put(Email.EMAIL_COLUMN_ADDRESS, email.getEmail());

        email.setId(db.insert(Email.EMAIL_TABLE_NAME, null, values));

        return email;
    }

    public long addEmail(long mentorId, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Email.EMAIL_COLUMN_MENTOR_ID, mentorId);
        values.put(Email.EMAIL_COLUMN_ADDRESS, email);

        return db.insert(Email.EMAIL_TABLE_NAME, null, values);

    }

    public Email getEmail(long emailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Email email;

        Cursor cursor = db.query(Email.EMAIL_TABLE_NAME, new String[]{Email.EMAIL_COLUMN_ID, Email.EMAIL_COLUMN_MENTOR_ID, Email.EMAIL_COLUMN_ADDRESS}, Email.EMAIL_COLUMN_ID + "=?", new String[]{String.valueOf(emailId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            email = new Email(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2));

            return email;
        }

        return null;

    }

    public Cursor getAllEmail(long mentorId) {
        String sql = "SELECT * FROM " + Email.EMAIL_TABLE_NAME + " WHERE " + Email.EMAIL_COLUMN_MENTOR_ID + " = " + mentorId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    public void deleteEmail(long emailId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Email.EMAIL_TABLE_NAME, Email.EMAIL_COLUMN_ID + " = ?",
                new String[]{String.valueOf(emailId)});
        db.close();
    }



    public java.util.Date getUtilDate(String strDate) {
        java.sql.Date sqlDate = new java.sql.Date(Long.valueOf(strDate));
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        return utilDate;
    }

    public java.sql.Date getSqlDate(String strDate) {
        Long millis = Long.valueOf((strDate));
        java.util.Date utilDate = new java.util.Date(Long.valueOf(strDate));
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    public String getStringDate(){

        Date todays = new Date(System.currentTimeMillis());
        String todaysDate = DateFormat.getDateInstance().format(todays);

        return todaysDate;

    }



    public boolean isValidDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date testDate = null;

        try {
            testDate = sdf.parse(date);
        }
        catch(ParseException pe){
            return false;
        }
        return true;
    }

}


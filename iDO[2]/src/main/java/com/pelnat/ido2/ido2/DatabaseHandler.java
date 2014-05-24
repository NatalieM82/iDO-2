package com.pelnat.ido2.ido2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pelnat.ido2.ido2.TaskDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Natalie on 20/11/13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "taskManager";

    // Task table name
    private static final String TABLE_TASKS = "tasks";

    // Task Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATE_YEAR = "year";
    private static final String KEY_DATE_MONTH = "month";
    private static final String KEY_DATE_DAY = "day";
    private static final String KEY_TIME_HOUR = "hour";
    private static final String KEY_TIME_MINUTES = "minutes";
    private static final String KEY_LOCATION_LONGITUDE = "longitude";
    private static final String KEY_LOCATION_LATITUDE = "latitude";
    private static final String KEY_ALARM = "alarm";
    private static final String KEY_DONE = "done";
    private static final String[] COLUMNS = {KEY_ID, KEY_MESSAGE};


    //   private static final String KEY_PH_NO = "phone_number";

    public static DatabaseHandler instance = null;

    private Context context;

    public static synchronized DatabaseHandler getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_MESSAGE + " , "
                + KEY_DATE_YEAR + " , "
                + KEY_DATE_MONTH + " , "
                + KEY_DATE_DAY + " , "
                + KEY_TIME_HOUR + " , "
                + KEY_TIME_MINUTES + " , "
                + KEY_LOCATION_LONGITUDE + " , "
                + KEY_LOCATION_LATITUDE + " , "
                + KEY_ALARM + " , "
                + KEY_DONE
                + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addTask(TaskDetails task) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        /*
         * save task Message, Date(year+month+day), Time(Hour+Time), Location(Longitude+Latitude)
         * */

        values.put(KEY_ID, task._id);
        values.put(KEY_MESSAGE, task._taskMessage);
        values.put(KEY_DATE_YEAR, task._dateYear);
        values.put(KEY_DATE_MONTH, task._dateMonth);
        values.put(KEY_DATE_DAY, task._dateDay);
        values.put(KEY_TIME_HOUR, task._timeHour);
        values.put(KEY_TIME_MINUTES, task._timeMinute);
        values.put(KEY_ALARM, task._alarm);
        values.put(KEY_DONE, task._done);

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single task
    TaskDetails getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_TASKS + " where " + id + "=" + KEY_ID, null);
        if (cursor != null)
            cursor.moveToFirst();

        TaskDetails task = new TaskDetails();
        task.setID(Integer.parseInt(cursor.getString(0)));
        task.setTaskMessage(cursor.getString(1));
        task.set_dateYear(Integer.parseInt(cursor.getString(2)));
        task.set_dateMonth(Integer.parseInt(cursor.getString(3)));
        task.set_dateDay(Integer.parseInt(cursor.getString(4)));
        task.set_timeHour(Integer.parseInt(cursor.getString(5)));
        task.set_timeMinute(Integer.parseInt(cursor.getString(6)));
        task.set_alarm(Integer.parseInt(cursor.getString(7)));
        task.set_done(Integer.parseInt(cursor.getString(8)));

        // return task
        return task;
    }

    // Getting All Tasks
    public List<TaskDetails> getAllTasks() {
        List<TaskDetails> taskList = new ArrayList<TaskDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                TaskDetails task = new TaskDetails();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setTaskMessage(cursor.getString(1));
                task.set_dateYear(Integer.parseInt(cursor.getString(2)));
                task.set_dateMonth(Integer.parseInt(cursor.getString(3)));
                task.set_dateDay(Integer.parseInt(cursor.getString(4)));
                task.set_timeHour(Integer.parseInt(cursor.getString(5)));
                task.set_timeMinute(Integer.parseInt(cursor.getString(6)));
//              task.set_alarm(Integer.parseInt(cursor.getString(7)));
//              task.set_done(Integer.parseInt(cursor.getString(8)));
                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToPrevious());
        }

        // return task list
        return taskList;
    }

    // Updating single task
    public void updateTask(TaskDetails task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // The fields
        ContentValues con = new ContentValues();
        con.put(KEY_ID, task.get_id());
        con.put(KEY_MESSAGE, task._taskMessage);
        con.put(KEY_DATE_YEAR, task._dateYear);
        con.put(KEY_DATE_MONTH, task._dateMonth);
        con.put(KEY_DATE_DAY, task._dateDay);
        con.put(KEY_TIME_HOUR, task._timeHour);
        con.put(KEY_TIME_MINUTES, task._timeMinute);
        con.put(KEY_ALARM, task._alarm);
        con.put(KEY_DONE, task._done);

        db.update(TABLE_TASKS, con, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getID())});
        db.close();
    }

    // Deleting single task
    public void deleteTask(TaskDetails item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
        db.close();
    }


    // Getting tasks Count
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



}

package com.pelnat.ido2.ido2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Natalie on 23/05/2014.
 */
public class NewTask extends FragmentActivity implements DialogListener {

    private int hasAlarm = 0; //0 no , 1 yes
    private int isDone = 0;   //0 no , 1 yes
    private int timeHour = -1;
    private int timeMinute = -1;
    private int dateYear = -1;
    private int dateMonth = -1;
    private int dateDay = -1;
    public static DatabaseHandler db;
    private static TaskListBaseAdapter iLBa;
    @SuppressLint("NewApi")

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=DatabaseHandler.getInstance(this);
        // Get the message from the intent
        Intent intent = getIntent();
        iLBa= new TaskListBaseAdapter(this, db.getInstance(this).getAllTasks());
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);


        // Set the text view as the activity layout
        setContentView(R.layout.create_new_task);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //      NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createTask(View view) {
        // Getting the text
        EditText editText = (EditText) findViewById(R.id.new_task);
        if (editText.getText().toString().isEmpty()) return;
        String taskMessage = editText.getText().toString();

        //Creating ID
        int nowUseAsId = (int) (long) System.currentTimeMillis();
        if (nowUseAsId < 0) nowUseAsId *= -1;

        //Checking if Alarm needed
        TaskDetails newTask=new TaskDetails(nowUseAsId, taskMessage, dateYear, dateMonth, dateDay, timeHour, timeMinute,hasAlarm, isDone);
        Toast.makeText(NewTask.this, "Out "+timeHour+":"+timeMinute, Toast.LENGTH_LONG).show();
        if ((timeHour != -1 && timeMinute != -1) || (dateDay != -1 && dateMonth != -1 && dateYear != -1)) {

            Toast.makeText(NewTask.this, "In "+timeHour+":"+timeMinute, Toast.LENGTH_LONG).show();
            Toast.makeText(NewTask.this, "In "+dateDay+":"+dateMonth+":"+dateYear, Toast.LENGTH_LONG).show();
            hasAlarm = 1;
            newTask.set_alarm(hasAlarm);
            isDone = 0;
            newTask.set_done(isDone);
            createAlarmAtDate(newTask);
        }


//
//        Intent intent = new Intent("com.pelnat.ido2.ido2.ReminderBroadCastReceiver");
//        intent.putExtra("taskMessage", newTask._taskMessage);
//        intent.putExtra("taskId", newTask._id);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, newTask.getID(), intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);

        db.addTask(newTask);
        editText.setText("");
        initialize_variables();
        updateListView();
        finish();

    }

    public void updateListView() {
        try {
            ListView lv = (ListView) findViewById(R.id.listV_main);
            TaskListBaseAdapter currentList = new TaskListBaseAdapter(this, (java.util.List<TaskDetails>) iLBa);
            lv.setAdapter(currentList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize_variables() {
        try {
        /* initialize variables */
            // Time
            timeHour = -1;
            timeMinute = -1;
            // Date
            dateYear = -1;
            dateMonth = -1;
            dateDay = -1;

            hasAlarm = 0;
            isDone = 0;

//            ifEditTask = -1;
//            taskIdSelected = -1;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAlarmAtDate(TaskDetails task) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.pelnat.ido2.ido2.ReminderBroadCastReceiver");
            intent.putExtra("taskMessage", task._taskMessage);
            intent.putExtra("taskId", task._id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getID(), intent, 0);

        /* getting all parameters of create Date ob from task */
            Date date = new Date();
            // fix date before and set a default day
            date.setYear(date.getYear() + 1900);
            date.setMonth(date.getMonth() + 1);
            date.setDate(date.getDay());
            date.setHours(10);
            date.setMinutes(0);
            //System.out.println("----------------"+task._dateYear+" "+task._dateMonth+" "+task._dateDay+" "+task._timeHour+" "+task._timeMinute);
            if (task._dateDay != -1 && task._dateMonth != -1 && task._dateYear != -1) {
                date.setYear(task._dateYear);
                date.setMonth(task._dateMonth);
                date.setDate(task._dateDay);
            }
            if (task._timeHour != -1 && task._timeMinute != -1) {
                date.setHours(task._timeHour);
                date.setMinutes(task._timeMinute);
            }
            if (task._timeHour == -1 && task._timeMinute == -1 && task._dateDay == -1 && task._dateMonth == -1 && task._dateYear == -1) {
                return;
            }
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millisecondsUntilDate(), pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long millisecondsUntilDate() {
        try {
            Calendar cal = Calendar.getInstance();
            if (timeHour != -1 && timeMinute != -1 && dateDay == -1 && dateMonth == -1 && dateYear == -1) {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), timeHour, timeMinute);
            }

            if (dateDay != -1 && dateMonth != -1 && dateYear != -1 && timeHour != -1 && timeMinute != -1) {
                cal.set(dateYear, dateMonth, dateDay, timeHour, timeMinute);
            }

            if (dateDay != -1 && dateMonth != -1 && dateYear != -1 && timeHour == -1 && timeMinute == -1) {
                cal.set(dateYear, dateMonth, dateDay, 10, 0);
            }
            Calendar now = Calendar.getInstance();
            long diff_in_ms = cal.getTimeInMillis() - now.getTimeInMillis();
            return diff_in_ms;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void cancel(View view)
    {
        finish();
    }

    public void showTimePickerDialog(View v) {
        try {
            DialogFragment newFragment = new TimePickerFragment();

            // add details if user want to edit exist task
            if (timeHour != 0 && timeMinute != 0) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("timeHour", timeHour);
                dataBundle.putInt("timeMinute", timeMinute);
                newFragment.setArguments(dataBundle);
            }
            newFragment.show(getFragmentManager(), "timePicker");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDatePickerDialog(View v) {
        try {
            DialogFragment newFragment = new DatePickerFragment();

            // add details if user want to edit exist task
            if (dateYear != 0 && dateMonth != 0 && dateDay != 0) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("dateYear", dateYear);
                dataBundle.putInt("dateMonth", dateMonth);
                dataBundle.putInt("dateDay", dateDay);
                newFragment.setArguments(dataBundle);
                System.out.println("save bundle----------" + dateDay + "." + dateMonth + "." + dateYear);
            }
            newFragment.show(getFragmentManager(), "datePicker");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        Button bTime = (Button) findViewById(R.id.time);
        Button bDate = (Button) findViewById(R.id.date);
        if (checked) {
            bTime.setEnabled(true);
            bDate.setEnabled(true);
            hasAlarm = 1;

        }
        else {
            bTime.setEnabled(false);
            bDate.setEnabled(false);
            hasAlarm = 0;
        }
    }

    @Override
    public void onFinishEditDialog(Intent data) {

        try {
        /* For TimePicker Fragment */
            if (data.getExtras().containsKey("hour"))
                timeHour = data.getExtras().getInt("hour");
            if (data.getExtras().containsKey("minute"))
                timeMinute = data.getExtras().getInt("minute");

        /* For DatePicker Fragment */
            if (data.getExtras().containsKey("year"))
                dateYear = data.getExtras().getInt("year");
            if (data.getExtras().containsKey("month"))
                dateMonth = data.getExtras().getInt("month");
            if (data.getExtras().containsKey("day"))
                dateDay = data.getExtras().getInt("day");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

package com.pelnat.ido2.ido2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public static DatabaseHandler db=null;
    private TaskListBaseAdapter iLBa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        db=DatabaseHandler.getInstance(this);
        setContentView(R.layout.activity_main);
        updateListView();
        iLBa= new TaskListBaseAdapter(this, db.getInstance(this).getAllTasks());
        final ListView listView = (ListView) findViewById(R.id.listV_main);
    }

    protected void onResume()
    {
        super.onResume();
        updateListView();
        //  iLBa.notifyDataSetChanged();
    }

    public void updateListView()
    {
        ListView lv=(ListView) findViewById(R.id.listV_main);
        TaskListBaseAdapter iLBa=new TaskListBaseAdapter(this, db.getInstance(this).getAllTasks());
        lv.setAdapter(iLBa);
    }

    public void createNew(View view)
    {
        // Do something in response to button
        Intent intent = new Intent(this, NewTask.class);
        startActivity(intent);
    }

    public void done (View view) {
       View parent = (View) view.getParent();
       parent.setAlpha(0.2f);
    }

    public void edit (View view) {

//        // Message
//        EditText message = (EditText) findViewById(R.id.etNewTask);
//        message.setText(currentList.getItemByID(taskIdSelected).getTaskMessage());

    }

    public void delete(View view)
    {
        ListView listView = (ListView) findViewById(R.id.listV_main);
        int position = listView.getPositionForView(view);
        TaskDetails item = (TaskDetails) listView.getItemAtPosition(position);
        db.deleteTask(item);
        // iLBa.notifyDataSetChanged();
        updateListView();
    }
}

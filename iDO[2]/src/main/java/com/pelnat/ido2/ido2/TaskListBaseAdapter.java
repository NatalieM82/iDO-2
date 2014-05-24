package com.pelnat.ido2.ido2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Natalie on 23/05/2014.
 */
public class TaskListBaseAdapter extends BaseAdapter {
    private final Activity context;
    private static List<TaskDetails> itemDetailsrrayList;
    private LayoutInflater l_Inflater;

    public TaskListBaseAdapter(Activity context, List<TaskDetails> results) {
        this.context = context;
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    private final View.OnClickListener editButtonOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(context, NewTask.class);
            intent.putExtra("urlIndex", (Integer) view.getTag());
            context.startActivity(intent);
        }
    };

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.task, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(itemDetailsrrayList.get(position).getTaskMessage());


        return convertView;
    }


    private static class ViewHolder {
        TextView tvName;

    }
}

package com.example.parking.presentation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity;
import com.example.parking.R;
import com.example.parking.data.models.Employee;

import java.util.ArrayList;

public class ExpListAdapterAdminEmployees extends BaseExpandableListAdapter {
    private final ArrayList<Employee> mGroups;
    private final Context mContext;

    public ExpListAdapterAdminEmployees(Context context, ArrayList<Employee> groups){
        this.mContext = context;
        this.mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        TextView textCaption = (TextView) convertView.findViewById(R.id.textCaption);
        if (isExpanded){
            textCaption.setText("");
        }
        else{
            textCaption.setText("Click for details");
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(mGroups.get(groupPosition).getName() + ", " + mGroups.get(groupPosition).getEmail());

        return convertView;

    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.admin_employees_child_view, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.textName);
        textChild.setText(mGroups.get(groupPosition).getName());
        TextView textChild1 = (TextView) convertView.findViewById(R.id.textEmail);
        textChild1.setText(mGroups.get(groupPosition).getEmail());

        // тут вызываем функцию удаления
        Button buttonDelete = (Button)convertView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view -> {
            mGroups.remove(groupPosition);
            notifyDataSetChanged();
        });

        // здесь переходим в активность с редактированием, вызывая фрагмент для сотрудников
        Button buttonEdit = (Button)convertView.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "employees";
                Intent intent = new Intent(mContext.getApplicationContext(), EditModelActivity.class);
                intent.putExtra("fragment", data);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

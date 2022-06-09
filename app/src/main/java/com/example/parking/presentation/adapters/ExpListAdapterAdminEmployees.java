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
import com.example.parking.presentation.fragments.employee.list.elm.Effect;
import com.example.parking.presentation.fragments.employee.list.elm.Event;
import com.example.parking.presentation.fragments.employee.list.elm.State;

import java.util.ArrayList;

import vivid.money.elmslie.core.store.Store;

public class ExpListAdapterAdminEmployees extends BaseExpandableListAdapter {
    private final ArrayList<Employee> mGroups;
    private final Context mContext;
    public Store<Event, Effect, State> store;

    public ExpListAdapterAdminEmployees(
            Context context,
            ArrayList<Employee> groups,
            Store<Event, Effect, State> store
    ){
        this.mContext = context;
        this.mGroups = groups;
        this.store = store;
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
        textGroup.setText(mGroups.get(groupPosition).getName());

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
        Employee currentEmployee = mGroups.get(groupPosition);

        TextView textChild1 = (TextView) convertView.findViewById(R.id.textEmail);
        textChild1.setText(currentEmployee.getName());

        Button buttonDelete = (Button)convertView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view -> store.accept(new Event.Ui.ClickDeleteEmployee(currentEmployee, groupPosition)));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

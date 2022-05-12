package com.example.parking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.R;
import com.example.parking.models.Reservation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpListAdapterUserReservations extends BaseExpandableListAdapter {
    private final ArrayList<Reservation> mGroups;
    private final Context mContext;

    public ExpListAdapterUserReservations(Context context, ArrayList<Reservation> groups){
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
        textGroup.setText(mGroups.get(groupPosition).getDate() + ", " + mGroups.get(groupPosition).getTime());

        return convertView;

    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_reservations_child_view, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.textDate);
        textChild.setText(mGroups.get(groupPosition).getDate());
        TextView textChild1 = (TextView) convertView.findViewById(R.id.textTime);
        textChild1.setText(mGroups.get(groupPosition).getTime());
        TextView textChild2 = (TextView) convertView.findViewById(R.id.textCar);
        textChild2.setText(mGroups.get(groupPosition).getCar());
        TextView textChild3 = (TextView) convertView.findViewById(R.id.textCarNum);
        textChild3.setText(mGroups.get(groupPosition).getCarNum());

        Button button = (Button)convertView.findViewById(R.id.buttonDelete);
        button.setOnClickListener(view -> {
            mGroups.remove(groupPosition);
            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

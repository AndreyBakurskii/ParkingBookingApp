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
import com.example.parking.data.models.ParkingSpot;
import com.example.parking.presentation.fragments.parkingSpot.list.elm.Event;
import com.example.parking.presentation.fragments.parkingSpot.list.elm.Effect;
import com.example.parking.presentation.fragments.parkingSpot.list.elm.State;

import java.util.ArrayList;

import vivid.money.elmslie.core.store.Store;

public class ExpListAdapterAdminSpots extends BaseExpandableListAdapter {
    private final ArrayList<ParkingSpot> mGroups;
    private final Context mContext;
    public Store<Event, Effect, State> store;

    public ExpListAdapterAdminSpots(Context context, ArrayList<ParkingSpot> groups, Store store){
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
        textGroup.setText(String.valueOf(mGroups.get(groupPosition).getParkingNumber()));
        return convertView;

    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.admin_spots_child_view, null);
        }

        ParkingSpot currentParkingSpot = mGroups.get(groupPosition);

        TextView textChild2 = (TextView) convertView.findViewById(R.id.textNum);
        textChild2.setText(String.valueOf(currentParkingSpot.getParkingNumber()));

        Button buttonDelete = (Button)convertView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view -> store.accept(new Event.Ui.ClickDeleteParkingSpot(currentParkingSpot, groupPosition)));

        // здесь переходим в активность с редактированием, вызывая фрагмент для мест
        Button buttonEdit = (Button)convertView.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(view -> store.accept(new Event.Ui.ClickEditParkingSpot(currentParkingSpot, groupPosition)));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

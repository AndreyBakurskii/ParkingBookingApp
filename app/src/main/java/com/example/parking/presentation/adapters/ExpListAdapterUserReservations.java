package com.example.parking.presentation.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity;
import com.example.parking.R;
import com.example.parking.data.models.Reservation;
import com.example.parking.presentation.activities.UserActivity.elm.Effect;
import com.example.parking.presentation.activities.UserActivity.elm.Event;
import com.example.parking.presentation.activities.UserActivity.elm.State;

import org.w3c.dom.Text;

import java.util.ArrayList;

import vivid.money.elmslie.core.store.Store;

public class ExpListAdapterUserReservations extends BaseExpandableListAdapter {
    private final ArrayList<Reservation> mGroups;
    private final Activity mContext;
    public Store<Event, Effect, State> store;

    public ExpListAdapterUserReservations(
            Activity context,
            ArrayList<Reservation> groups,
            Store<Event, Effect, State> store){
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

        Reservation currentReservation = mGroups.get(groupPosition);

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(currentReservation.getPresentationDate() + ", " + currentReservation.getPresentationTime());

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

        Reservation currentReservation = mGroups.get(groupPosition);

        TextView textDate = (TextView) convertView.findViewById(R.id.textDate);
        textDate.setText(currentReservation.getPresentationDate());

        TextView textTime = (TextView) convertView.findViewById(R.id.textTime);
        textTime.setText(currentReservation.getPresentationTime());

        TextView textCaptionSpotNum = (TextView) convertView.findViewById(R.id.textSpotNum);
        textCaptionSpotNum.setText(String.valueOf(currentReservation.getParkingSpot().getParkingNumber()));

        TextView textCarModel = (TextView) convertView.findViewById(R.id.textCar);
        textCarModel.setText(currentReservation.getCar().getModel());

        TextView textCarNum = (TextView) convertView.findViewById(R.id.textCarNum);
        textCarNum.setText(currentReservation.getCar().getRegistryNumber());

        Button button = (Button)convertView.findViewById(R.id.buttonDelete);
        button.setOnClickListener(
//                view -> store.accept(new Event.Ui.ClickDeleteReservation(currentReservation, groupPosition))
        view -> {
            Log.i("BUTTON DELETE", "CLICKED");
        }
        );

        // здесь переходим в активность с редактированием, вызывая фрагмент для брони
        Button buttonEdit = (Button)convertView.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(
                view -> {
                    Log.i("BUTTON EDIT", "CLICKED");
                }
        );

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

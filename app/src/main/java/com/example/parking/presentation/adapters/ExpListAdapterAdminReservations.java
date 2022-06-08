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
import com.example.parking.data.models.Reservation;

import java.util.ArrayList;

public class ExpListAdapterAdminReservations extends BaseExpandableListAdapter {
    private final ArrayList<Reservation> mGroups;
    private final Context mContext;

    public ExpListAdapterAdminReservations(Context context, ArrayList<Reservation> groups){
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
//        textGroup.setText(mGroups.get(groupPosition).getDate() + ", " + mGroups.get(groupPosition).getTime());

        return convertView;

    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.admin_reservations_child_view, null);
        }

//        TextView textChild = (TextView) convertView.findViewById(R.id.textDate);
//        textChild.setText(mGroups.get(groupPosition).getDate());
//        TextView textChild1 = (TextView) convertView.findViewById(R.id.textTime);
//        textChild1.setText(mGroups.get(groupPosition).getTime());
//        TextView textChild2 = (TextView) convertView.findViewById(R.id.captionSpotNum);
//        textChild2.setText(mGroups.get(groupPosition).getSpotNum());
//        TextView textChild3 = (TextView) convertView.findViewById(R.id.textCarNum);
//        textChild3.setText(mGroups.get(groupPosition).getCarNum());
//        TextView textChild4 = (TextView) convertView.findViewById(R.id.textEmail);
//        textChild4.setText(mGroups.get(groupPosition).getEmail());

        Button button = (Button)convertView.findViewById(R.id.buttonDelete);
        button.setOnClickListener(view -> {
            mGroups.remove(groupPosition);
            notifyDataSetChanged();
        });

        // здесь переходим в активность с редактированием, вызывая фрагмент для брони
        Button buttonEdit = (Button)convertView.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "reservations";
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

package com.example.course_work_student_money;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class BlankFragment3 extends Fragment {

    SimpleCursorAdapter userAdapter;
    DBHelper sqlHelper;
    Cursor userCursor;
    ListView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootview = inflater.inflate(R.layout.blank_fragment3, container, false);
        mList= (ListView)  rootview.findViewById(android.R.id.list);
        return rootview;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sqlHelper = new DBHelper(getActivity());
        sqlHelper.create_db();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sqlHelper.open();
            userCursor = sqlHelper.database.rawQuery("select * from " + DBHelper.TABLE +" where rashod=1", null);
            String[] headers = new String[]{DBHelper.COLUMN_NAME,DBHelper.COLUMN_TAG,DBHelper.COLUMN_MONEY,DBHelper.COLUMN_DATA };
            userAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item, userCursor, headers, new int[]{ R.id._name, R.id._tag, R.id._money, R.id._data},0);
            mList.setAdapter(userAdapter);

        }
        catch (SQLException ex){}
    }

}
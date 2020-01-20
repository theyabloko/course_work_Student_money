package com.example.course_work_student_money;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.sql.SQLException;

public class BlankFragment extends Fragment {
    SimpleCursorAdapter userAdapter;
    DBHelper sqlHelper;
    Cursor userCursor;
    ListView mList;

    String Data_search;
    String Queary;
    private TextView Data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_blank, container, false);
        mList= (ListView)  rootview.findViewById(android.R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Add_Activity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        Data = (TextView) rootview.findViewById(R.id.textView9);
        return rootview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sqlHelper = new DBHelper(getActivity());
        sqlHelper.create_db();
    }

    /*
    public void onRadioButtonClicked(View view) {
        DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
        String date = df.format(Calendar.getInstance().getTime());
        // если переключатель отмечен
        boolean checked = ((RadioButton) view).isChecked();
        // Получаем нажатый переключатель
        switch(view.getId()) {
            case R.id.radioButton_today:
                if (checked){
                    Data_search=date;
                    Data.setText(Data_search);
                    Toast.makeText(getBaseContext(), Data_search, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radioButton_week:
                if (checked){
                    Data_search="1";
                    onResume();
                }
            case R.id.radioButton_month:
                if (checked){
                    Data_search="1";
                }
                break;
            case R.id.radioButton_year:
                if (checked){
                    Data_search="1";
                }
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        try {
            sqlHelper.open();
            userCursor = sqlHelper.database.rawQuery("select * from " + DBHelper.TABLE +" order by data DESC ", null);
            String[] headers = new String[]{DBHelper.COLUMN_NAME,DBHelper.COLUMN_TAG,DBHelper.COLUMN_MONEY,DBHelper.COLUMN_DATA };
            userAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item, userCursor, headers, new int[]{ R.id._name, R.id._tag, R.id._money, R.id._data},0);
            mList.setAdapter(userAdapter);

        }
        catch (SQLException ex){}
    }





}
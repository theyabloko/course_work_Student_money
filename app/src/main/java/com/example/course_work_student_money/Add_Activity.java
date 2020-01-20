package com.example.course_work_student_money;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Add_Activity extends AppCompatActivity {

    private EditText NameEditText;
    private Spinner TagSpinner;
    private EditText MoneyEditText;
    private EditText DataEditText;
    private Spinner TypeSpinner;
    private EditText c4date;
    Button delButton;
    Button saveButton;


    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;

    String[] Tag = {"Стипендия","Подработка","Продукты", "Развлечения", "Образование", "Услуги", "Другое"};
    String Type="";
    String Tag_name ="";

    Calendar dateAndTime= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        NameEditText = (EditText) findViewById(R.id.editText_name);
        MoneyEditText = (EditText) findViewById(R.id.editText_money);
        DataEditText = (EditText) findViewById(R.id.editText_data);

        delButton = (Button) findViewById(R.id.button_del);
        saveButton = (Button) findViewById(R.id.button_save);

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getWritableDatabase();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DBHelper.TABLE + " where " + DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            NameEditText.setText(userCursor.getString(1));
            MoneyEditText.setText(userCursor.getString(3));
            DataEditText.setText(userCursor.getString(4));

            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }


        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Tag);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_tag);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(6);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position==0){Tag_name="Стипендия";}
                else if(position==1){Tag_name="Подработка";}
                else if(position==2){Tag_name="Продукты";}
                else if(position==3){Tag_name="Развлечения";}
                else if(position==4){Tag_name="Образование";}
                else if(position==5){Tag_name="Услуги";}
                else if(position==6){Tag_name="Другое";}
                else{Tag_name="";}

                // показываем позиция нажатого элемента
              //  Toast.makeText(getBaseContext(), Tag_name, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });





    }
    public void onRadioButtonClicked(View view) {
        // если переключатель отмечен
        boolean checked = ((RadioButton) view).isChecked();
        // Получаем нажатый переключатель
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked){
                    Type="0";
                }
                break;
            case R.id.radioButton2:
                if (checked){
                    Type="1";
                }
                break;
        }
    }
    // выбор даты
    public void setDate(View v) {
        new DatePickerDialog(this, sd,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    };
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener sd=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear=monthOfYear+1;
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            int month = monthOfYear;
            String formattedMonth = "" + month;
            String formattedDayOfMonth = "" + dayOfMonth;

            if(month < 10){
                formattedMonth = "0" + month;
            }
            if(dayOfMonth < 10){
                formattedDayOfMonth = "0" + dayOfMonth;
            }
            DataEditText.setText(year + "-" + formattedMonth + "-" + formattedDayOfMonth);
        }
    };


    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, NameEditText.getText().toString());
        cv.put(DBHelper.COLUMN_TAG, Tag_name);
        cv.put(DBHelper.COLUMN_MONEY, MoneyEditText.getText().toString());
        cv.put(DBHelper.COLUMN_DATA, DataEditText.getText().toString());
        cv.put(DBHelper.COLUMN_RASHOD, Type);

        if (userId > 0) {
            db.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        } else {
            db.insert(DBHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DBHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

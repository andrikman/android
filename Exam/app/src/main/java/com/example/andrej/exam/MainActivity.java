package com.example.andrej.exam;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    protected ArrayList<ToDoItem> list = new ArrayList<>();
    private Context context;
    private String storageFileName = "storage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        try {
            FileInputStream fis = context.openFileInput( storageFileName );
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<ToDoItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadPreferences();



        final TodoAdapter adapter = new TodoAdapter(this, R.layout.list_item, list);

        ListView listView = (ListView) findViewById(R.id.listV);
        listView.setAdapter(adapter);



        final EditText editText = (EditText) findViewById(R.id.edit_text_item_name);
        final Button button = (Button) findViewById(R.id.button_add_item);
        final Button settings = (Button) findViewById(R.id.settings_button);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Intent myIntent = new Intent(view.getContext(), Main2Activity.class);
                myIntent.putExtra("STRING_I_NEED", list.get(position));
                myIntent.putExtra("Position", position);
                startActivity(myIntent);
            }

        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String errStr = editText.getText().toString();
                if(TextUtils.isEmpty(errStr)) {
                    editText.setError("Вы не ввели название.");
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);

                    list.add(new ToDoItem(editText.getText().toString(), date.format(cal.getTime())));
                    adapter.notifyDataSetChanged();
                    editText.setText("");

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                }
                saveToStorage();
            }
        });



    }

    @Override
    protected void onResume() {
        try {
            FileInputStream fis = context.openFileInput( storageFileName );
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<ToDoItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final TodoAdapter adapter = new TodoAdapter(this, R.layout.list_item, list);
        ListView listView = (ListView) findViewById(R.id.listV);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    private void loadPreferences() {
        SharedPreferences settingsPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.listV);
        Button setBtn = (Button) findViewById(R.id.settings_button);
        String colourId = settingsPref.getString("ColourKey","1");
        if(colourId.equals("1"))
            listView.setBackgroundColor(Color.WHITE);
        else if(colourId.equals("2"))
            listView.setBackgroundColor(Color.BLUE);
        else if(colourId.equals("3"))
            listView.setBackgroundColor(Color.RED);
        else if(colourId.equals("4"))
            listView.setBackgroundColor(Color.GREEN);
        else if(colourId.equals("5"))
            listView.setBackgroundColor(Color.GRAY);
        settingsPref.registerOnSharedPreferenceChangeListener(MainActivity.this);
        String colourTextId = settingsPref.getString("ColourButtonKey","1");
        if(colourTextId.equals("1")){
            setBtn.setBackgroundColor(Color.GRAY);
            setBtn.setTextColor(Color.BLACK);}
        else if(colourTextId.equals("2")){
            setBtn.setBackgroundColor(Color.BLUE);
            setBtn.setTextColor(Color.BLACK);}
        else if(colourTextId.equals("3")){
            setBtn.setBackgroundColor(Color.RED);
            setBtn.setTextColor(Color.BLACK);}
        else if(colourTextId.equals("4")){
            setBtn.setBackgroundColor(Color.GREEN);
            setBtn.setTextColor(Color.BLACK);}
        else if(colourTextId.equals("5"))
        {
            setBtn.setBackgroundColor(Color.BLACK);
            setBtn.setTextColor(Color.WHITE);
        }
        settingsPref.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadPreferences();
    }
    protected void saveToStorage(){
        try {
            ObjectOutputStream fileStorageStream = new ObjectOutputStream(openFileOutput(storageFileName, Context.MODE_PRIVATE));
            fileStorageStream.writeObject(list);
            fileStorageStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
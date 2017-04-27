package com.example.andrej.exam;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Main2Activity extends AppCompatActivity {
    private ToDoItem item = new ToDoItem("","");
    final Context context = this;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String strDetails;
    private String Twitadd;
    public Button btnPhoto;
    public ImageView imgPhoto;
    public int savePos;
    protected ArrayList<ToDoItem> list;
    private String storageFileName = "storage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        try {
            FileInputStream fis = context.openFileInput( storageFileName );
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<ToDoItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        item = (ToDoItem) getIntent().getSerializableExtra("STRING_I_NEED");
        savePos = (int) getIntent().getSerializableExtra("Position");
        final TextView details = (TextView) findViewById(R.id.Details);
        final TextView Twit_add = (TextView) findViewById(R.id.twit_add);


        ((TextView) findViewById(R.id.Details)).setText(item.getName());
        strDetails = details.getText().toString();

        final Button btnEdit = (Button) findViewById(R.id.button_edit_details);
        final Button share = (Button) findViewById(R.id.share_button);
        final EditText editText = (EditText) findViewById(R.id.edit_text_details);
        btnPhoto = (Button) findViewById(R.id.Camera);
        imgPhoto = (ImageView) findViewById(R.id.Photo);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Twitadd = Twit_add.getText().toString();
                String tweetUrl = "https://twitter.com/intent/tweet?text="+ strDetails+" "+Twitadd;
                Uri uri = Uri.parse(tweetUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errStr = editText.getText().toString();
                if(TextUtils.isEmpty(errStr)) {
                    editText.setError("Вы не ввели текст.");
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                    strDetails = editText.getText().toString();
                    ((TextView) findViewById(R.id.Details)).setText(strDetails);
                    item = new ToDoItem(strDetails,  date.format(cal.getTime()));
                    list.remove(savePos);
                    list.add(item);
                    saveToStorage();
                    editText.setText("");

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                }
            }
        });
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgPhoto.setImageBitmap(imageBitmap);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



}

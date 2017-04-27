package com.example.andrej.exam;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<ToDoItem> {

    public Context context;
    public LayoutInflater lInflater;
    private ArrayList<ToDoItem> objects;

    public TodoAdapter(Context context,int resource, ArrayList<ToDoItem> objects) {
        super(context,resource, objects);
        this.context = context;
        this.objects = objects;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ToDoItem t = (ToDoItem) getItem(position);
        final TodoAdapter todoadapter = this;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.Record)).setText(t.getName());



        final CheckBox checkB = (CheckBox) convertView.findViewById(R.id.checkItem);

        checkB.setTag(position);
        checkB.setChecked(t.getCh());

        checkB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                t.setCh(checkB.isChecked());
                if(context instanceof MainActivity){
                    ((MainActivity)context).saveToStorage();
                }
            }
        });

        ImageView image = (ImageView) convertView.findViewById(R.id.remove_button);

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Подтверждение");
                builder.setMessage("Вы действительно хотите удалить элемент?");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        objects.remove(t);
                        todoadapter.notifyDataSetChanged();
                        if(context instanceof MainActivity){
                            ((MainActivity)context).saveToStorage();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return convertView;
    }
}

package com.example.customlistviewimage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.customlistviewimage.MySQLiteHelper.TABLE_EXCLUSION;

public class ExclusionActivity extends AppCompatActivity {
    final Context context = this;
    private CategoryDataSource categoryHelp;
    int action;
    Toast toast;
    private Button button;
    ArrayAdapter<String> adapter;
    ArrayList<String> Exclusions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusion);

        categoryHelp = new CategoryDataSource(this);
        categoryHelp.open();
        //database.execSQL("INSERT INTO " + TABLE_EXCLUSION + " VALUES (0, 'www.aegislab.com', 0)");
        //List<Message> messages = db.getAllMessages();
        Exclusions = new ArrayList<>();
        //Exclusions.add(categoryHelp.printDb());
        Exclusions.addAll(categoryHelp.printDb());

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Exclusions);

        ListView listView = (ListView) findViewById(R.id.exclusion_list);
        listView.setAdapter(adapter);

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialogue);
                dialog.setTitle("Title...");



                // EditText edit = (EditText)findViewById(R.id.editText);

                //radio button on click return action
                RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        if (checkedId == R.id.radioButton) {
                            action = 0;
                            Log.d("action", "action: " + action);
                        } else if (checkedId == R.id.radioButton2) {
                            action = 1;
                            Log.d("action", "action: " + action);
                        }
                    }
                });
                //when ok is clicked
                Button button = (Button)dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText edit = (EditText)dialog.findViewById(R.id.editText);
                        //TextView tview = (TextView)findViewById(R.id.textView);
                        //tview.append((edit.getText()).toString() + " " + action);
                        //System.out.println("link: " + (edit.getText()).toString());
                        categoryHelp.insertDB((edit.getText().toString()), action);
                        //if add is successful
                        Exclusions.add((edit.getText().toString()));
                        if (toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getApplicationContext(), "Entered", Toast.LENGTH_SHORT);
                        toast.show();

                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                Button buttonCancel = (Button)dialog.findViewById(R.id.button2);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }


        });


    }

}

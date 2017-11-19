package com.example.customlistviewimage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private CategoryDataSource categoryHelp;

    ListView list1;
    Context context;
    Toast toast;
    //private ToggleButton toggle;
    String[] itemname = {
            "Alcohol, Drugs, and Cigarettes",
            "Gambling",
            "Gaming",
            "Pornography",
            "Social Networking and Messaging",
            "Video Streaming",
            "Shopping"
    };
    String[] categoryid = {
            "1001",
            "1002",
            "1003",
            "1004",
            "1005",
            "1006",
            "1007",
    };
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



   /*     this.setListAdapter(new ArrayAdapter<String>(
                this, R.layout.mylist,
                R.id.Itemname,itemname));*/

        categoryHelp = new CategoryDataSource(this);
        categoryHelp.open();

        list1 = (ListView) findViewById(android.R.id.list);
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for (int i=0;i<itemname.length;i++) {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("name",itemname[i]);
            hashMap.put("category",categoryid[i]+"");
            arrayList.add(hashMap);
        }
        String[] from = {"name","category"};
        int[] to = {R.id.Itemname};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.mylist,from,to)
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
               View view = super.getView(position, convertView, parent);
                ToggleButton toggle = (ToggleButton)view.findViewById(R.id.toggle_blocking);
                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            categoryHelp.updateActionPass(position);
                        else
                            categoryHelp.updateActionBlock(position);
                    }
                });

                Log.d("Data", position + "," + categoryHelp.savedActionState(position));
                if(categoryHelp.savedActionState(position) == false){
                    //toggle = (ToggleButton)findViewById(R.id.toggle_blocking);
                    toggle.setChecked(true); //pass
                }
                if(categoryHelp.savedActionState(position) == true) {
                    //toggle = (ToggleButton)findViewById(R.id.toggle_blocking);
                    toggle.setChecked(false); //block
                }


                return view;
            }
        };

        list1.setAdapter(simpleAdapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id)
            {
                System.out.println(position);
                //ToggleButton toggle = (ToggleButton)view.findViewById(R.id.toggle_blocking);

                clickHandler(view, position);
            }
        });

    }

    public void clickHandler(View v, int position){
        ToggleButton toggle = (ToggleButton)v.findViewById(R.id.toggle_blocking);
        if(toggle.isChecked()) {
            categoryHelp.updateActionBlock(position);
            toggle.setChecked(false);
            if(toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getApplicationContext(),"Blocked",Toast.LENGTH_SHORT);
            toast.show();
            //Toast.makeText(getApplicationContext(),"Blocked",Toast.LENGTH_SHORT).show();
        }
        else{
            categoryHelp.updateActionPass(position);
            toggle.setChecked(true);
            if(toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getApplicationContext(),"Allowed",Toast.LENGTH_SHORT);
            toast.show();

            //Toast.makeText(getApplicationContext(),"Allowed",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        categoryHelp.open();
        /*if(categoryHelp.savedActionState(position) == false){
            //toggle = (ToggleButton)findViewById(R.id.toggle_blocking);
            toggle.setChecked(false);
        }
        if(categoryHelp.savedActionState(position) == true) {
            //toggle = (ToggleButton)findViewById(R.id.toggle_blocking);
            toggle.setChecked(false);
        }*/

    }

    @Override
    protected void onPause() {
        categoryHelp.close();
        super.onPause();
    }
}


package com.nrkdrk.textreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Berk Can on 9.03.2017.
 */

public class NoteActivity extends Activity {

    LinearLayout not;

    @Override
    protected void onResume() {
        super.onResume();

        String[] names_of = fileList(); //arr.length
        int name_size = names_of.length;
        not=(LinearLayout)findViewById(R.id.not);
        ListView names = (ListView)findViewById(R.id.LIST);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names_of);

        if (names_of.length<1){
            not.setVisibility(View.VISIBLE);
            names.setVisibility(View.GONE);
        }else {
            not.setVisibility(View.GONE);
            names.setVisibility(View.VISIBLE);
        }

        names.setAdapter(nameAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_layout);

        ListView listView = (ListView)findViewById(R.id.LIST);
        not=(LinearLayout)findViewById(R.id.not);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String[] names_of = fileList();
                String file_name = names_of[position];
                final int data_block = 100;

                try {
                    FileInputStream fis = openFileInput(file_name);
                    InputStreamReader isr = new InputStreamReader(fis);
                    char[] data_stuff = new char[data_block];
                    String final_data = "";
                    int size;

                    try {
                        while ((size = isr.read(data_stuff)) > 0) {
                            String read_data = String.copyValueOf(data_stuff, 0, size);
                            final_data += read_data;
                            data_stuff = new char[data_block];

                        }

                        Intent new_intent = new Intent(NoteActivity.this, LoadedFile.class);
                        new_intent.putExtra("Final", final_data);
                        new_intent.putExtra("Name", file_name);
                        startActivity(new_intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}

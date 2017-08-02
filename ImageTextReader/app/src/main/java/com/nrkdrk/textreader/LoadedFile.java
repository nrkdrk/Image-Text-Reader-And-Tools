package com.nrkdrk.textreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * Created by Berk Can on 9.03.2017.
 */

public class LoadedFile extends ActionBarActivity {

    Button save;
    EditText data, name;
    String Text, FileName;
    String Final;
    final Context context = this;
    private EditText imported, imported2;
    static Bundle intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_file);

        intent = getIntent().getExtras();

        imported = (EditText) findViewById(R.id.DATA);
        imported.setText(intent.getString("Final"));
        imported2 = (EditText) findViewById(R.id.NAME);
        imported2.setText(intent.getString("Name"));

        Final = intent.getString("Final");
        save = (Button) findViewById(R.id.SAVE);
        data = (EditText) findViewById(R.id.DATA);
        name = (EditText) findViewById(R.id.NAME);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileName = name.getText().toString();

                Text = data.getText().toString();

                if (FileName.matches("")) {
                   // Toast.makeText(getBaseContext(), "Input a file name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        FileOutputStream fou = openFileOutput(FileName, MODE_WORLD_READABLE);//"text.txt"
                        OutputStreamWriter osw = new OutputStreamWriter(fou);
                        try {
                            osw.write(Text);
                            osw.flush();
                            osw.close();
                            Toast.makeText(getBaseContext(), "Text Saved!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void delete(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);// set title
        alertDialogBuilder.setTitle(intent.getString("Name")+" başlıklı notu silmek istediğinize emin misiniz?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Evet",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        FileName = name.getText().toString();
                        deleteFile(FileName);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                })
                .setNegativeButton("Vazgeç",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

package com.nrkdrk.textreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;

/**
 * Created by Berk Can on 9.03.2017.
 */

public class MenuActiviy extends Activity {

    LinearLayout cameraBtn,noteBtn,soundToTextBtn,pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        cameraBtn=(LinearLayout)findViewById(R.id.cameraBtn);
        noteBtn=(LinearLayout)findViewById(R.id.noteBtn);
        soundToTextBtn=(LinearLayout)findViewById(R.id.soundToTextBtn);
        pdf=(LinearLayout)findViewById(R.id.textToPdf);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(camera);
            }
        });

        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent note=new Intent(getApplicationContext(),NoteActivity.class);
                startActivity(note);
            }
        });

        soundToTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent soundToText=new Intent(getApplicationContext(),TextToSpeechActivity.class);
                startActivity(soundToText);
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent soundToText=new Intent(getApplicationContext(),PdfNoteSelectActivity.class);
                startActivity(soundToText);
            }
        });

    }

}

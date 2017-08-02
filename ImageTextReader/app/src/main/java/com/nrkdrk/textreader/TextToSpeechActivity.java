package com.nrkdrk.textreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Berk Can on 10.03.2017.
 */

public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Button btn_konus,textToSpeechNoteSave;
    private EditText editText1,translateValueEdt;
    private ImageButton btnKonus;
    private TextView txtKGiris;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    final Context context = this;
    public static String stringDepo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texttospeech);

        tts = new TextToSpeech(this, this);
        btn_konus= (Button) findViewById(R.id.btn_konus);
        textToSpeechNoteSave= (Button) findViewById(R.id.textToSpeechSaveNote);
        editText1= (EditText) findViewById(R.id.editText1);
        translateValueEdt= (EditText) findViewById(R.id.translateValueEdt);
        btnKonus = (ImageButton) findViewById(R.id.btnKonus);
        txtKGiris = (TextView) findViewById(R.id.txtKGiris);

        btnKonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        textToSpeechNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newValue=translateValueEdt.getText().toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Çevirilen metin için bir başlık yazınız"); //Set Alert dialog title here

                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String name = input.getEditableText().toString();
                        Toast.makeText(context,name,Toast.LENGTH_LONG).show();

                        try {
                            FileOutputStream fou = openFileOutput(name, MODE_WORLD_READABLE);//FileName "text.txt
                            OutputStreamWriter osw = new OutputStreamWriter(fou);
                            try {
                                osw.write(newValue);
                                osw.flush();
                                osw.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });

                alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        stringDepo="";
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        btn_konus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = editText1.getText().toString();
                speakOut(text);
            }
        });

    }

    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
          //  Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_SPEECH_INPUT: {

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtKGiris.setText(result.get(0));
                    translateValueEdt.setText(result.get(0));
                    stringDepo=result.get(0);
                    //speakOut(result.get(0));
                }

                break;

            }

        }

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {
            //int result= tts.setLanguage(Locale.ENGLISH);
            Locale locale = new Locale("tr", "TR");
            int result = tts.setLanguage(locale);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                btn_konus.setEnabled(true);
                String text = editText1.getText().toString();
                speakOut(text);
            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    private void speakOut(String s) {

        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }
}

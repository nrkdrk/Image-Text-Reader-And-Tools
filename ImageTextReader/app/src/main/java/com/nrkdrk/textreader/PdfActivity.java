package com.nrkdrk.textreader;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Berk Can on 11.03.2017.
 */

public class PdfActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button btnCreatePdf,pdfShare;
    public static String gelenDegerBaslık,gelenDegerİcerik;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = (TextView)findViewById(R.id.textview);
        btnCreatePdf = (Button)findViewById(R.id.create_pdf);
        mTextView.setText(gelenDegerİcerik);

    }

    private void cretaPdf(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted

                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                alert.setTitle("Oluşturulacak pdf için bir başlık giriniz."); //Set Alert dialog title here

                final EditText input = new EditText(context);
                alert.setView(input);
                input.setText(gelenDegerBaslık.toString());
                alert.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        gelenDegerBaslık = input.getEditableText().toString();
                        createPdfFle();
                    }
                });

                alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.cancel();
                    }
                });

                android.app.AlertDialog alertDialog = alert.create();
                alertDialog.show();


            } else {
                //Request Location Permission
                checkWritePermission();
            }
        }
        else {

            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
            alert.setTitle("Oluşturulacak pdf için bir başlık giriniz."); //Set Alert dialog title here

            final EditText input = new EditText(context);
            alert.setView(input);
            input.setText(gelenDegerBaslık.toString());

            alert.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    gelenDegerBaslık = input.getEditableText().toString();
                    createPdfFle();
                }
            });

            alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });

            android.app.AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }

    }

    private void createPdfFle(){
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                // Get the directory for the app's private pictures directory.
                final File file = new File(Environment.getExternalStorageDirectory(), gelenDegerBaslık+".pdf");

                if (file.exists ()) {
                    file.delete ();
                }

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);

                    PdfDocument document = new PdfDocument();
                    Point windowSize = new Point();
                    getWindowManager().getDefaultDisplay().getSize(windowSize);
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(windowSize.x, windowSize.y, 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);
                    View content = getWindow().getDecorView();
                    content.draw(page.getCanvas());
                    document.finishPage(page);
                    document.writeTo(out);
                    document.close();
                    out.flush();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PdfActivity.this, "File created: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("TAG_PDF", "File was not created: "+e.getMessage());
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE = 99;
    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Write Storage Permission Needed")
                        .setMessage("This app needs the Write Storage permission, please accept to use to write functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(PdfActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        createPdfFle();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void sharePdfFile(){
        String emailAddress[] = {getString(R.string.email)}; // email: test@gmail.com

        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), gelenDegerBaslık+".pdf"));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Paylaşılan Pdf");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Merhaba, yönlendirdiğim pdf ektedir");
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(emailIntent, "Send email using:"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.share_pdf:
                sharePdfFile();
                return true;
            case R.id.create_pdf:
                cretaPdf();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

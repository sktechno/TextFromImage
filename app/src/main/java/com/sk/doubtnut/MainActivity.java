package com.sk.doubtnut;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sk on 19/07/17.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    ImageView image;
    TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_STORAGE_PERMISSION = 10;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    Button saveData;
    ImageView reload;
    Button showData;
    CheckBox tag1,tag2,tag3,tag4,tag5;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.pickImage);
        textView = (TextView) findViewById(R.id.text);
        saveData = (Button) findViewById(R.id.saveData);
        showData = (Button) findViewById(R.id.showData);
        reload = (ImageView) findViewById(R.id.reloadImage);

        tag1 = ( CheckBox) findViewById(R.id.tag1);
        tag2 = ( CheckBox) findViewById(R.id.tag2);
        tag3 = ( CheckBox) findViewById(R.id.tag3);
        tag4 = ( CheckBox) findViewById(R.id.tag4);
        tag5 = ( CheckBox) findViewById(R.id.tag5);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        checkPermission();

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowData.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                File imagFile = new File(mCurrentPhotoPath);
                if (imagFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagFile.getAbsolutePath());
                    mImageBitmap = myBitmap;
                    image.setImageBitmap(myBitmap);
                    detectText();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void detectText(){

        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

        Frame imageFrame = new Frame.Builder()
                .setBitmap(mImageBitmap)
                .build();


        String imageText = "";


        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = textBlock.getValue();                   // return string
        }

        textView.setText(imageText);
        saveData.setEnabled(true);

        final String finalImageText = imageText;
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags  = "";
                if (tag1.isChecked())
                    tags = tags+",tag1";
                if (tag2.isChecked())
                    tags = tags+",tag2";
                if (tag3.isChecked())
                    tags = tags+",tag3";
                if (tag4.isChecked())
                    tags = tags+",tag4";
                if (tag5.isChecked())
                    tags = tags+",tag5";
                tags = tags.replaceFirst("\\,","");

                if (TextUtils.isEmpty(tags)) {
                    Toast.makeText(MainActivity.this, "Please select some tag", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(textView.getText())){
                    Toast.makeText(MainActivity.this, "Please Take image properly ", Toast.LENGTH_LONG).show();
                    return;
                }

                DataBase.getInstance(MainActivity.this).insertorUpdate(tags,mCurrentPhotoPath, textView.getText().toString());
                textView.setText("");
                tag1.setChecked(false);
                tag2.setChecked(false);
                tag3.setChecked(false);
                tag4.setChecked(false);
                tag5.setChecked(false);
                image.setImageResource(0);
                saveData.setEnabled(false);
            }
        });




    }

    private void openCamera(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(Intent.createChooser(cameraIntent, "Take image"), REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "doubnut" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Permission required")
                        .setMessage("Doubtnut require storage access to save  image.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION &&
                grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {

            Toast.makeText(this, "Doubtnut require storage access to save profile image. Please enable it from settings",Toast.LENGTH_LONG).show();
        }
    }

}

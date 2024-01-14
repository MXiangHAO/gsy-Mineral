package com.example.chapter_blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class test extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private jp.wasabeef.richeditor.RichEditor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mEditor = findViewById(R.id.editor);

        ImageButton insertImageButton = findViewById(R.id.action_insert_image);
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.focusEditor();
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Get the screen width
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            // Get the scaled bitmap
            Bitmap scaledBitmap = getScaledBitmap(selectedImageUri, screenWidth);

            // Convert the bitmap to Base64
            String base64Image = encodeImageToBase64(scaledBitmap);

            // Build the HTML code for the image with width set to screen width
            String htmlCode = String.format("<img src=\"data:image/png;base64,%s\" style=\"width: %dpx; height: auto;\">",
                    base64Image, screenWidth);

            // Insert the image into the editor
            mEditor.setHtml(htmlCode);
        }
    }

    private Bitmap getScaledBitmap(Uri uri, int screenWidth) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            float scale = (float) screenWidth / originalBitmap.getWidth();
            int scaledHeight = (int) (originalBitmap.getHeight() * scale);

            return Bitmap.createScaledBitmap(originalBitmap, screenWidth, scaledHeight, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



}


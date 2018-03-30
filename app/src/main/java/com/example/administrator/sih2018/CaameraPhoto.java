package com.example.administrator.sih2018;

/**
 * Created by Administrator on 7/17/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaameraPhoto {
    File photoFile;

    final String TAG = this.getClass().getSimpleName();

    private String photoPath;

    public String getPhotoPath() {
        return photoPath;
    }

    public Uri getPhotoUri(){
        return Uri.fromFile(photoFile);
    }
    private Context context;
    public CaameraPhoto(Context context){
        this.context = context;
    }

    public Intent takePhotoIntent() throws IOException {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (in.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
             photoFile = createImageFile();
            System.out.println("in take photo");

            // Continue only if the File was successfully created
            if (photoFile != null) {
                System.out.println("in take photo");
                in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                System.out.println("in take photo");
            }
        }
        return in;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        System.out.println("in create image");
        return image;
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}

package com.example.suraj.androidgoogleimagesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class FullImageActivity extends ActionBarActivity {
    private ImageView image;
    private ProgressBar progressBar;
    private ShareActionProvider shareActionProvider;
    private Image imageObj;
    private boolean imageLoaded = false;
    private boolean shareIntentCreated = false;
    private PhotoViewAttacher photoViewAttacher = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        initMembers();
        displayImage();
    }

    private void initMembers() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        progressBar = (ProgressBar)findViewById(R.id.pbImageLoad);
        progressBar.setVisibility(View.VISIBLE);
        image = (ImageView)findViewById(R.id.ivFullImage);
    }

    private void displayImage() {
        Intent intent = getIntent();
        imageObj = (Image)intent.getSerializableExtra("image");
        setTitle(imageObj.getTitle());

        int height = imageObj.getHeight();
        int width = imageObj.getWidth();

        int displayWidth = Utility.getDisplayWidth(this);
        int displayHeight = height*displayWidth/width;

        Picasso.with(this).load(imageObj.getUrl()).error(R.drawable.ic_errorimage).resize(displayWidth,displayHeight).centerInside().placeholder(R.drawable.ic_defaultimage).into(image, new Callback.EmptyCallback() {

            @Override
            public void onSuccess() {
                imageLoaded = true;
                setIntents(imageObj.getUrl());
                progressBar.setVisibility(View.GONE);
                photoViewAttacher.update();
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }

        });

        // The MAGIC happens here!
        photoViewAttacher = new PhotoViewAttacher(image);

    }


    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void setIntents(String fullUrl) {
        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_SEND);

        Uri uri = getLocalBitmapUri(image);
        myIntent.putExtra(Intent.EXTRA_STREAM, uri );
        myIntent.setType("image/*");
        setShareIntent(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_image, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    private void setShareIntent(Intent intent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(intent);
            shareIntentCreated = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (!shareIntentCreated)
                setIntents(imageObj.getUrl());
            return true;
        } else if (id == R.id.action_browse) {
            String url = imageObj.getContextUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}

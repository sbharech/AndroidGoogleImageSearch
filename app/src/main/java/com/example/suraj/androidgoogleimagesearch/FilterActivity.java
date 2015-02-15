package com.example.suraj.androidgoogleimagesearch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class FilterActivity extends ActionBarActivity {
    private Spinner colorType;
    private Spinner imageSize;
    private Spinner imageType;
    private EditText siteType;
    private Button saveButton;
    private Button cancelButton;

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initMembers();
        setAdaptersAndListeners();
    }

    private void initMembers() {
        colorType = (Spinner)findViewById(R.id.spColorFilter);
        imageSize = (Spinner)findViewById(R.id.spImageSize);
        imageType = (Spinner)findViewById(R.id.spImageType);
        siteType = (EditText)findViewById(R.id.etSiteFilter);
        saveButton = (Button)findViewById(R.id.btnSave);
        cancelButton = (Button)findViewById(R.id.btnCancel);
        filter = (Filter)getIntent().getSerializableExtra("filter");
        if (filter == null)
            filter = new Filter();

    }

    private void setAdaptersAndListeners() {


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setSiteFilter(siteType.getText().toString());
                Intent returnValue = new Intent();
                returnValue.putExtra("filter",filter);
                setResult(RESULT_OK, returnValue);
                finish();
            }
        });

        colorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.setColor(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.setImageType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter.setImageSize(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (filter != null) {
            String imageTypeStr = filter.getImageType();
            String[] imageTypeStrArray = getResources().getStringArray(R.array.image_type);
            int i = 0;
            for (;i < imageTypeStrArray.length; ++i) {
                if (imageTypeStrArray[i].equals(imageTypeStr)) {
                    imageType.setSelection(i);
                    break;
                }
            }

            String colorTypeStr = filter.getColor();
            String[] colorTypeStrArray = getResources().getStringArray(R.array.color_filter);
            i = 0;
            for (;i < colorTypeStrArray.length; ++i) {
                if (colorTypeStrArray[i].equals(colorTypeStr)) {
                    colorType.setSelection(i);
                    break;
                }
            }

            String imageSizeStr = filter.getImageSize();
            String[] imageSizeStrArray = getResources().getStringArray(R.array.image_size);
            i = 0;
            for (;i < imageSizeStrArray.length; ++i) {
                if (imageSizeStrArray[i].equals(imageSizeStr)) {
                    imageSize.setSelection(i);
                    break;
                }
            }
            if (filter.getSiteFilter() != null)
                siteType.setText(filter.getSiteFilter());

        } else {
            imageType.setSelection(0);
            colorType.setSelection(0);
            imageSize.setSelection(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

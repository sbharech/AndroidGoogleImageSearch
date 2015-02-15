package com.example.suraj.androidgoogleimagesearch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class FilterFragment extends DialogFragment {
    private Spinner colorType;
    private Spinner imageSize;
    private Spinner imageType;
    private EditText siteType;
    private Button saveButton;
    private Button cancelButton;
    private Filter filter;

    static FilterFragment newInstance(Filter filter) {
        FilterFragment ff = new FilterFragment();
        Bundle args = new Bundle();
        args.putSerializable("filter", filter);
        ff.setArguments(args);

        return ff;
    }

    public interface FilterFragmentDialogListener {
        void updateDialogValue(Bundle result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container);

        initMembers(view);
        setAdaptersAndListeners();

        return view;
    }

    private void initMembers(View view) {
        getDialog().setTitle(R.string.advanced_search_options);
        colorType = (Spinner)view.findViewById(R.id.spColorFilter);
        imageSize = (Spinner)view.findViewById(R.id.spImageSize);
        imageType = (Spinner)view.findViewById(R.id.spImageType);
        siteType = (EditText)view.findViewById(R.id.etSiteFilter);
        saveButton = (Button)view.findViewById(R.id.btnSave);
        cancelButton = (Button)view.findViewById(R.id.btnCancel);

        filter = (Filter)getArguments().getSerializable("filter");
        if (filter == null)
            filter = new Filter();
    }

    private void setAdaptersAndListeners() {


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent returnValue = new Intent();
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setSiteFilter(siteType.getText().toString());
                FilterFragmentDialogListener dialogListener = (FilterFragmentDialogListener)getActivity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("filter", filter);
                dialogListener.updateDialogValue(bundle);
                dismiss();
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


}

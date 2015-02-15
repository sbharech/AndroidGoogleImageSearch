package com.example.suraj.androidgoogleimagesearch;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by suraj on 08/02/15.
 */
public class SearchResultListAdapter extends ArrayAdapter<Image> {
    static class ViewHolder {
        ImageView image;
        //TextView title;
    }


    SearchResultListAdapter(Context context, ArrayList<Image> imageList) {
        super(context, 0, imageList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Image image = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_result_grid_image, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView)convertView.findViewById(R.id.ivImage);
            //viewHolder.title = (TextView)convertView.findViewById(R.id.tvImageTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        int height = image.getHeight();
        int width = image.getWidth();

        int displayWidth = Utility.getDisplayWidth(getContext()) / 3;
        int targetHeight = height * displayWidth / width;

        viewHolder.image.setImageResource(0);
        Picasso.with(getContext()).load(image.getThumbnail()).resize(displayWidth,targetHeight).centerCrop().into(viewHolder.image);

        //viewHolder.title.setText(Html.fromHtml(image.getTitle()));
        return convertView;
    }
}

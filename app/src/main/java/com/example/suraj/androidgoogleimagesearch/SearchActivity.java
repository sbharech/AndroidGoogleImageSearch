package com.example.suraj.androidgoogleimagesearch;

import android.app.SearchManager;
import android.content.Context;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import com.etsy.android.grid.StaggeredGridView;


public class SearchActivity extends ActionBarActivity implements FilterFragment.FilterFragmentDialogListener {
    //private EditText queryTextView;
    //private Button   searchButton;
    private StaggeredGridView searchResultGrid;
    private SearchView searchView;
    private ProgressBar progressView;

    private ArrayList<Image> resultImageList;
    private SearchResultListAdapter searchResultGridAdapter;
    private Filter filter = null;
    private String currentQueryString = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initMembers();
        setAdaptersAndListeners();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void saveRecentSearchQuery(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSearchQuerySuggestionProvider.AUTHORITY, RecentSearchQuerySuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            currentQueryString = intent.getStringExtra(SearchManager.QUERY);
            searchResultGridAdapter.clear();
            saveRecentSearchQuery(currentQueryString);
            fetchQueryResult(0);
        }
    }

    private void initMembers() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);

        searchResultGrid = (StaggeredGridView)findViewById(R.id.gvSearchResult);
        progressView = (ProgressBar)findViewById(R.id.pbSearchProgress);
        progressView.setVisibility(View.GONE);

        resultImageList = new ArrayList<Image>();
        searchResultGridAdapter = new SearchResultListAdapter(this, resultImageList);
        searchResultGrid.setAdapter(searchResultGridAdapter);

        loadFiltersFromFile();

    }


    class CustomEndlessScrollListener extends EndlessScrollListener {
        public CustomEndlessScrollListener(int visibleThreshold, int itemPerPage) {
            super(visibleThreshold,itemPerPage);
        }
        // Defines the process for actually loading more data based on page
        public void onLoadMore(int page, int totalItemsCount) {
            fetchQueryResult(page * itemPerPage);
        }
    }

    private void fetchQueryResult(int start) {
        if (currentQueryString.isEmpty())
            return;

        if (start == 0) {
            progressView.setVisibility(View.VISIBLE);
            setTitle(currentQueryString);
        }


        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(currentQueryString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (encodedString == null)
            return;

        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + encodedString);

        if (start > 0) {
            finalQuery.append("&start=" + start);
        }

        if (filter != null) {
            if (!filter.getImageSize().equals("any"))
                finalQuery.append("&imgsz=" + filter.getImageSize());
            if (!filter.getColor().equals("any"))
                finalQuery.append("&imgcolor=" + filter.getColor());
            if (!filter.getImageType().equals("any"))
                finalQuery.append("&imgtype=" + filter.getImageType());
            if (filter.getSiteFilter() != null && filter.getSiteFilter().length() > 0)
                finalQuery.append("&as_sitesearch=" + filter.getSiteFilter());
        }

        Log.i("Calling URL: ", finalQuery.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(finalQuery.toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //searchResultGridAdapter.clear();
                    progressView.setVisibility(View.GONE);
                    JSONArray imageJSONArray = response.getJSONObject("responseData").getJSONArray("results");
                    searchResultGridAdapter.addAll(Image.populateImagesFromJSON(imageJSONArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("Info", "Success....");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressView.setVisibility(View.GONE);
                new NetworkErrorDialog(SearchActivity.this).createNetworkErrorDialog();
            }
        });
    }

    private void setAdaptersAndListeners() {

        searchResultGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = (Image)parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), FullImageActivity.class);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        searchResultGrid.setOnScrollListener(new CustomEndlessScrollListener(2,8));
    }

    private void loadFiltersFromFile() {
        File dir = getFilesDir();
        File filterFile = new File(dir, "filter.txt");

        try {
            ArrayList<String> filters = (ArrayList<String>)FileUtils.readLines(filterFile);
            filter = new Filter();

            for (String option : filters) {
                String name = option.substring(0,option.indexOf(":"));
                String value = option.substring(option.indexOf(":") + 1);

                switch (name) {
                    case "sizetype":
                        filter.setImageSize(value);
                        break;
                    case "color":
                        filter.setColor(value);
                        break;
                    case "imagetype":
                        filter.setImageType(value);
                        break;
                    case "site":
                        filter.setSiteFilter(value);
                        break;
                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFiltersToFile() {
        if (filter != null) {
            File dir = getFilesDir();
            File filterFile = new File(dir, "filter.txt");

            try {
                ArrayList<String> toWrite = new ArrayList<String>();
                toWrite.add("sizetype:" + filter.getImageSize());
                toWrite.add("color:" + filter.getColor());
                toWrite.add("imagetype:" + filter.getImageType());
                toWrite.add("site:" + filter.getSiteFilter());

                FileUtils.writeLines(filterFile, toWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);


        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }
        });

        return true;
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment ff = FilterFragment.newInstance(filter);
        ff.show(fm, "filterFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("filter",filter);
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateDialogValue(Bundle result) {
        filter = (Filter)result.getSerializable("filter");
        writeFiltersToFile();
    }
}

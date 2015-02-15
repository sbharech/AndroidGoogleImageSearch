package com.example.suraj.androidgoogleimagesearch;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by suraj on 11/02/15.
 */
public class RecentSearchQuerySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.suraj.androidgoogleimagesearch.suggestion.authority";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchQuerySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

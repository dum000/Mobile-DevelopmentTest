package parrtim.applicationfundamentals.providers;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSearchSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =
            RecentSearchSuggestionsProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

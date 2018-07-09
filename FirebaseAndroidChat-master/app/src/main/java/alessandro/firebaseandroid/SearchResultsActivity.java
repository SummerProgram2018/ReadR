package alessandro.firebaseandroid;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class SearchResultsActivity extends Activity {

    DatabaseTable db = new DatabaseTable(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Cursor c = db.getWordMatches(query, null);
            //process Cursor and display results

            Log.e("database", "how many boys" + Integer.toString(c.getColumnCount()));
        }
    }
}

package alessandro.firebaseandroid.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import alessandro.firebaseandroid.R;
import alessandro.firebaseandroid.model.UserModel;

public class FriendListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView friendListView;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private FriendListAdapter friendListAdapter;
    private ArrayList<UserModel> friendList = new ArrayList<UserModel>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_layout);

        initFriendList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FriendListActivity.this.finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFriendList() {
        friendList.add(new UserModel("Daniel", "temp", "temp"));
        friendList.add(new UserModel("Ju", "temp", "temp"));

        friendListView = (ListView) findViewById(R.id.friend_list);
        friendListAdapter = new FriendListAdapter(this, friendList);

        friendListView.setAdapter(friendListAdapter);
        friendListView.setTextFilterEnabled(false);

        // use to enable search view popup text
        //friendListView.setTextFilterEnabled(true);

        // set up click listener
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0 && position <= friendList.size()) {
                    handelListItemClick((UserModel)friendListAdapter.getItem(position - 1));
                }
            }
        });
    }

    private void handelListItemClick(UserModel user) {
        // close search view if its visible
        if (searchView != null && searchView.isShown()) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }

        // pass selected user and sensor to share activity
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        friendListAdapter.getFilter().filter(newText);

        // use to enable search view popup text
//        if (TextUtils.isEmpty(newText)) {
//            friendListView.clearTextFilter();
//        }
//        else {
//            friendListView.setFilterText(newText.toString());
//        }

        return true;
    }
}
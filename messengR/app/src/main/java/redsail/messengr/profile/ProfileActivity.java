package redsail.messengr.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import redsail.messengr.BaseActivity;
import redsail.messengr.MainActivity;
import redsail.messengr.R;

public class ProfileActivity extends BaseActivity {
    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_main) {
            Intent mIntent = new Intent(this, MainActivity.class);
            startActivity(mIntent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private Button mInfoButton;
    private Button mWantButton;
    private Button mOwnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_user_info);
        mWantButton = (Button) findViewById(R.id.button_user_wanted);
        mOwnButton = (Button) findViewById(R.id.button_user_owned);
        // Click listeners
        mInfoButton.setOnClickListener(this);
        mWantButton.setOnClickListener(this);
        mOwnButton.setOnClickListener(this);


    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(ProfileActivity.this, ProfileInfoActivity.class));
        finish();
    }
    private void viewWanted() {
        Log.d(TAG, "ViewWishlist");
        startActivity(new Intent(ProfileActivity.this, ProfileWantedActivity.class));
        finish();
    }

    private void viewOwned() {
        Log.d(TAG, "ViewOwnedBooks");
        startActivity(new Intent(ProfileActivity.this, ProfileOwnActivity.class));
        finish();
    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_user_info) {
            viewInfo();
        } else if (i == R.id.button_user_wanted) {
            viewWanted();
        } else if (i == R.id.button_user_owned) {
            viewOwned();
        }
    }

}

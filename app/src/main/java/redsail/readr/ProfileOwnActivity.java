package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ProfileOwnActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ProfileOwnActivity";
    private Button mInfoButton;
    private Button mWantButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_owned);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_user_info);
        mWantButton = (Button) findViewById(R.id.button_user_wanted);

        // Click listeners
        mInfoButton.setOnClickListener(this);
        mWantButton.setOnClickListener(this);


    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(ProfileOwnActivity.this, ProfileInfoActivity.class));
        finish();
    }
    private void viewWanted() {
        Log.d(TAG, "ViewWishlist");
        startActivity(new Intent(ProfileOwnActivity.this, ProfileWantedActivity.class));
        finish();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_user_info) {
            viewInfo();
        } else if (i == R.id.button_user_wanted) {
            viewWanted();
        }
    }

}

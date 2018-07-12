package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ProfileWantedActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ProfileWantedActivity";
    private Button mInfoButton;
    private Button mOwnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_wanted);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_user_info);
        mOwnButton = (Button) findViewById(R.id.button_user_owned);
        // Click listeners
        mInfoButton.setOnClickListener(this);
        mOwnButton.setOnClickListener(this);


    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(ProfileWantedActivity.this, ProfileInfoActivity.class));
        finish();
    }

    private void viewOwned() {
        Log.d(TAG, "ViewOwnedBooks");
        startActivity(new Intent(ProfileWantedActivity.this, ProfileOwnActivity.class));
        finish();
    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_user_info) {
            viewInfo();
        } else if (i == R.id.button_user_owned) {
            viewOwned();
        }
    }

}
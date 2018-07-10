package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BookDisActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BookDisActivity";

    private Button mInfoButton;
    private Button mOwnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_dis);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_book_info);
        mOwnButton = (Button) findViewById(R.id.button_book_owner);
        // Click listeners
        mInfoButton.setOnClickListener(this);
        mOwnButton.setOnClickListener(this);

    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(BookDisActivity.this, BookInfoActivity.class));
        finish();
    }

    private void viewOwner() {
        Log.d(TAG, "ViewOwner");
        startActivity(new Intent(BookDisActivity.this, BookOwnerActivity.class));
        finish();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_book_info) {
            viewInfo();
        } else if (i == R.id.button_book_owner) {
            viewOwner();
        }
    }
}


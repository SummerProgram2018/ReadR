package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BookActivity";

    private Button mInfoButton;
    private Button mDisButton;
    private Button mOwnButton;
    private TextView mInfoView;
    private TextView mDiscusView;
    private TextView mOwnView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_book_info);
        mDisButton = (Button) findViewById(R.id.button_book_dis);
        mOwnButton = (Button) findViewById(R.id.button_book_owner);
        // Click listeners
        mInfoButton.setOnClickListener(this);
        mDisButton.setOnClickListener(this);
        mOwnButton.setOnClickListener(this);

    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(BookActivity.this, BookInfoActivity.class));
        finish();
    }
    private void viewDiscussion() {
        Log.d(TAG, "ViewDiscussionBoard");
        startActivity(new Intent(BookActivity.this, BookDisActivity.class));
        finish();
    }

    private void viewOwner() {
        Log.d(TAG, "ViewOwner");
        startActivity(new Intent(BookActivity.this, BookOwnerActivity.class));
        finish();
    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_book_info) {
            viewInfo();
        } else if (i == R.id.button_book_dis) {
            viewDiscussion();
        } else if (i == R.id.button_book_owner) {
            viewOwner();
        }
    }
}

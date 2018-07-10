package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BookInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BookInfoActivity";

    private Button mDisButton;
    private Button mOwnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        // Views
        mDisButton = (Button) findViewById(R.id.button_book_dis);
        mOwnButton = (Button) findViewById(R.id.button_book_owner);
        // Click listeners
        mDisButton.setOnClickListener(this);
        mOwnButton.setOnClickListener(this);

    }

    private void viewDiscussion() {
        Log.d(TAG, "ViewDiscussionBoard");
        startActivity(new Intent(BookInfoActivity.this, BookDisActivity.class));
        finish();
    }

    private void viewOwner() {
        Log.d(TAG, "ViewOwner");
        startActivity(new Intent(BookInfoActivity.this, BookOwnerActivity.class));
        finish();
    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_book_dis) {
            viewDiscussion();
        } else if (i == R.id.button_book_owner) {
            viewOwner();
        }
    }
}


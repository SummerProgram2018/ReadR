package redsail.readr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookOwnerActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BookActivity";

    private Button mInfoButton;
    private Button mDisButton;
    private Button mPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_owner);

        // Views
        mInfoButton = (Button) findViewById(R.id.button_book_info);
        mDisButton = (Button) findViewById(R.id.button_book_dis);
        mPurchase = (Button) findViewById(R.id.book_purchase);
        // Click listeners
        mInfoButton.setOnClickListener(this);
        mDisButton.setOnClickListener(this);
        mPurchase.setOnClickListener(this);
    }

    private void viewInfo() {
        Log.d(TAG, "ViewInformation");
        startActivity(new Intent(BookOwnerActivity.this, BookInfoActivity.class));
        finish();
    }
    private void viewDiscussion() {
        Log.d(TAG, "ViewDiscussionBoard");
        startActivity(new Intent(BookOwnerActivity.this, BookDisActivity.class));
        finish();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_book_info) {
            viewInfo();
        } else if (i == R.id.button_book_dis) {
            viewDiscussion();
        } else if (i == R.id.book_purchase) {
            startActivity(new Intent(BookOwnerActivity.this, BuyActivity.class));
            finish();
        }
    }
}
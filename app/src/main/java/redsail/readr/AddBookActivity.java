package redsail.readr;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddBookActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AddBookActivity";
    private Button mAddButton;
    private Button mCancelButton;
    private EditText mBookName;
    private EditText mISBN;
    private EditText mAuthor;
    private EditText mGenre;
    private EditText mDescription;
    private EditText mPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // View
        mAddButton = (Button) findViewById(R.id.button_book_add);
        mCancelButton = (Button) findViewById(R.id.button_add_cancel);
        mBookName = (EditText) findViewById(R.id.edit_bookname);
        mISBN = (EditText) findViewById(R.id.edit_isbn);
        mAuthor = (EditText) findViewById(R.id.edit_author);
        mGenre = (EditText) findViewById(R.id.edit_genre);
        mDescription = (EditText) findViewById(R.id.edit_des);
        mPrice = (EditText) findViewById(R.id.edit_price);

        mAddButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mBookName.getText().toString())) {
            mBookName.setError("Required");
            result = false;
        } else {
            mBookName.setError(null);
        }

        if (TextUtils.isEmpty(mISBN.getText().toString())) {
            mISBN.setError("Required");
            result = false;
        } else if (!TextUtils.isDigitsOnly(mISBN.getText())){
            mISBN.setError("Digits Only!");
        }else {
            mISBN.setError(null);
        }

        if (TextUtils.isEmpty(mAuthor.getText().toString())) {
            mAuthor.setError("Required");
            result = false;
        } else {
            mAuthor.setError(null);
        }

        if (TextUtils.isEmpty(mPrice.getText().toString())) {
            mPrice.setError("Required");
            result = false;
        } else if (!TextUtils.isDigitsOnly(mPrice.getText())){
            mISBN.setError("Digits Only!");
        } else {
            mPrice.setError(null);
        }
        return result;
    }
    private void addBookProfile() {
        if (!validateForm()) {
            return;
        }
        String bookTitle = mBookName.getText().toString();
        String genre = mGenre.getText().toString();
        String ISBN = mISBN.getText().toString();
        String author = mAuthor.getText().toString();
        String description = mDescription.getText().toString();
        float price = Float.valueOf(mPrice.getText().toString());
        Book book = new Book(bookTitle, genre, ISBN, author, description, price);
        Log.d(TAG, "AddBook");
        finish();
    }
    private void cancelAddBook() {
        finish();
    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_book_add) {
            addBookProfile();
        } else if (i == R.id.button_add_cancel) {
            cancelAddBook();
        }
    }
}

package redsail.readr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TradeActivity";

    private Button mConfirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        // Views
        mConfirmButton = (Button) findViewById(R.id.button_confirm_purchase);

    }

    private void confirmPurchase() {
        Log.d(TAG, "BookPurchase");
    }


    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_confirm_purchase) {
            confirmPurchase();
        }
    }
}

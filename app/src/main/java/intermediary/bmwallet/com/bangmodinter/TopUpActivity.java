package intermediary.bmwallet.com.bangmodinter;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import intermediary.bmwallet.com.bangmodinter.Class.JSONParser;
import intermediary.bmwallet.com.bangmodinter.Class.SessionManager;


public class TopUpActivity extends Activity implements CardReader.AccountCallback  {

    TextView topUpText;
    String topUpAmount = "0";
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public CardReader mCardReader;
    JSONObject jObj;
    SessionManager ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        topUpText = (TextView) findViewById(R.id.top_up_amount);
        mCardReader = new CardReader(this);

        enableReaderMode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "You selected Phone", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                ss = new SessionManager(this);
                ss.logoutUser();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void enableReaderMode() {
        Activity activity = TopUpActivity.this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, mCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Activity activity = TopUpActivity.this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }

    public void topUpOnClick(View v){
        if(topUpAmount.length()<4) {
            if(topUpAmount.equals("0")){
                topUpAmount = "";
            }
            switch (v.getId()) {
                case R.id.o:
                    topUpAmount += "0";
                    break;
                case R.id.double_o:
                    if(topUpAmount.equals("") || topUpAmount.length()==4){
                        topUpAmount += "0";
                    }else {
                        topUpAmount += "00";
                    }
                    break;
                case R.id.one:
                    topUpAmount += "1";
                    break;
                case R.id.two:
                    topUpAmount += "2";
                    break;
                case R.id.three:
                    topUpAmount += "3";
                    break;
                case R.id.four:
                    topUpAmount += "4";
                    break;
                case R.id.five:
                    topUpAmount += "5";
                    break;
                case R.id.six:
                    topUpAmount += "6";
                    break;
                case R.id.seven:
                    topUpAmount += "7";
                    break;
                case R.id.eight:
                    topUpAmount += "8";
                    break;
                case R.id.nine:
                    topUpAmount += "9";
                    break;
                case R.id.backspace:
                    if(!topUpAmount.equals("0")) {
                        if(topUpAmount.length()>1){
                            topUpAmount = topUpAmount.substring(0, topUpAmount.length() - 1);
                        }else{
                            topUpAmount = "0";
                        }
                    }
                    break;
                case R.id.clear:
                    topUpAmount = "0";
                    break;

            }
            topUpText.setText(topUpAmount);
        }else{
            switch (v.getId()){
                case R.id.backspace:
                    if(!topUpAmount.equals("0")) {
                        topUpAmount = topUpAmount.substring(0, topUpAmount.length() - 1);
                        if(topUpAmount.length()==1){
                            topUpAmount = "0";
                        }
                    }
                    break;
                case R.id.clear:
                    topUpAmount = "0";
                    break;

            }
            topUpText.setText(topUpAmount);
        }
    }

    @Override
    public void onAccountReceived(String account) {
        account = account.substring(1);
        String[] out = account.split(",");
        String url[] = new String[1];
        url[0] = "https://secure.bm-wallet.com/mobile_connect/inter_topup.php";
        String customerId = out[0];
        ss = new SessionManager(TopUpActivity.this);
        jObj = new JSONObject();
        try {
            jObj.put("cust_id",customerId);
            jObj.put("inter_id", ss.getUserDetails().getUserId());
            jObj.put("total",topUpAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        topUpAmount = "0";

        new SendOrder().execute(url);


    }

    private class SendOrder extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            String url= urls[0];

            JSONObject json = JSONParser.postToUrlObj(url, jObj, TopUpActivity.this);

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            topUpText.setText(topUpAmount);
            String status = "";
            try {
                status = json.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(status.equals("1")){
                Toast.makeText(TopUpActivity.this, "Top Up Successful", Toast.LENGTH_LONG).show();
            }else if(status.equals("0")){
                Toast.makeText(TopUpActivity.this,"Not Enough Money",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(TopUpActivity.this,"Action Fail",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }
}

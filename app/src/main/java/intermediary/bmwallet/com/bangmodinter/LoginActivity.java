package intermediary.bmwallet.com.bangmodinter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import intermediary.bmwallet.com.bangmodinter.Class.JSONParser;
import intermediary.bmwallet.com.bangmodinter.Class.SessionManager;
import intermediary.bmwallet.com.bangmodinter.Class.Utility;

public class LoginActivity extends Activity {

    EditText userText, passText;
    Button loginBtn;
    String username,password;
    JSONObject jObj = new JSONObject();
    static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText = (EditText)findViewById(R.id.username);
        passText = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {


                if ((!TextUtils.isEmpty(userText.getText().toString()) && !TextUtils.isEmpty(passText.getText().toString()))){

                    try {
                        username = userText.getText().toString().trim();
                        password = Utility.md5(passText.getText().toString().trim());
                        if (username != null && password != null) {
                            jObj.put("username", username);
                            jObj.put("password", password);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url[] = new String[1];
                    url[0] = "https://apps.bm-wallet.com/inter/login.php";
                    new JSONPost().execute(url);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Please Insert Username and Password.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class JSONPost extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            String url = urls[0];

            // Getting JSON from URL
            JSONObject json = JSONParser.postToUrlObj(url, jObj, LoginActivity.this);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("STATUS").equalsIgnoreCase("1")) {
                    SessionManager ss = new SessionManager(LoginActivity.this);
                    ss.createLoginSession(json);

                    Intent intent = new Intent(LoginActivity.this, TopUpActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Wrong Username or Password.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}


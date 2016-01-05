package intermediary.bmwallet.com.bangmodinter.Class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intermediary.bmwallet.com.bangmodinter.LoginActivity;

/**
 * Created by Thanatkorn on 9/28/2014.
 */
public class SessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserData";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    JSONObject jObj;
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(JSONObject info){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        JSONObject user = null;

        try {
            user = new JSONObject(info.getString("INTERINFO"));
        } catch (JSONException e) {
            e.printStackTrace();
        }




        try {


            editor.putString("userId", user.getString("INTERID"));
            editor.putString("firstName", user.getString("FIRSTNAME"));
            editor.putString("lastName", user.getString("LASTNAME"));

        } catch (JSONException e) {
            System.out.println("Fail here");
            e.printStackTrace();
        }
        // commit changes
        editor.commit();
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */


    public User getUserDetails(){
        User user = new User();
        user.setUserId(pref.getString("userId","0"));
        user.setPrefix(pref.getString("prefix","Mr."));
        user.setFirstName(pref.getString("firstName","Firstname"));
        user.setLastName(pref.getString("lastName","Lastname"));
        user.setImgPath(pref.getString("imgPath","non"));

        return user;
    }







    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}

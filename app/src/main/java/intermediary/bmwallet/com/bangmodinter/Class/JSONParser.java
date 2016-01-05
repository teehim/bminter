package intermediary.bmwallet.com.bangmodinter.Class;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Thanatkorn on 9/28/2014.
 */
public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    // constructor
    public JSONArray getJSONFromUrl(String url,Context context) {
        String result = "";
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient client = new MyHttpClient(context);
            HttpGet get = new HttpGet(url);
            // Execute the GET call and obtain the response
            HttpResponse getResponse = null;
            getResponse = client.execute(get);


            HttpEntity httpEntity = getResponse.getEntity();
            is = httpEntity.getContent();

            result = convertInputStreamToString(is);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = decode(result);

        // try parse the string to a JSON object
        try {
            jArr = new JSONArray(result.substring(result.indexOf("["), result.lastIndexOf("]") + 1));
            System.out.println(decode(jArr.getJSONObject(0).getString("firstname")));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString() + result);
        }
        // return JSON String
        return jArr;
    }

    public static JSONObject postToUrlObj(String url, JSONObject in, Context context){

        String result = "";



        DefaultHttpClient client = new MyHttpClient(context);
        HttpPost httpPost = new HttpPost(url);
        String json = "";

        try {

            json = in.toString();

            httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            httpPost.setHeader("test", json.toString());

            HttpResponse httpResponse = client.execute(httpPost);
            is = httpResponse.getEntity().getContent();

            result = convertInputStreamToString(is);
            result = decode(result);
            System.out.println(result);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jObj = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString() + result);
        }
        return jObj;

    }

    public static void postToUrlVoid(String url, JSONObject in, Context context){

        String result = "";



        DefaultHttpClient client = new MyHttpClient(context);
        HttpPost httpPost = new HttpPost(url);
        String json = "";

        try {

            json = in.toString();

            httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            httpPost.setHeader("test", json.toString());

            HttpResponse httpResponse = client.execute(httpPost);
            is = httpResponse.getEntity().getContent();

            result = convertInputStreamToString(is);
            result = decode(result);
            System.out.println(result);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void normalPost(String url,ArrayList<NameValuePair> nameValuePairs, Context context) throws UnsupportedEncodingException {

        String result = "";



        DefaultHttpClient client = new MyHttpClient(context);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        String json = "";

        try {

            //json = in.toString();

           /* httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));*/

            HttpResponse httpResponse = client.execute(httpPost);
            is = httpResponse.getEntity().getContent();

            result = convertInputStreamToString(is);
            result = decode(result);
            System.out.println(result);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static JSONArray postToUrlArr(String url, JSONObject in, Context context){

        String result = "";



        DefaultHttpClient client = new MyHttpClient(context);
        HttpPost httpPost = new HttpPost(url);
        String json = "";

        try {

            json = in.toString();

            httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            httpPost.setHeader("test", json.toString());

            HttpResponse httpResponse = client.execute(httpPost);
            is = httpResponse.getEntity().getContent();

            result = convertInputStreamToString(is);
            result = decode(result);
            System.out.println(result);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jArr = new JSONArray(result.substring(result.indexOf("["), result.lastIndexOf("]") + 1));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString() + result);
        }
        return jArr;

    }

    static final String decode(final String in)
    {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1)
        {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

    private static String convertInputStreamToString(InputStream is) throws IOException{
        String result = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "n");
        }
        is.close();
        result = sb.toString();
        return result;

    }

}

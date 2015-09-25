package mychef.mychef.backend;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.victorsima.uber.model.Prices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

public class PlaidAccess {

    private PlaidCallback callback;

    public void getCategories(String username, String password, ArrayList<Dish> dishes,
                              PlaidCallback callback)
            throws IOException, JSONException {
        new RetrievePlaidData().execute(dishes);
        this.callback = callback;
    }

    private class RetrievePlaidData extends AsyncTask<ArrayList<Dish>, Void, Void> {

        private Exception exception;

        protected Void doInBackground(ArrayList<Dish>... disheses) {
            ArrayList<Dish> dishes = disheses[0];

            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("https://tartan.plaid.com/connect");

            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("client_id", "test_id"));
            nameValuePair.add(new BasicNameValuePair("secret", "test_secret"));
            nameValuePair.add(new BasicNameValuePair("username", "plaid_test"));
            nameValuePair.add(new BasicNameValuePair("password", "plaid_good"));
            nameValuePair.add(new BasicNameValuePair("type", "wells"));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");

                JSONObject js = new JSONObject(responseString);
                JSONArray transactions = js.getJSONArray("transactions");
                for (int i = 0; i < transactions.length(); i++) {
                    JSONArray categories = transactions.getJSONObject(i).getJSONArray("category");
                    Log.d("testy2", categories.toString());
                    if (categories.length() > 2) {
                        String a = (String) categories.get(2);
                        Log.d("testy3", a);
                        for (Dish dish : dishes) {
                            if (dish.getCategory().equalsIgnoreCase(a)) {
                                dish.setPriority(dish.getPriority() + 1);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // log exception
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            callback.plaidCallback();
        }
    }
}
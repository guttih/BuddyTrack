package gicalls;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.guttih.buddytrack.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GuðjónHólm on 17.6.2016.
 *
 * Usage:
 * Make you UI implement the CallBackListener and add the callback function.
 *
 * For example make an activity use this function to make a API call and return the result to the activity.
 *
 *   -Start by making the activity implement the callbacklistener like so:
 *      public class MainActivity extends AppCompatActivity implements CallBackListener{...
 *
 *  -Then implement the callback(String response) in the activity class.
 *
 *  -finally declare an instance of the ApiCall and execute a call like this
 *      ApiCall task = new ApiCall();
 *      ApiCall.setListener(this);
 *      task.execute();
 *
 */
public class ApiCall extends AsyncTask<Void, Void, String> {
    static CallBackListener mListener;
    private Exception exception;
    private String URL_API_GETCAR;

    private ApiCall(){}
    public ApiCall(CallBackListener listener, String url){
        super();
        URL_API_GETCAR = url;
        setListener(listener);
    }
    private static void setListener(CallBackListener listener) {
        mListener = listener;
    }
    protected void onPreExecute() {
        /*progressBar.setVisibility(View.VISIBLE);
        responseView.setText("");*/
    }

    protected String doInBackground(Void... urls) {
        /*String email = emailText.getText().toString();*/
        // Do some validation here

        try {

            URL url = new URL(URL_API_GETCAR);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
    @Override
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        /*progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
        responseView.setText(response);*/
        Log.d("res", response);
        mListener.ApiCallback(response);
    }
}

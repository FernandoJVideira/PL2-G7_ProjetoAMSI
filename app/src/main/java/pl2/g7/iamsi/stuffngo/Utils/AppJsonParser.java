package pl2.g7.iamsi.stuffngo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

public class AppJsonParser {

    public static String parserJsonLogin(String response) {

        String token = null;

        try{
            JSONObject login = new JSONObject(response);

            if(login.has("auth_key")){
                token = login.getString("auth_key");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }


    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
}

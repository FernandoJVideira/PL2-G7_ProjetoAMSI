package pl2.g7.iamsi.stuffngo.Models;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Utils.AppJsonParser;

public class SingletonStuffNGo {

    private static SingletonStuffNGo INSTANCE = null;
    private static RequestQueue volleyQueue = null;
    private LoginListener loginListener = null;
    private String token;
    private static final String UrlAPILogin = "http://10.0.2.2:8081/api/user/login";

    public static synchronized SingletonStuffNGo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SingletonStuffNGo(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return INSTANCE;
    }

    private SingletonStuffNGo(Context context) {

        //gerarDadosDinamico();
        //livroBD = new LivroBDHelper(context);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void loginAPI(final String username, final String password){

        StringRequest request = new StringRequest(Request.Method.GET, UrlAPILogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                token = AppJsonParser.parserJsonLogin(response);

                if (loginListener != null) {
                    loginListener.onValidateLogin(token, username);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64Encode(username + ":" + password));
                System.out.println("Authorization: " + base64Encode("Basic " + username + ":" + password));
                return headers;
            }
        };
        volleyQueue.add(request);
    }

    private static String base64Encode(String value) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder()
                        .encodeToString(value.getBytes(StandardCharsets.UTF_8.toString()));
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }
}

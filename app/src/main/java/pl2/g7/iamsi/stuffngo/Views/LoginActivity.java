package pl2.g7.iamsi.stuffngo.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Models.SingletonStuffNGo;
import pl2.g7.iamsi.stuffngo.R;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private static final int MIN_PASS = 8;
    private static final int MIN_UNAME = 2;
    EditText etUname;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUname = findViewById(R.id.etUname);
        etPassword = findViewById(R.id.etPassword);
        SingletonStuffNGo.getInstance(this).setLoginListener(this);
    }

    public void onClickLogin(View view) {

        if (!isUsernameValido(etUname) || !isPasswordValida(etPassword)) {
            Toast.makeText(this, "Email ou Password Inválidos", Toast.LENGTH_SHORT).show();
            return;
        }
        SingletonStuffNGo.getInstance(this).loginAPI(etUname.getText().toString(), etPassword.getText().toString());
    }

    public boolean isUsernameValido(EditText etMail) {

        String usernameToTxt = etMail.getText().toString();

        return !usernameToTxt.isEmpty() && usernameToTxt.length() >= MIN_UNAME;
    }

    public boolean isPasswordValida(EditText etPWD) {

        String pwdToText = etPWD.getText().toString();

        return pwdToText.length() >= MIN_PASS;
    }

    @Override
    public void onValidateLogin(String token) {
        System.out.println("Token: " + token);
        if (token != null) {
            MainActivity.TOKEN = token;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Email ou Password Inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}
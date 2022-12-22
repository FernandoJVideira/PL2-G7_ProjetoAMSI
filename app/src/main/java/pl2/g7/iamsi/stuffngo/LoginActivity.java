package pl2.g7.iamsi.stuffngo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final int MIN_PASS = 4;
    EditText etEmail;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    public void onClickLogin(View view) {

        if (!isEmailValido(etEmail) || !isPasswordValida(etPassword)) {
            Toast.makeText(this, "Email ou Password Inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(MenuMainActivity.EMAIL, etEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    public boolean isEmailValido(EditText etMail) {

        String emailToText = etMail.getText().toString();

        return !emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches();
    }

    public boolean isPasswordValida(EditText etPWD) {

        String pwdToText = etPWD.getText().toString();

        return pwdToText.length() >= MIN_PASS;
    }
}
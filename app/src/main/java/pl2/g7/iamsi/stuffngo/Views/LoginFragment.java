package pl2.g7.iamsi.stuffngo.Views;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl2.g7.iamsi.stuffngo.Listeners.LoginListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.R;

public class LoginFragment extends Fragment implements LoginListener {
    private static final int MIN_PASS = 8;
    private static final int MIN_UNAME = 2;
    EditText etUname;
    EditText etPassword;
    Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etUname = view.findViewById(R.id.etUname);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btLogin);
        Singleton.getInstance(getContext()).setLoginListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUsernameValido(etUname) || !isPasswordValida(etPassword)) {
                    Toast.makeText(getContext(), "Email ou Password Inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                Singleton.getInstance(getContext()).loginAPI(etUname.getText().toString(), etPassword.getText().toString(), getContext());
            }
        });

        return view;
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
    public void onValidateLogin(String token, String username) {
        System.out.println("Token: " + token);
        if (token != null) {
            //Guarda o valor do Token nas Shared Preferences(name: "Shared Preferences") como Token ;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Token",token);
            editor.putString("Username",username);
            editor.apply();
            //Muda para a página de Home
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loggedIn(true);
            ((MainActivity) getActivity()).replaceFragment(new HomeFragment());
        } else {
            Toast.makeText(getContext(), "Email ou Password Inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}
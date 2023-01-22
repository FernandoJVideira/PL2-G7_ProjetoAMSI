package pl2.g7.iamsi.stuffngo.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Models.User;
import pl2.g7.iamsi.stuffngo.R;


public class EditProfileFragment extends Fragment implements UserListener {

    public static final int MIN_CHAR_NOME = 2;
    public static final int CHAR_NIF = 9;
    public static final int MIN_CHAR_TELEMOVEL = 9;
    public static final int MAX_CHAR_TELEMOVEL = 13;


    private EditText etNome, etNif, etTelemovel, etUsername , etEmail;
    private TextView btnSave, btnCancel, txtUsername;
    private User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etNome = view.findViewById(R.id.etNome);
        etNif = view.findViewById(R.id.etNIF);
        etTelemovel = view.findViewById(R.id.etTel);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        Singleton.getInstance(getContext()).setUserListener(this);
        Singleton.getInstance(getContext()).getUserDataAPI(getContext());
        user = Singleton.getInstance(getContext()).getUser();

        if (user != null){
            txtUsername.setText(user.getUsername());
            etUsername.setText(user.getUsername());
            etEmail.setText(user.getEmail());
            etNome.setText(user.getNome());
            etNif.setText(user.getNif());
            etTelemovel.setText(user.getTelemovel());
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserValido()) {
                    String nome = etNome.getText().toString();
                    String username = etUsername.getText().toString().toLowerCase();
                    String email = etEmail.getText().toString().toLowerCase();
                    String nif = etNif.getText().toString();
                    String telemovel = etTelemovel.getText().toString();

                    if (user != null) {
                        user.setUsername(username);
                        user.setEmail(email);
                        user.setNome(nome);
                        user.setNif(nif);
                        user.setTelemovel(telemovel);
                        System.out.println(user.getUsername());

                        Singleton.getInstance(getContext()).editarDadosAPI(user, getContext());
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });

        return view;
    }

    private boolean isUserValido() {
        String nome = etNome.getText().toString();
        String nif = etNif.getText().toString();
        String username = etUsername.getText().toString().toLowerCase();
        String email = etEmail.getText().toString().toLowerCase();
        String telemovel = etTelemovel.getText().toString();

        if (username.length() < MIN_CHAR_NOME) {
            etUsername.setError("Username inválido");
            return false;
        }

        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            etEmail.setError("Email inválido");
            return false;
        }

        if (nome.length() < MIN_CHAR_NOME) {
            etNome.setError("Nome Invalido");
            return false;
        }
        if (nif.length() != CHAR_NIF) {
            etNif.setError("Nif Inválido");
            return false;
        }
        else if (!nif.matches("^[0-9]*$"))
        {
            etNif.setError("Nif Inválido");
            return false;
        }

        if ((telemovel.length() < MIN_CHAR_TELEMOVEL && !telemovel.matches("^[0-9]*$")) || telemovel.length() > MAX_CHAR_TELEMOVEL) {
            etTelemovel.setError("Telemóvel Inválido");
            return false;
        }
        return true;
    }

    @Override
    public void onRefreshUser(User user) {
        if(user != null){
            txtUsername.setText(user.getUsername());
            etNome.setText(user.getNome());
            etNif.setText(user.getNif());
            etTelemovel.setText(user.getTelemovel());
        }
    }
}
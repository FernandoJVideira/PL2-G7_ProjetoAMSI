package pl2.g7.iamsi.stuffngo.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl2.g7.iamsi.stuffngo.Listeners.UserListener;
import pl2.g7.iamsi.stuffngo.Models.Singleton;
import pl2.g7.iamsi.stuffngo.Models.User;
import pl2.g7.iamsi.stuffngo.R;


public class ProfileFragment extends Fragment implements UserListener {

    private User user;

    private TextView txtUsername, txtEmail, txtNome, txtNif, txtTelemovel, txtMorada, txtPerfil, txtPassword;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtNome = view.findViewById(R.id.txtNome);
        txtNif = view.findViewById(R.id.txtNIF);
        txtTelemovel = view.findViewById(R.id.txtTel);
        txtMorada = view.findViewById(R.id.txtMorada);
        txtPerfil = view.findViewById(R.id.txtPerfil);
        txtPassword = view.findViewById(R.id.txtEncomendas);

        Singleton.getInstance(getContext()).setUserListener(this);
        Singleton.getInstance(getContext()).getUserDataAPI(getContext());
        user = Singleton.getInstance(getContext()).getUser();

        if (user != null){
            txtUsername.setText(user.getUsername());
            txtEmail.setText(user.getEmail());
            txtNome.setText(user.getNome());
            txtNif.setText(user.getNif());
            txtTelemovel.setText(user.getTelemovel());
        }

        txtPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new EditProfileFragment());
            }
        });

        txtMorada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MoradasFragment());
            }
        });

        txtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(getContext(), EncomendaActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRefreshUser(User user) {
        if(user != null){
            txtUsername.setText(user.getUsername());
            txtEmail.setText(user.getEmail());
            txtNome.setText(user.getNome());
            txtNif.setText(user.getNif());
            txtTelemovel.setText(user.getTelemovel());
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
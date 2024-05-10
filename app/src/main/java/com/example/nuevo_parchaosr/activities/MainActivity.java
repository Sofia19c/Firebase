package com.example.nuevo_parchaosr.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nuevo_parchaosr.R;
import com.example.nuevo_parchaosr.activities.acces.LoginActivity;
import com.example.nuevo_parchaosr.activities.amigos.AmigosFragment;
import com.example.nuevo_parchaosr.activities.map.MapaFragment;
import com.example.nuevo_parchaosr.activities.user.UserFragment;
import com.example.nuevo_parchaosr.databinding.MainLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BasicActivity {
    private MainLayoutBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AmigosFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mapa:
                    replaceFragment(new MapaFragment());
                    break;
                case R.id.amigos:
                    replaceFragment(new AmigosFragment());
                    break;
                case R.id.ajustes:
                    replaceFragment(new UserFragment());
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        if (currentFragment instanceof MapaFragment) {
            DialogInterface.OnClickListener positiveListener = (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            };
            DialogInterface.OnClickListener negativeListener = (dialog, which) -> {
                Log.e("Negativo", "Usuario continua en linea");
            };
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}

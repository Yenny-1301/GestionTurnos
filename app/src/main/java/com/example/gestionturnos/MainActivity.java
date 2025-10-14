package com.example.gestionturnos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestionturnos.databinding.ActivityMainBinding;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity implements TurnosFragment.NavigationHost, ServiciosFragment.NavigationHost {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // verificar si el usuario hizo log in
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        //si no
        if(!isLoggedIn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        DynamicColors.applyToActivitiesIfAvailable(MainActivity.this.getApplication());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //cargar turnos como HOME
        if (savedInstanceState == null) { // Solo si no hay estado guardado
            replaceFragment(new TurnosFragment(), false);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            int itemId = item.getItemId();

            if (itemId == R.id.turnosFragment) {
                replaceFragment(new TurnosFragment(), false);
            } else if (itemId == R.id.serviciosFragment) {
                replaceFragment(new ServiciosFragment(), false);
            } else if (itemId == R.id.perfilFragment) {
                replaceFragment(new PerfilFragment(), false);
            }

            return  true;
        });
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        replaceFragment(fragment, addToBackStack);
    }
    public void replaceFragment(Fragment fragment, boolean addToBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frameLayout, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
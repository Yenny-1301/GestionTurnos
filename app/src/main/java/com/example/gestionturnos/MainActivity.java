package com.example.gestionturnos;

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

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //replaceFragment(new TurnosFragment());
        replaceFragment(new PerfilFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            int itemId = item.getItemId();

            if (itemId == R.id.turnos) {
                replaceFragment(new TurnosFragment());
            } else if (itemId == R.id.servicios) {
                replaceFragment(new ServiciosFragment());
            } else if (itemId == R.id.perfil) {
                replaceFragment(new PerfilFragment());
            }

            return  true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
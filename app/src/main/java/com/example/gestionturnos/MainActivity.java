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

public class MainActivity extends AppCompatActivity implements TurnosFragment.NavigationHost {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TurnosFragment(), false);

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
package com.reconciliationhouse.android.loverekindle;

import android.os.Bundle;
import android.provider.FontRequest;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;
import com.reconciliationhouse.android.loverekindle.utils.ProgressBarHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if(UserPreferences.getRole(getApplicationContext())!=null) {
            if (UserPreferences.getRole(getApplicationContext()).equals(String.valueOf(UserModel.Role.Regular))) {
                navView.getMenu().findItem(R.id.listOfRequestFragment).setVisible(false);
            } else if (UserPreferences.getRole(getApplicationContext()).equals(String.valueOf(UserModel.Role.Counsellor))) {
                navView.getMenu().findItem(R.id.listOfRequestFragment).setVisible(false);
            } else {
                navView.getMenu().findItem(R.id.listOfRequestFragment).setVisible(true);
            }
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.settingsFragment) {
                    navView.setVisibility(View.GONE);

                } else if (destination.getId() == R.id.loginFragment) {
                    navView.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.signUpFragment) {
                    navView.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.forgetPasswordFragment) {
                    navView.setVisibility(View.GONE);
                } else {
                    navView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doExitApp();
    }

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
//            super.onBackPressed();
            finish();
        }
    }

}

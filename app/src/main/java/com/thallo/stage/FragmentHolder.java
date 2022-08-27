package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gyf.immersionbar.ImmersionBar;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryViewModel;
import com.thallo.stage.databinding.ActivityFragmentHolderBinding;
import com.thallo.stage.R;

import java.util.List;

public class FragmentHolder extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFragmentHolderBinding binding;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.background)
                .autoStatusBarDarkModeEnable(true,0.2f)
                .init();
        binding = ActivityFragmentHolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent=getIntent();
        NavHostFragment navHostController= (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        NavController navController = navHostController.getNavController();
        switch (intent.getStringExtra("page"))
        {
            case "HISTORY":
                navController.navigate(R.id.historyFragment,null);
                binding.toolbar.setTitle(R.string.pop_history);
                break;
            case "BOOKMARK":
                navController.navigate(R.id.bookmarkFragment, null);
                binding.toolbar.setTitle(R.string.pop_star);
                break;
            case "DOWNLOAD":
                navController.navigate(R.id.downloadFragment, null);
                binding.toolbar.setTitle(R.string.pop_download);
                break;
            case "ADDONS":
                navController.navigate(R.id.addonsManagerFragment, null);
                binding.toolbar.setTitle(R.string.pop_addons);

                break;
            case "ABOUT":
                navController.navigate(R.id.aboutFragment, null);
                binding.toolbar.setTitle(R.string.setting_about);
                break;
        }

        setSupportActionBar(binding.toolbar);




        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }



    @Override
    public void onBackPressed() {
        finish();
    }


}
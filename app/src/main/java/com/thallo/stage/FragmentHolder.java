package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

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

import java.util.List;

public class FragmentHolder extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFragmentHolderBinding binding;

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
        Intent intent=getIntent();
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
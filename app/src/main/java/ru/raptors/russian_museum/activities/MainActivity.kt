package ru.raptors.russian_museum.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_games, R.id.navigation_ar
            )
        )
        setSupportActionBar(binding.toolbar)

        NavigationUI.setupWithNavController(binding.collapsingToolbarLayout, binding.toolbar,
                navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)
        // setupActionBarWithNavController(navController, appBarConfiguration)
        // navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.tkdev.dogs.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.tkdev.dogs.R
import com.tkdev.dogs.common.FactoryInjector
import com.tkdev.dogs.viewmodels.DogsViewModel

class DogsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val dogsViewModel: DogsViewModel by viewModels {
        FactoryInjector.provideDogsViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        dogsViewModel.snackBarMessage.observe(
            this,
            { message ->
                message.getContentIfNotHandled()?.let {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        it, Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.secondaryColor, null))
                        .setTextColor(resources.getColor(R.color.secondaryTextColor, null))
                        .show()
                }
            }
        )

        dogsViewModel.toolbarTitle.observe(
            this,
            { title ->
                toolbar.title = title
            }
        )
    }
}
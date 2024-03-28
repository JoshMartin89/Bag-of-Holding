package com.joshmartin.bagofholding

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import com.joshmartin.bagofholding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Making the toolbar the action bar to populate menu
        setSupportActionBar(binding.toolbar)

        //Adding Main Activity to the view model BagViewModel for data handling of weight
        val bagViewModel = ViewModelProvider(this)[BagViewModel::class.java]

        //Setting up observer for LiveData weight
        bagViewModel.updateBagWeight() //Init bag weight
        val weightObserver = Observer<Double> {result-> binding.weightNotification.text =
            "$result lb / 400"
        }
        bagViewModel.getWeight().observe(this, weightObserver)

    }

        //creating the overflow menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController : NavController = Navigation.findNavController(this, R.id.nav_host) //Creating Nav Controller for menu
                return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item) //Navigate based on menu item id, the same id as fragments
    }
}
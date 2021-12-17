package com.example.imdb

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.imdb.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), HomeFragment.OnHeadlineSelectedListener {

    var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.let {
            it?.setupWithNavController(navController)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(fragment: Fragment) {

        if (fragment is HomeFragment) {
            fragment.setOnHeadlineSelectedListener(this)
        }
    }

    override fun onArticleSelected() {
    }

}

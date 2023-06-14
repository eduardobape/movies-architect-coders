package com.example.mymovies.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mymovies.R
import com.example.mymovies.databinding.ActivityNavHostBinding

class NavHostActivity : AppCompatActivity() {

	private lateinit var navController: NavController
	private lateinit var binding: ActivityNavHostBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNavHostBinding.inflate(layoutInflater)
		setContentView(binding.root)
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController
		setActionBar()
	}

	private fun setActionBar() {
		setSupportActionBar(binding.toolbarNavHostActivity)
		setupActionBarWithNavController(navController)
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}
}
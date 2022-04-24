package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ProductDetailsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            finish()

        val details = ProductDetailsFragment.newInstance(intent.extras!!)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, details)
            .commit()
    }

}
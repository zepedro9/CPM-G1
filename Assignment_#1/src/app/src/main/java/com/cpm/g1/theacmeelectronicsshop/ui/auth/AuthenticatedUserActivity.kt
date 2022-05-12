package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.security.clearUserUUID
import com.cpm.g1.theacmeelectronicsshop.security.getUserUUID

open class AuthenticatedUserActivity: AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        if(getUserUUID(applicationContext) == "")
            logout()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("finish", true);
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP; // To clean up all activities
        clearUserUUID(applicationContext)
        startActivity(intent);
        finish();
    }
}
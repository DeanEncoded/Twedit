package io.deanencoded.twedit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import io.deanencoded.twedit.helpers.ThemeChecker
import io.deanencoded.twedit.helpers.getSharedPref

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeChecker().darkModeChecker(this)

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            // check if we have an access token in shared prefs... if we do... navigate to home
            Log.e("pref",getSharedPref("access_token").toString())
            if(getSharedPref("access_token") != null){
                // go home
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            }else {
                // go to intro activity
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish()
        }, 1000)

    }
}

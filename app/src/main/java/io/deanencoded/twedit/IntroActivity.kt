package io.deanencoded.twedit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btn_create_account.setOnClickListener {
            startActivity(Intent(this@IntroActivity,CreateAccountActivity::class.java))
        }

        tv_login.setOnClickListener {
            startActivity(Intent(this@IntroActivity,LoginActivity::class.java))
        }
    }
}

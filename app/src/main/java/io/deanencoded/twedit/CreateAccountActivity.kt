package io.deanencoded.twedit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.VolleyError
import com.deanencoded.ease.helpers.KeyboardHelper
import io.deanencoded.twedit.helpers.setSharedPref
import io.deanencoded.twedit.repo.Auth
import io.deanencoded.twedit.repo.VolleyCallback
import kotlinx.android.synthetic.main.activity_create_account.*
import org.json.JSONObject

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_create.setOnClickListener {
            // hide the keyboard & show the overlay to block controls
            KeyboardHelper().hideKeyboard(this,btn_create)
            overlay.visibility = View.VISIBLE
            Toast.makeText(this,"Creating your account",Toast.LENGTH_SHORT).show()
            // try adn create the account.
            val name = et_name.text.toString()
            val username = et_username.text.toString()
            val password = et_password.text.toString()
            Auth().signup(this,name = name,username = username,password = password, callback = object:
                VolleyCallback {
                override fun onSuccess(result: JSONObject){
                    // we got something
                    if(result.getBoolean("success")){
                        // we got what we needed!
                        // store the id, name, username and access_token in savedprefs
                        val userdata = result.getJSONObject("userdata")
                        setSharedPref("id",userdata.getInt("id").toString())
                        setSharedPref("name",userdata.getString("name"))
                        setSharedPref("username",userdata.getString("username"))
                        setSharedPref("access_token",userdata.getString("access_token"))

                        // we're good to go home.
                        val i = Intent(this@CreateAccountActivity,HomeActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)
                        finish()
                    }else{
                        // display the message from the response
                        Toast.makeText(this@CreateAccountActivity,result.getString("message"),Toast.LENGTH_SHORT).show()
                        overlay.visibility = View.GONE
                    }
                }
                override fun onError(result: VolleyError) {
                    // probably a network error
                    Toast.makeText(this@CreateAccountActivity,"Couldn't create your account. Try again",Toast.LENGTH_SHORT).show()
                    Log.e("res",result.message)
                    overlay.visibility = View.GONE
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_login.et_username
import kotlinx.android.synthetic.main.activity_login.overlay
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity,CreateAccountActivity::class.java))
        }

        btn_login.setOnClickListener {
            KeyboardHelper().hideKeyboard(this,btn_login)
            overlay.visibility = View.VISIBLE

            val username = et_username.text.toString()
            val password = et_password.text.toString()

            Auth().login(this,username = username,password = password, callback = object:
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
                        val i = Intent(this@LoginActivity,HomeActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)
                        finish()
                    }else{
                        // display the message from the response
                        Toast.makeText(this@LoginActivity,result.getString("message"),
                            Toast.LENGTH_SHORT).show()
                        overlay.visibility = View.GONE
                    }
                }
                override fun onError(result: VolleyError) {
                    // probably a network error
                    Toast.makeText(this@LoginActivity,"Couldn't log in. Try again",
                        Toast.LENGTH_SHORT).show()
                    Log.e("res",result.message)
                    overlay.visibility = View.GONE
                }
            })
        }
    }
}

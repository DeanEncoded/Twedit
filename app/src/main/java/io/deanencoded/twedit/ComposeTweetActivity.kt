package io.deanencoded.twedit

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.VolleyError
import com.deanencoded.ease.helpers.KeyboardHelper
import io.deanencoded.twedit.helpers.ThemeChecker
import io.deanencoded.twedit.helpers.loadRoundedPhoto
import io.deanencoded.twedit.helpers.getSharedPref
import io.deanencoded.twedit.repo.Tweet
import io.deanencoded.twedit.repo.VolleyCallback
import kotlinx.android.synthetic.main.activity_compose_tweet.*
import org.json.JSONObject

class ComposeTweetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChecker().darkModeChecker(this)
        setContentView(R.layout.activity_compose_tweet)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val id = getSharedPref("id").toString().toInt()
        val token = getSharedPref("access_token").toString()
        // load the user photo
        loadRoundedPhoto(drawee_user_photo,TweditAPI(userID = id).data.photo)

        // request focus on our edittext
        et_tweet_text.requestFocus()

        btn_tweet.setOnClickListener {
            // progress dialog
            val progressDialog = ProgressDialog(this@ComposeTweetActivity)
            progressDialog.setMessage("Sending out tweet")
            progressDialog.show()

            // lets send out the tweet.
            KeyboardHelper().hideKeyboard(this@ComposeTweetActivity,btn_tweet)
            val tweetText = et_tweet_text.text.toString()
            if(tweetText.isNotEmpty()){
                // we're good to go
                Tweet().new(this,id,token,tweetText,object: VolleyCallback{
                    override fun onSuccess(result: JSONObject) {
                        if(result.getBoolean("success")){
                            // tweet was sent!
                            progressDialog.dismiss()
                            Toast.makeText(this@ComposeTweetActivity,"Tweet sent!",Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this@ComposeTweetActivity,result.getString("message"),Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(result: VolleyError) {
                        Toast.makeText(this@ComposeTweetActivity,result.message,Toast.LENGTH_SHORT).show()
                    }

                })
            }else{
                Toast.makeText(this@ComposeTweetActivity,"Type in something to tweet, eh?", Toast.LENGTH_SHORT).show()
            }
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

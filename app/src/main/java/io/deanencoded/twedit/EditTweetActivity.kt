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
import kotlinx.android.synthetic.main.activity_edit_tweet.*
import kotlinx.android.synthetic.main.activity_edit_tweet.drawee_user_photo
import kotlinx.android.synthetic.main.activity_edit_tweet.toolbar
import org.json.JSONObject

class EditTweetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChecker().darkModeChecker(this)
        setContentView(R.layout.activity_edit_tweet)

        val id = getSharedPref("id").toString().toInt()
        val token = getSharedPref("access_token").toString()
        // load the user photo
        loadRoundedPhoto(drawee_user_photo,TweditAPI(userID = id).data.photo)
        
        // get intent
        val i = intent
        // add the tweet text to the edittext
        et_tweet_text.setText(i.getStringExtra("tweet_text"))
        
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        
        btn_update_tweet.setOnClickListener {
            val progressDialog = ProgressDialog(this@EditTweetActivity)
            progressDialog.setMessage("Updating tweet")
            progressDialog.show()
            // lets send out the tweet.
            KeyboardHelper().hideKeyboard(this@EditTweetActivity,btn_update_tweet)
            val tweetText = et_tweet_text.text.toString()
            if(tweetText.isNotEmpty()){
                // we're good to go
                Tweet().edit(this,id,token,i.getIntExtra("tweet_id",0),tweetText,object: VolleyCallback {
                    override fun onSuccess(result: JSONObject) {
                        if(result.getBoolean("success")){
                            progressDialog.dismiss()
                            // tweet was sent!
                            Toast.makeText(this@EditTweetActivity,"Tweet updated!", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this@EditTweetActivity,result.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(result: VolleyError) {
                        Toast.makeText(this@EditTweetActivity,result.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }else{
                Toast.makeText(this@EditTweetActivity,"Type in something to tweet, eh?", Toast.LENGTH_SHORT).show()
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

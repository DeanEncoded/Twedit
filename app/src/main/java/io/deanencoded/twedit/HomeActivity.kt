package io.deanencoded.twedit


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.navigation.NavigationView
import io.deanencoded.twedit.adapters.TweetAdapter
import io.deanencoded.twedit.helpers.*
import io.deanencoded.twedit.items.Tweet
import io.deanencoded.twedit.repo.Tweets
import io.deanencoded.twedit.repo.VolleyCallback
import kotlinx.android.synthetic.main.activity_create_account.toolbar
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {


    // userdata
    private lateinit var userName: String
    private lateinit var userUsername: String
    private lateinit var accessToken: String

    // drawer
    private lateinit var myToggle: ActionBarDrawerToggle
    private lateinit var myDrawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    // tweets :: recyclerview
    private lateinit var tweetsAdapter: TweetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)

        ThemeChecker().darkModeChecker(this)

        setContentView(R.layout.activity_home)

        // userdata
        userName = getSharedPref("name").toString()
        userUsername = getSharedPref("username").toString()
        accessToken = getSharedPref("access_token").toString()

        showUserDetails() // show the users details in appropriate widgets

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        myDrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_view)
        myToggle = ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close)
        myDrawerLayout.addDrawerListener(myToggle)
        myToggle.syncState()

        drawee_user_photo.setOnClickListener {
            if (myDrawerLayout.isDrawerOpen(navigationView)) {
                myDrawerLayout.closeDrawer(navigationView)
            } else {
                myDrawerLayout.openDrawer(navigationView)
            }
        }

        // navigation
        // logging out
        nav_logout.setOnClickListener {
            // remove saved prefs with user data and launch intro.
            if (clearSharedPrefs()) {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeActivity, IntroActivity::class.java))
                finish()
            }
        }

        // compose fab
        fab_compose.setOnClickListener {
            // start the Compose activity
            startActivity(Intent(this@HomeActivity, ComposeTweetActivity::class.java))
        }

        // handling the tweets recyclerview
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview_tweets.layoutManager = layoutManager

        // swipe to refresh
        tweets_refresh_layout.setOnRefreshListener {
            showTweets()
        }

        // show tweets
        showTweets()

        // theme switch
        nav_themeswitch.setOnClickListener { darkModeSwitch() }
    }

    // dark mode switch
    fun darkModeSwitch() {
        // its time to switch dark mode on or off.
        val darkModeOn = getSharedPref("DARK_MODE_ON", true) as Boolean
        if (darkModeOn) {
            setSharedPref("DARK_MODE_ON", value = false, isBoolean = true)
        } else {
            setSharedPref("DARK_MODE_ON", value = true, isBoolean = true)
        }
        // restart the activity
        restartActivity()
    }

    private fun restartActivity() {
        val intent = Intent(this@HomeActivity, HomeActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun showUserDetails() {
        loadRoundedPhoto(
            drawee_user_photo,
            TweditAPI(userID = getSharedPref("id").toString().toInt()).data.photo
        )
        loadRoundedPhoto(
            drawee_user_photo_nav,
            TweditAPI(userID = getSharedPref("id").toString().toInt()).data.photo
        )
        tv_name.text = userName
        tv_username.text = "@$userUsername"
    }

    private fun showTweets() {
        Tweets().all(
            this,
            getSharedPref("id").toString().toInt(),
            accessToken,
            object : VolleyCallback {
                override fun onSuccess(result: JSONObject) {
                    tweets_refresh_layout.isRefreshing = false
                    if (result.getBoolean("success")) {
                        // let's display our tweets.
                        // convert tweets in json to tweet items first
                        val tweets: MutableList<Tweet> = ArrayList()

                        val tweetsArray = result.getJSONArray("tweets")
                        for (i in 0 until tweetsArray.length()) {
                            val t = tweetsArray.getJSONObject(i)
                            val tweet = Tweet(
                                tweetId = t.getInt("id"),
                                tweetBy = t.getInt("tweet_by"),
                                name = t.getString("name"),
                                username = t.getString("username"),
                                text = t.getString("tweet_text"),
                                time = t.getInt("tweet_time"),
                                edited = t.getBoolean("edited")
                            )
                            tweets.add(tweet)
                        }

                        // now display the tweets in the recyclerview
                        tweetsAdapter = TweetAdapter(this@HomeActivity, tweets)
                        tweetsAdapter.setOnItemClickListener(object :
                            TweetAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                // this is pointing to the edit tweet button
                                // start the edit tweet activity with intended intent data
                                // only start it if the tweet is editable (Checking the timestamps)
                                val thistweet = tweets[position]
                                val i = Intent(this@HomeActivity, EditTweetActivity::class.java)
                                i.putExtra("tweet_text", thistweet.text)
                                i.putExtra("tweet_id", thistweet.tweetId)

                                val currentTime = System.currentTimeMillis() / 1000
                                Log.e(
                                    "timestuff",
                                    "CurrentTime: $currentTime and Tweet time: ${thistweet.time}"
                                )
                                /*

                                there seem to be some differences with the unix timestamps (server and client)
                                the timestamp on android seems more correct than the one stored on the server
                                TODO: FIX THIS. FOR NOW WE'LL JUST START THE ACTIVITY

                                if((currentTime - 60) < thistweet.time){
                                    startActivity(i)
                                }else{
                                    // cant edit
                                    Toast.makeText(this@HomeActivity,"Can't edit tweet",Toast.LENGTH_SHORT).show()
                                }*/

                                startActivity(i)

                            }
                        })
                        recyclerview_tweets.adapter = tweetsAdapter
                        Toast.makeText(this@HomeActivity, "Tweets laoded", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            result.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(result: VolleyError) {
                    tweets_refresh_layout.isRefreshing = false
                    // error
                    Toast.makeText(this@HomeActivity, result.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}

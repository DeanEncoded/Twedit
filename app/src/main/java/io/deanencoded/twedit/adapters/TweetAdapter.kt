package io.deanencoded.twedit.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import io.deanencoded.twedit.R
import io.deanencoded.twedit.TweditAPI
import io.deanencoded.twedit.helpers.getSharedPref
import io.deanencoded.twedit.helpers.loadRoundedPhoto
import io.deanencoded.twedit.items.Tweet


class TweetAdapter(internal var context: Context, private var tweets: List<Tweet>) : androidx.recyclerview.widget.RecyclerView.Adapter<TweetAdapter.ViewHolder>() {

    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemlayout_tweet, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        context.loadRoundedPhoto(holder.photo,TweditAPI(userID = tweets[position].tweetBy).data.photo)

        holder.tvName.text = tweets[position].name
        holder.tvUsername.text = "@" + tweets[position].username
        holder.tvText.text = tweets[position].text

        // if the tweet is edited.. show
        if(tweets[position].edited){
            holder.tvEdited.visibility = View.VISIBLE
        }

        // if the tweetBy id matches with our user id... then show the edit button
        if(tweets[position].tweetBy == context.getSharedPref("id").toString().toInt()){
            holder.editTweet.visibility = View.VISIBLE
        }
        // TODO: Some tweets are showing the edit tweet button even though they don't belong to that user. I don't know why.
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.clickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal val tvName: TextView = itemView.findViewById(R.id.tv_name)
        internal val photo: SimpleDraweeView = itemView.findViewById(R.id.profile_photo)
        internal val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        internal val tvText: TextView = itemView.findViewById(R.id.tv_tweet_text)
        internal val tvEdited: TextView = itemView.findViewById(R.id.tv_tweet_edited)
        internal val editTweet: ImageView = itemView.findViewById(R.id.tweet_action_edit)

        init {
            editTweet.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener.onItemClick(v, adapterPosition)
        }
    }
}

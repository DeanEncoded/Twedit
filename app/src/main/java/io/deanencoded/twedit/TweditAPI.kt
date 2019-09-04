package io.deanencoded.twedit

class TweditAPI(serverAddress : String = "http://10.0.2.2:3000", var userID: Int = 0){
    class AuthObj(s: String){
        val login = "$s/auth/login"
        val signup = "$s/auth/signup"
    }
    class TweetObj(s: String){
        val new = "$s/tweet/new"
        val delete = "$s/tweet/delete"
        val edit = "$s/tweet/edit"
    }
    class TweetsObj(s: String){
        val all = "$s/tweets/all"
    }
    class DataObj(s: String,id: Int){
        val photo = "$s/data/$id/profilephoto"
    }

    val auth = AuthObj(serverAddress)
    val tweet = TweetObj(serverAddress)
    val tweets = TweetsObj(serverAddress)
    val data = DataObj(serverAddress,userID)
}



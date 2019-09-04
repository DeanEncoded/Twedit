package io.deanencoded.twedit.helpers

import android.content.Context
import android.net.Uri
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView

fun Context.loadRoundedPhoto(photo: SimpleDraweeView,url: String){
    val roundingParams = RoundingParams.fromCornersRadius(15f)
    roundingParams.roundAsCircle = true
    //roundingParams.setCornersRadii(20f, 20f, 0f, 0f)
    roundingParams.borderWidth = 0f
    photo.hierarchy.roundingParams = roundingParams
    // load the photo from api endpoint
    photo.setImageURI(Uri.parse(url))

}
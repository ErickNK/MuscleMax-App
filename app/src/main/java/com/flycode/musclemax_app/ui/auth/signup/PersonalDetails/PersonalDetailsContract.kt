package com.flycode.musclemax_app.ui.auth.signup.PersonalDetails

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.flycode.musclemax_app.ui.base.BaseContract

interface PersonalDetailsContract {
    interface PersonalDetailsFragment : BaseContract.View{
        fun hideProgressBar()
        fun setPhotoProgress(progress: Int)
        fun setImageBitmap(imageBitmap: Bitmap)
        fun showProgressBar()
        fun getContentResolver(): ContentResolver
        fun getContext(): Context?
    }
    interface PersonalDetailsPresenter<V : PersonalDetailsFragment> : BaseContract.Presenter<V>{
        fun setupProfilePic()
        fun clearImageBitmap()
        fun onCaptureImageResult(data: Intent)
        fun onPickerImageResult(data: Intent)
    }
}
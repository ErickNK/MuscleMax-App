package com.flycode.musclemax_app.ui.auth.signup.PersonalDetails


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.materialdialogs.MaterialDialog

import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.PersonalDetailsBinding
import com.flycode.musclemax_app.ui.auth.signup.SignUpFragment
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.musclemax_app.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal_details.*

import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PersonalDetailsFragment 
    : BaseFragment<PersonalDetailsFragment, PersonalDetailsPresenter, PersonalDetailsViewModel>(),
        PersonalDetailsContract.PersonalDetailsFragment{

    @Inject override lateinit var viewModel: PersonalDetailsViewModel
    @Inject lateinit var superViewModel: SignUpViewModel
    @Inject lateinit var signUpFragment: SignUpFragment
    lateinit var personalDetailsBinding : PersonalDetailsBinding
    var imagePermission = false

    companion object {
        private val SELECT_PHOTO = 1
        private val CAPTURE_PHOTO = 2
        private val PERMISSION_REQUEST_CODE = 3
        fun newInstance(): PersonalDetailsFragment {
            val fragment = PersonalDetailsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        personalDetailsBinding  = DataBindingUtil.inflate(inflater,
                R.layout.fragment_personal_details, container, false)
        personalDetailsBinding.superViewModel = superViewModel
        return personalDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkImagePermissions()
        setupProfilePic()
        profile_pic_progress_bar.progress = 0
        profile_pic_progress_bar.max = 100

        btn_next.setOnClickListener{
            if(checkPasswordRetype()) //TODO: validate data.
                NavHostFragment.findNavController(this).navigate(R.id.LocationDetailsFragment)
        }
    }

    private fun checkPasswordRetype(): Boolean{
        return if ( (!superViewModel.uiState.facebookLoginSuccess && !superViewModel.uiState.googleLoginSuccess)
            && tv_password_retype.text.toString() != tv_password.text.toString()){
            showError("The passwords do not match")
            false
        }else true
    }


    /**
     * Show custom Chooser dialog allowing to choose method of adding a photo
     * or removing the current one.
     *
     */
    private fun setupProfilePic() {
        profile_pic.tag = presenter.imageViewTarget
        presenter.setupProfilePic()
        profile_pic.setOnClickListener{
            context?.let {
                MaterialDialog.Builder(it)
                        .title("Add a progress photo")
                        .items(R.array.uploadImage)
                        .itemsIds(R.array.uploadImageItemIds)
                        .itemsCallback { _, _, position, _ ->
                            when (position) {
                                0 -> {
                                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                                    photoPickerIntent.type = "image/*"
                                    startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                                }
                                1 -> {
                                    val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(photoCaptureIntent, CAPTURE_PHOTO)
                                }
                                2 -> {
                                    profile_pic.setImageResource(R.drawable.image_placeholder)
                                    //Don't save image. The placeholder will be set by default
                                    presenter.clearImageBitmap()
                                }
                            }
                        }
                        .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == SELECT_PHOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onPickerImageResult(data)
                }
            } else if (requestCode == CAPTURE_PHOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onCaptureImageResult(data)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                imagePermission = true
            }
        }
    }

    /**
     * Show the progress bar on the progress photo
     * to indicate the image is being loaded.
     *
     */
    override fun showProgressBar() {
        profile_pic_progress_bar.visibility = View.VISIBLE
    }

    /**
     * Hide the progress bar on the progress photo
     * to indicate the image is done being loaded.
     *
     */
    override fun hideProgressBar() {
        profile_pic_progress_bar.visibility = View.GONE
    }

    override fun setPhotoProgress(progress: Int) {
        profile_pic_progress_bar.progress = progress
    }

    override fun setImageBitmap(imageBitmap: Bitmap) {
        profile_pic.maxWidth = 200
        profile_pic.setImageBitmap(imageBitmap)
    }

    override fun getContentResolver(): ContentResolver {
        return context?.contentResolver!!
    }

    /**
     * Check if the application has been granted access to the camera.
     * If not hide the progress image card view and try requesting for it.
     *
     */
    private fun checkImagePermissions() {
        if (context?.let {
                    ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA)
                } != PackageManager.PERMISSION_GRANTED) {

            imagePermission = false

            activity?.let {
                ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSION_REQUEST_CODE
                )
            }

        } else {
            imagePermission = true
        }
    }


}// Required empty public constructor

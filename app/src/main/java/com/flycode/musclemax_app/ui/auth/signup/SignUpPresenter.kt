package com.flycode.musclemax_app.ui.auth.signup

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.flycode.musclemax_app.data.models.Picture
import com.flycode.musclemax_app.data.models.Response
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.data.network.TempService
import com.flycode.musclemax_app.data.network.UserService
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.flycode.musclemax_app.util.FileUtils
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream


class SignUpPresenter(
        val userService: UserService,
        val tempService: TempService
) : BasePresenter<SignUpFragment,SignUpPresenter,SignUpViewModel>(),
        SignUpContract.SignUpPresenter<SignUpFragment>{

    override var imageBitmap: Bitmap? = null
    override var doImageSave: Boolean = false
    private var imagePath: String? = null

    override fun signUp() {
        view?.let { view ->
            view.showLoading()
            if (doImageSave)
                compositeDisposable.add(
                    saveImageLocally() //SAVE PROFILE PIC LOCALLY
                        .flatMap {
                            //SAVE PROFILE PIC REMOTELY TEMPORALLY
                            prepareImageUpload()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                        }
                        .flatMap {
                            it.data.name = "profile_pic"
                            it.data.description = "Profile picture of user."
                            viewModel.user.pictures?.add(it.data)

                            //SAVE THE USER TOGETHER WITH PICTURES REMOTELY
                            userService.addWithPictures(model = "user",user = viewModel.user)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                        }
                        .flatMap {
                            saveUserLocally(it.data) //SAVE THE USER TOGETHER WITH PICTURES LOCALLY
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({
                            view.hideLoading()
                            if (it == true) view.showMessage("Successfully Registered")
                            else view.showError("Sorry, something went wrong. Please try again.")
                        },{
                            view.hideLoading()
                            view.showError(it.message!!)
                        })
                )
            else
                compositeDisposable.add(
                    userService.add("user",viewModel.user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap {
                                saveUserLocally(it.data) //SAVE THE USER TOGETHER WITH PICTURES LOCALLY
                            }
                            .subscribe ({
                                view.hideLoading()
                                if (it == true) view.showMessage("Successfully Registered")
                                else view.showError("Sorry, something went wrong. Please try again.")
                            },{
                                view.hideLoading()
                                view.showError(it.message!!)
                            })
                )
        }
    }

    fun onClearImageBitmap(){
        imageBitmap = null
    }

    private fun prepareImageUpload(): Observable<Response<Picture>>{
        val file = File(imagePath)
        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), FileUtils.readFileToBytes(imagePath!!))
        val image = MultipartBody.Part.createFormData("tempImage", file.name, reqFile)

        return tempService.tempSaveImage(image)
    }

    private fun saveImageLocally() : Observable<String>{
        return Observable.create({ emitter ->
                //SAVE NEW IMAGE
                imageBitmap?.let {
                    FileUtils.saveImage(view?.context!!,it, "/user_photos","profile_pic.jpg")
                }.apply {
                    if (this != null){
                        imagePath = this
                        emitter.onNext(this)
                        emitter.onComplete()
                    }
                    else emitter.onError(Throwable("Saving Image failed. Please try again."))
                }
        })
    }

    private fun saveUserLocally(user: User) : Observable<Boolean>{
        user.pictures?.find {
            it.name == "profile_pic"
        }.let {
            it?.local_location = imagePath!!
        }
        return Observable.just(user.save())
    }

    override fun onCaptureImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        imageBitmap = data.extras.get("data") as Bitmap
                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                    },{ throwable ->
                        view.showError(throwable.message!!)
                    },{
                        view.setImageBitmap(imageBitmap!!)
                        doImageSave = true
                        view.hideProgressBar()
                    })
            )
        }
    }

    override fun onPickerImageResult(data: Intent) {
        view?.let { view ->
            view.showProgressBar()
            compositeDisposable.add(
                    Observable.create(ObservableOnSubscribe<String> { emitter ->
                        val imageUri = data.data
                        view.setPhotoProgress(30)

                        val imageStream: InputStream = view.getContentResolver().openInputStream(imageUri)!!

                        view.setPhotoProgress(50)

                        imageBitmap = BitmapFactory.decodeStream(imageStream)

                        view.setPhotoProgress(100)
                        emitter.onComplete()
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({

                            },{
                                throwable -> view.showError(throwable.message!!)
                            },{
                                view.setImageBitmap(imageBitmap!!)
                                doImageSave = true
                                view.hideProgressBar()
                            })
            )
        }
    }
}
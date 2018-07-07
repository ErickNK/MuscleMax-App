package com.flycode.musclemax_app.di.module

import android.content.Context
import com.flycode.musclemax_app.data.Config
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.google.android.gms.location.places.Places.PLACE_DETECTION_API
import com.google.android.gms.location.places.Places.GEO_DATA_API



@Module
open class GoogleModule {

    @Provides
    @Singleton
    fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Config.GOOGLE_SERVER_CLIENT_ID)
                .requestProfile()
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        return GoogleSignIn.getClient(context, gso)
    }


}
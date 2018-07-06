package com.flycode.musclemax_app.data.models.apolloMappers

import com.flycode.musclemax_app.data.models.Location
import com.flycode.musclemax_app.data.models.Picture
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.data.models.WeightMeasurement
import com.flycode.musclemax_app.type.Location_Input
import com.flycode.musclemax_app.type.Picture_Input
import com.flycode.musclemax_app.type.User_Input
import com.flycode.musclemax_app.type.Weight_Measurement_Input

object UserMapper{
    fun mapUserToUserInput(user: User): User_Input {
        return User_Input
                .builder()
                .id(user.id.toString())
                .firstname(user.firstname)
                .lastname(user.lastname)
                .gender(user.gender)
                .tel(user.tel)
                .email(user.email)
                .age(user.age.toLong())
                .build()
    }

    fun mapPictureToPictureInput(picture: Picture): Picture_Input {
        return Picture_Input
                .builder()
                .id(picture.id.toString())
                .name(picture.name)
                .description(picture.description)
                .type(picture.type)
                .size(picture.size.toLong())
                .remote_location(picture.remote_location)
                .picturable_id(picture.picturable_id.toString())
                .picturable_type(picture.picturable_type)
                .build()
    }

    fun mapLocationToLocationInput(location:Location): Location_Input {
        return Location_Input
                .builder()
                .id(location.id.toString())
                .address(location.address)
                .latLng(location.latLng)
                .locatable_id(location.locatable_id.toString())
                .locatable_type(location.locatable_type)
                .build()
    }

    fun mapWeightMeasurementToWeightMeasurementInput(weightMeasurement: WeightMeasurement): Weight_Measurement_Input {
        return Weight_Measurement_Input
                .builder()
                .id(weightMeasurement.id.toString())
                .owner_id(weightMeasurement.owner_id.toString())
                .weight(weightMeasurement.weight.toDouble())
                .height(weightMeasurement.height.toDouble())
                .bmi(weightMeasurement.bmi.toDouble())
                .build()
    }
}

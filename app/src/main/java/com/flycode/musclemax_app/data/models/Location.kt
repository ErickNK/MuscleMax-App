package com.flycode.musclemax_app.data.models

import com.flycode.musclemax_app.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = (Database::class), name = "locations" )
class Location {
    @field: [PrimaryKey Column()]
    var id : Int = 0

    @Column()
    var address: String = ""

    @Column()
    var latLng : String = ""

    @Column()
    var locatable_id : Int = 0
    @Column()
    var locatable_type : String = ""
}

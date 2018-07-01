package com.flycode.musclemax_app.data.db

@com.raizlabs.android.dbflow.annotation.Database(
        name = Database.NAME,
        version = Database.VERSION,
        foreignKeyConstraintsEnforced = true
)
object Database {
    const val NAME = "MucleMax"
    const val VERSION = 2
}

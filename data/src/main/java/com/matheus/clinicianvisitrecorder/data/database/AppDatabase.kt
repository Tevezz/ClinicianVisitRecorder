package com.matheus.clinicianvisitrecorder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matheus.clinicianvisitrecorder.data.model.VisitDao
import com.matheus.clinicianvisitrecorder.data.model.VisitEntity

@Database(entities = [VisitEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun visitDao(): VisitDao
}
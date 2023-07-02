package com.dicoding.courseschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//TODO 3 : Define room database class
@Database(entities = [Course::class], version = 2, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {


        @Volatile
        private var instance: CourseDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        fun getInstance(context: Context): CourseDatabase {
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(context, CourseDatabase::class.java, "courses.db").addMigrations(
                    MIGRATION_1_2)
                    .build().also { instance = it }
            }
        }

    }
}

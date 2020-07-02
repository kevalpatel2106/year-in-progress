package com.kevalpatel2106.yip.repo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import java.util.concurrent.Executors

@Database(entities = [DeadlineDto::class], version = 2, exportSchema = true)
@TypeConverters(DbTypeConverter::class)
abstract class YipDatabase : RoomDatabase() {

    internal abstract fun getDeviceDao(): DeadlineDao

    companion object {
        private const val DB_NAME = "yip.db"
        private lateinit var instance: YipDatabase

        internal fun create(application: Context, inMemory: Boolean = false): YipDatabase {
            instance = if (inMemory) {
                Room.inMemoryDatabaseBuilder(application, YipDatabase::class.java)
            } else {
                Room.databaseBuilder(application, YipDatabase::class.java, DB_NAME)
            }.addCallback(object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // Preload the data
                    Executors.newSingleThreadScheduledExecutor().execute {
                        PrebuiltDeadlineBuilder.getPrebuiltDeadline(application)
                            .forEach { instance.getDeviceDao().insert(it) }
                    }
                }
            }).addMigrations(Migration1To2).build()
            return instance
        }

    }
}

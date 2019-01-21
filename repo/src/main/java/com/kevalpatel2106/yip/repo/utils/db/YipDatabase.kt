package com.kevalpatel2106.yip.repo.utils.db

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kevalpatel2106.yip.entity.PrebuiltProgress
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import java.util.*
import java.util.concurrent.Executors

@Database(entities = [ProgressDto::class], version = 1, exportSchema = true)
@TypeConverters(DbTypeConverter::class)
abstract class YipDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "yip.db"
        private lateinit var instance: YipDatabase

        internal fun create(application: Application, inMemory: Boolean = false): YipDatabase {
            instance = if (inMemory) {
                Room.inMemoryDatabaseBuilder(application, YipDatabase::class.java)
            } else {
                Room.databaseBuilder(application, YipDatabase::class.java, DB_NAME)
            }.addCallback(object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // Preload the data
                    Executors.newSingleThreadScheduledExecutor().execute {
                        getPrebuiltProgresses(application).forEach { instance.getDeviceDao().insert(it) }
                    }
                }
            }).build()

            return instance
        }

    }

    internal abstract fun getDeviceDao(): ProgressDao
}


internal fun getPrebuiltProgresses(application: Application): ArrayList<ProgressDto> {
    val prebuiltProgress = ArrayList<ProgressDto>()
    PrebuiltProgress.values().filter {
        it != PrebuiltProgress.CUSTOM
    }.forEach {
        prebuiltProgress.add(
            ProgressDto(
                color = it.color,
                start = Date(System.currentTimeMillis()),
                end = Date(System.currentTimeMillis() + 1000),
                title = it.getName(application),
                order = it.ordinal,
                prebuiltProgress = it
            )
        )
    }
    return prebuiltProgress
}

private fun PrebuiltProgress.getName(context: Context) = when (this) {
    PrebuiltProgress.YEAR_PROGRESS -> context.getString(R.string.this_year)
    PrebuiltProgress.DAY_PROGRESS -> context.getString(R.string.today)
    PrebuiltProgress.WEEK_PROGRESS -> context.getString(R.string.this_week)
    PrebuiltProgress.MONTH_PROGRESS -> context.getString(R.string.this_month)
    PrebuiltProgress.QUARTER_PROGRESS -> context.getString(R.string.this_quarter)
    PrebuiltProgress.CUSTOM -> "-"
}
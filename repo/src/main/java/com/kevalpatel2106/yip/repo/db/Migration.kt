package com.kevalpatel2106.yip.repo.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val TEMP_TABLE_NAME = "TEMP"

internal object Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${DeadlineTableInfo.TABLE_NAME} RENAME TO $TEMP_TABLE_NAME;")
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ${DeadlineTableInfo.TABLE_NAME} " +
                    "(${DeadlineTableInfo.ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "${DeadlineTableInfo.TYPE} INTEGER NOT NULL, " +
                    "${DeadlineTableInfo.TITLE} TEXT NOT NULL, " +
                    "${DeadlineTableInfo.START_TIME} INTEGER NOT NULL, " +
                    "${DeadlineTableInfo.END_TIME} INTEGER NOT NULL, " +
                    "${DeadlineTableInfo.COLOR} INTEGER NOT NULL, " +
                    "${DeadlineTableInfo.NOTIFICATIONS_PERCENTS} TEXT NOT NULL DEFAULT '');"
        )
        database.execSQL(
            "INSERT INTO ${DeadlineTableInfo.TABLE_NAME} (${DeadlineTableInfo.ID}, " +
                    "${DeadlineTableInfo.TYPE}, ${DeadlineTableInfo.TITLE}, " +
                    "${DeadlineTableInfo.START_TIME}, ${DeadlineTableInfo.END_TIME}, " +
                    "${DeadlineTableInfo.COLOR}) SELECT ${DeadlineTableInfo.ID}, " +
                    "${DeadlineTableInfo.TYPE}, ${DeadlineTableInfo.TITLE}, " +
                    "${DeadlineTableInfo.START_TIME}, ${DeadlineTableInfo.END_TIME}, " +
                    "${DeadlineTableInfo.COLOR} FROM $TEMP_TABLE_NAME;"
        )
        database.execSQL("DROP TABLE $TEMP_TABLE_NAME;")
    }
}

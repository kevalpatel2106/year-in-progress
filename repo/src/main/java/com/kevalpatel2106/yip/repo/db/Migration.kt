package com.kevalpatel2106.yip.repo.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val TEMP_TABLE_NAME = "TEMP"

internal object Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${ProgressTableInfo.TABLE_NAME} RENAME TO $TEMP_TABLE_NAME;")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${ProgressTableInfo.TABLE_NAME} (${ProgressTableInfo.ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ${ProgressTableInfo.TYPE} INTEGER NOT NULL, ${ProgressTableInfo.TITLE} TEXT NOT NULL, ${ProgressTableInfo.START_TIME} INTEGER NOT NULL, ${ProgressTableInfo.END_TIME} INTEGER NOT NULL, ${ProgressTableInfo.COLOR} INTEGER NOT NULL, ${ProgressTableInfo.NOTIFICATIONS_MILLS} TEXT NOT NULL DEFAULT '');")
        database.execSQL("INSERT INTO ${ProgressTableInfo.TABLE_NAME} (${ProgressTableInfo.ID}, ${ProgressTableInfo.TYPE}, ${ProgressTableInfo.TITLE}, ${ProgressTableInfo.START_TIME}, ${ProgressTableInfo.END_TIME}, ${ProgressTableInfo.COLOR}) SELECT ${ProgressTableInfo.ID}, ${ProgressTableInfo.TYPE}, ${ProgressTableInfo.TITLE}, ${ProgressTableInfo.START_TIME}, ${ProgressTableInfo.END_TIME}, ${ProgressTableInfo.COLOR} FROM $TEMP_TABLE_NAME;")
        database.execSQL("DROP TABLE $TEMP_TABLE_NAME;")
    }
}
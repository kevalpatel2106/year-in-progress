package com.kevalpatel2106.yip.repo.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DbMigrationTest {
    private val TEST_DB_NAME = "yip_test.db"

    @Rule
    @JvmField
    val migrationHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        YipDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun testMigration_1To2() {
        // Create the database with version 2
        migrationHelper.createDatabase(TEST_DB_NAME, 1)

        //Run the migration and validate
        try {
            migrationHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, Migration1To2)
        } catch (e: Exception) {
            //Migration failed.
            Assert.fail(e.message)
            return
        }
    }

    @Test
    fun testMigration_2To3() {
        // Create the database with version 2
        migrationHelper.createDatabase(TEST_DB_NAME, 2)

        //Run the migration and validate
        try {
            migrationHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, Migration2To3)
        } catch (e: Exception) {
            //Migration failed.
            Assert.fail(e.message)
            return
        }
    }
}

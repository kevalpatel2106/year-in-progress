package com.kevalpatel2106.yip.utils.db

import android.app.Application
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import com.kevalpatel2106.yip.repo.utils.db.Migration1To2
import com.kevalpatel2106.yip.repo.utils.db.YipDatabase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class Migration1To2Test {
    private val TEST_DB_NAME = "yip_test.db"
    private val testProgress = ProgressDto(
            id = 438675,
            color = ProgressColor.COLOR_BLUE,
            end = Date(System.currentTimeMillis()),
            start = Date(System.currentTimeMillis()),
            notifications = "34,345",
            progressType = ProgressType.DAY_PROGRESS,
            title = "test"
    )

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
        val db = migrationHelper.createDatabase(TEST_DB_NAME, 1)

        //Inject data in version 1.
        db.execSQL("INSERT OR REPLACE INTO `progresses`(`id`,`type`,`title`,`start_mills`,`end_mills`,`color`,`order`,`is_enabled`)" +
                " VALUES (${testProgress.id}, ${testProgress.color}, ${testProgress.end}, ${testProgress.start}, ${testProgress.progressType}, ${testProgress.title}, 0, false);")

        //Run the migration
        try {
            migrationHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, Migration1To2)
        } catch (e: Exception) {
            //Migration failed.
            Assert.fail(e.message)
            return
        }

        // Get the data back from version 2.
        val testObserver = YipDatabase
                .create(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application)
                .getDeviceDao()
                .observe(testProgress.id)
                .test()

        testObserver.awaitTerminalEvent()

        //Assert the data
        testObserver.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { it.id == testProgress.id }
                .assertValueAt(0) { it.title == testProgress.title }
                .assertValueAt(0) { it.color == testProgress.color }
                .assertValueAt(0) { it.end == testProgress.end }
                .assertValueAt(0) { it.start == testProgress.start }
                .assertValueAt(0) { it.progressType == testProgress.progressType }
                .assertValueAt(0) { it.notifications.isEmpty() }
    }
}
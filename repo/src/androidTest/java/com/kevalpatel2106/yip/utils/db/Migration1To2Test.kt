package com.kevalpatel2106.yip.utils.db

import android.app.Application
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.db.Migration1To2
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class Migration1To2Test {
    private val TEST_DB_NAME = "yip_test.db"
    private val testDeadline = DeadlineDto(
        id = 438675,
        color = DeadlineColor.COLOR_BLUE,
        end = Date(System.currentTimeMillis()),
        start = Date(System.currentTimeMillis()),
        notifications = listOf(34F, 345F),
        type = DeadlineType.DAY_DEADLINE,
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
        db.execSQL(
            "INSERT OR REPLACE INTO `progresses`(`id`,`type`,`title`,`start_mills`,`end_mills`,`color`,`sortOrder`,`is_enabled`)" +
                    " VALUES (${testDeadline.id}, ${testDeadline.color}, ${testDeadline.end}, ${testDeadline.start}, ${testDeadline.type}, ${testDeadline.title}, 0, false);"
        )

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
            .observe(testDeadline.id)
            .test()

        testObserver.awaitTerminalEvent()

        //Assert the data
        testObserver.assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
            .assertValueAt(0) { it.id == testDeadline.id }
            .assertValueAt(0) { it.title == testDeadline.title }
            .assertValueAt(0) { it.color == testDeadline.color }
            .assertValueAt(0) { it.end == testDeadline.end }
            .assertValueAt(0) { it.start == testDeadline.start }
            .assertValueAt(0) { it.type == testDeadline.type }
            .assertValueAt(0) { it.notifications.isEmpty() }
    }
}

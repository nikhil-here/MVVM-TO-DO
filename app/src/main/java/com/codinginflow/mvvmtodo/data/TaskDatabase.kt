package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {


    abstract fun taskDao(): TaskDao

    /**
     * Populating Database Initially
     */

    class callBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Do the laundary"))
                dao.insert(Task("Buy Groceries",important = true))
                dao.insert(Task("Prepare Food",completed = true))
                dao.insert(Task("Call mom"))
                dao.insert(Task("call Elon Musk"))
                dao.insert(Task("Sleep for 3 weeks",completed = true))
            }
        }
    }
}
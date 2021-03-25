package com.codinginflow.mvvmtodo.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder {
    BY_NAME, BY_DATE
}

private const val TAG = "PreferenceManager"
data class  FilterPreferences(val sortOrder : SortOrder, val hideCompleted : Boolean)

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context : Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException)
            {
                emit(emptyPreferences())
                Log.e(TAG, "Error reading preferences : "+exception.message, )
            }else{
                throw exception;
            }
        }
        .map{ preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PrefernceKeys.SORT_ORDER]  ?: SortOrder.BY_DATE.name
            )

            val hideCompleted = preferences[PrefernceKeys.HIDE_COMPLETED] ?: false

            FilterPreferences(sortOrder,hideCompleted)
        }


    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit { preferences ->
            preferences[PrefernceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        dataStore.edit { preferences ->
            preferences[PrefernceKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PrefernceKeys{
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }

}
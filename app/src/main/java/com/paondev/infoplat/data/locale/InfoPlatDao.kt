package com.paondev.infoplat.data.locale;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.paondev.infoplat.model.History;
import kotlinx.coroutines.flow.Flow

import java.util.List;

@Dao
interface InfoPlatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history")
    fun getAllHistory(): Flow<List<History>>

    @Query("""
    SELECT * FROM history
    WHERE code = :searchName
    ORDER BY code ASC
    LIMIT :limit OFFSET :offset
""")
    suspend fun searchHistoryByName(
        searchName: String,
        offset: Int,
        limit: Int
    ): List<History>

}

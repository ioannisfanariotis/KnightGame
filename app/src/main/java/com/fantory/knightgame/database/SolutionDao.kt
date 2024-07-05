package com.fantory.knightgame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fantory.knightgame.models.Solution

@Dao
abstract class SolutionDao {
    @Query("DELETE FROM solution")
    abstract suspend fun deleteAll()

    @Insert
    abstract suspend fun insertSolution(solution: Solution)

    @Query("SELECT * FROM solution")
    abstract suspend fun getSolution(): List<Solution>
}
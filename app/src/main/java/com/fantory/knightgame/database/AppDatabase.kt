package com.fantory.knightgame.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fantory.knightgame.models.Solution

@Database(entities = [
    Solution::class,
], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun solutionDao(): SolutionDao
}
package com.fantory.knightgame.repo

import com.fantory.knightgame.database.SolutionDao
import com.fantory.knightgame.models.Solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class AppRepo @Inject constructor(@Named("SolutionDao") private val dao: SolutionDao) {

    fun insertSolution(solution: Solution): Flow<Boolean> =
        flow {
            dao.deleteAll()
            dao.insertSolution(solution)
            emit(true)
        }.flowOn(Dispatchers.IO)

    fun showSolution(): Flow<Solution> =
        flow {
            val solution = dao.getSolution()
            if (solution.isNotEmpty())
                emit(solution[0])
            else
                emit(Solution(0, ""))
        }.flowOn(Dispatchers.IO)
}
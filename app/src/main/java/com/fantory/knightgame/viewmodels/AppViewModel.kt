package com.fantory.knightgame.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantory.knightgame.models.Solution
import com.fantory.knightgame.repo.AppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor (private val repo: AppRepo, state: SavedStateHandle): ViewModel() {
    val size = state.get<Int>("size") ?: 10
    val moves = state.get<Int>("moves") ?: 3

    var loader = MutableLiveData<Boolean>()
    var exceptionErrorDialogLd = MutableLiveData<String>()

    private var validInsertMld = MutableLiveData<Boolean>()
    var validInsertLd : LiveData<Boolean> = validInsertMld

    private var solutionMld = MutableLiveData<Solution>()
    var solutionLd: LiveData<Solution> = solutionMld

    private var job: Job? = null

    fun insertSolution(solution: Solution) {
        loader.postValue(true)
        job = viewModelScope.launch {
            repo.insertSolution(solution)
                .catch {
                        error ->
                            loader.postValue(false)
                            exceptionErrorDialogLd.postValue(error.message)
                }.collect {
                        valid ->
                            loader.postValue(false)
                            validInsertMld.postValue(valid)
                }
        }
    }
    fun showSolution() {
        loader.postValue(true)
        job = viewModelScope.launch {
            repo.showSolution()
                .catch {
                        error ->
                            loader.postValue(false)
                            exceptionErrorDialogLd.postValue(error.message)
                }.collect {
                        settings ->
                            loader.postValue(false)
                            solutionMld.postValue(settings)
                }
        }
    }


    fun dispose() {
        job?.cancel()

        validInsertMld = MutableLiveData<Boolean>()
        validInsertLd = validInsertMld

        solutionMld = MutableLiveData<Solution>()
        solutionLd = solutionMld
    }
}
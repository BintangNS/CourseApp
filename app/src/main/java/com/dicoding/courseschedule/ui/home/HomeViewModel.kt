package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository) : ViewModel() {

    private val _queryType = MutableLiveData(QueryType.CURRENT_DAY)
    val queryType: LiveData<QueryType> = _queryType

    // Add the course LiveData
    val course: LiveData<Course?> = Transformations.switchMap(_queryType) { queryType ->
        when(queryType) {
            QueryType.CURRENT_DAY -> repository.getNearestSchedule(QueryType.CURRENT_DAY)
            QueryType.NEXT_DAY -> repository.getNearestSchedule(QueryType.NEXT_DAY)
            QueryType.PAST_DAY -> repository.getNearestSchedule(QueryType.PAST_DAY)
        }
    }

    fun setQueryType(queryType: QueryType) {
        if (_queryType.value != queryType) {
            _queryType.value = queryType
        }
    }
    fun refreshCourse() {
        setQueryType(_queryType.value!!)
    }
}

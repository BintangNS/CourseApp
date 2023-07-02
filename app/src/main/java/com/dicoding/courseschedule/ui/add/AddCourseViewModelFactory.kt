package com.dicoding.courseschedule.ui.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.data.CourseDao
import com.dicoding.courseschedule.data.DataRepository

class ViewModelFactory(private val courseDao: CourseDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCourseViewModel(DataRepository(courseDao)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
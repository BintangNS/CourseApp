package com.dicoding.courseschedule.ui.add

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.CourseDatabase
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var edCourseName: TextInputEditText
    private lateinit var spinnerDay: Spinner
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var edLecturer: TextInputEditText
    private lateinit var edNote: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edCourseName = findViewById(R.id.ed_course_name)
        spinnerDay = findViewById(R.id.spinner_day)
        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)
        edLecturer = findViewById(R.id.ed_lecturer)
        edNote = findViewById(R.id.ed_note)

        val courseDao = CourseDatabase.getInstance(applicationContext).courseDao()
        val viewModelFactory = ViewModelFactory(courseDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddCourseViewModel::class.java)

        findViewById<ImageButton>(R.id.ib_start_time).setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "startTimePicker")
        }

        findViewById<ImageButton>(R.id.ib_end_time).setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "endTimePicker")
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDay.adapter = adapter
        }

        viewModel.saved.observe(this) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    Toast.makeText(this, "Course successfully added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_insert -> {
                val courseName = edCourseName.text.toString()
                val day = spinnerDay.selectedItemPosition
                val startTime = tvStartTime.text.toString()
                val endTime = tvEndTime.text.toString()
                val lecturer = edLecturer.text.toString()
                val note = edNote.text.toString()
                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val selectedTime = String.format("%02d:%02d", hour, minute)
        when (tag) {
            "startTimePicker" -> tvStartTime.text = selectedTime
            "endTimePicker" -> tvEndTime.text = selectedTime
        }
    }
}
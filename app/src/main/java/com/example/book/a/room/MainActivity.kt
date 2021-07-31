package com.example.book.a.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var roomViewModel: RoomViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomViewModel = ViewModelProviders.of(this)[RoomViewModel::class.java]

        setContentView(R.layout.activity_main)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.list_of_rooms)

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        recyclerView.layoutManager = LinearLayoutManager(this)

        val output = roomViewModel.listOfRooms
        println(output)
    }
}
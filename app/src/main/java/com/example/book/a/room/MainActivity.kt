package com.example.book.a.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var roomViewModel: RoomViewModel

    private lateinit var recyclerView: RecyclerView

    private val adapter = RoomAdapter()

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

        recyclerView.adapter = adapter

        roomViewModel?.getRooms()?.observe(this, Observer {
          adapter.setData(it)
        })
    }
}
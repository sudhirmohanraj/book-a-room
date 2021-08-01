package com.example.book.a.room

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.book.a.room.model.Room
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RoomViewModel : ViewModel() {
    private val roomLiveData: MutableLiveData<List<Room>> = MutableLiveData(emptyList())
    var allRooms : List<Room> = emptyList()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        coroutineScope.launch {
            loadListOfRooms()
        }
    }


    /**
     * Returns a LiveData objecting containing a list of rooms
     */
    fun getRooms(): LiveData<List<Room>> = roomLiveData

    private fun loadListOfRooms() = coroutineScope.launch {
        val rooms = loadRooms()
        rooms.forEach{
            val name = it.name
            val spots = it.spots
            val thumbnail = it.thumbnail
            var room = Room(name,spots,thumbnail)
            retrieveBitmapImageFromUrl(room, it)
        }
        allRooms = rooms
        roomLiveData.postValue(rooms)
    }

    private fun retrieveBitmapImageFromUrl(rm: Room, it: Room) {
        try {
            val connection = URL(rm.thumbnail).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            rm.imageBitmap = BitmapFactory.decodeStream(input)
            it.imageBitmap = rm.imageBitmap
        } catch (e: IOException) {
            Timber.e("Encountered an issue with retrieving Image RoomViewModel#retrieveBitmapImageFromUrl. ")
        }
    }

    private suspend fun loadRooms(): List<Room> {
        return mutableListOf<Room>().also { rooms ->
            withContext(coroutineScope.coroutineContext) {
                val body = URL("https://wetransfer.github.io/rooms.json")
                    .openConnection()
                    .getInputStream()
                    .bufferedReader()
                    .readText()
                val roomsArr = JSONObject(body).getJSONArray("rooms")
                for (i in 0 until roomsArr.length()) {
                    roomsArr.getJSONObject(i).apply {
                        rooms.add(
                            Room(
                                getString("name"),
                                getInt("spots"),
                                getString("thumbnail")
                            )
                        )
                    }
                }
            }
        }
    }
}
package com.example.book.a.room

import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.book.a.room.model.Room
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RoomViewModel : ViewModel() {
    val listOfRooms = MutableLiveData<List<Room>>(emptyList())
    var allRooms : List<Room> = emptyList()
    val selectedRoomToBook = MutableLiveData<Room>()
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    init {
        coroutineScope.launch {
            loadListOfRooms()
        }
    }

    private fun loadListOfRooms() = coroutineScope.launch {
        val rooms = loadRooms()
        rooms.forEach{
            val name = it.name
            val spots = it.spots
            val thumbnail = it.thumbnail
        }
        allRooms = rooms
    }

//    private suspend fun retrieveBitmapImageFromUrl(pl: Place, it: Place) {
//        val imageUrl = loadImgForPlace(pl)
//        try {
//            val connection = imageUrl.openConnection() as HttpURLConnection
//            connection.doInput = true
//            connection.connect()
//            val input = connection.inputStream
//            pl.imageBitmap = BitmapFactory.decodeStream(input)
//            it.imageBitmap = pl.imageBitmap
//        } catch (e: IOException) {
//            Timber.e("Encountered an issue with retrieving Image NeighborhoodViewModel#retrieveBitmapImageFromUrl. ")
//        }
//    }

    private suspend fun loadRooms(): List<Room> {
        return mutableListOf<Room>().also { rooms ->
            withContext(coroutineScope.coroutineContext) {
                val body = URL("https://wetransfer.github.io/rooms.json")
                    .openConnection()
                    .getInputStream()
                    .bufferedReader()
                    .readText()
                val placesArr = JSONObject(body).getJSONArray("rooms")
                for (i in 0 until placesArr.length()) {
                    placesArr.getJSONObject(i).apply {
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
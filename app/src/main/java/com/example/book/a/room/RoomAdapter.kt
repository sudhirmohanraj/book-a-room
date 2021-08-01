package com.example.book.a.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book.a.room.model.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class RoomAdapter() : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var data: List<Room> = ArrayList()

    class RoomViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById(R.id.room_name) as TextView
        private val spots = itemView.findViewById(R.id.room_spots) as TextView
        private val book = itemView.findViewById(R.id.room_book) as Button
        private val image = itemView.findViewById(R.id.room_image) as ImageView
        private val coroutineScope = CoroutineScope((Dispatchers.Default))

        fun bind(room: Room) {
            name.text = room.name
            spots.text = room.spots.toString()
            image.setImageBitmap(room.imageBitmap)
            //Use OnClick Listeners to Handle Book Button
            book.setOnClickListener(View.OnClickListener {
                coroutineScope.launch {
                    val response = bookRoom()
                    println("response $response")
                }
            })
        }

        private suspend fun bookRoom(): String {
            var response = ""
            withContext(coroutineScope.coroutineContext) {
                val body = URL("https://wetransfer.github.io/bookRoom.json")
                    .openConnection()
                    .getInputStream()
                    .bufferedReader()
                    .readText()
                response = JSONObject(body).getString("success")
            }
            return response
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.room, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun setData(data: List<Room>) {
        this.data = data
        notifyDataSetChanged()
    }
}
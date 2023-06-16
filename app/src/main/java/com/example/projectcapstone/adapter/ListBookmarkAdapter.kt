package com.example.projectcapstone.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.projectcapstone.R
import com.example.projectcapstone.artikel.DetailArtikelActivity
import com.example.projectcapstone.preferences.UserPreference
import com.example.projectcapstone.response.dummyartikeldata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ListBookmarkAdapter(private val listArtikel: ArrayList<dummyartikeldata>, private val pref: UserPreference) : RecyclerView.Adapter<ListBookmarkAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_artikel, parent, false)
        fetchDataFromApi()
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, detail, description, photo) = listArtikel[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)
        holder.titleArticle.text = name

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailArtikelActivity::class.java)
            println(listArtikel)

            intentDetail.putExtra("key_fitur", listArtikel[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: dummyartikeldata)
    }

    override fun getItemCount(): Int = listArtikel.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_item_photo)
        val titleArticle: TextView = itemView.findViewById(R.id.tv_item_name)

    }

    fun fetchDataFromApi() {
        println("Fetching Articles ..")
        val url = URL("https://backend.batikin.site/api/articles/bookmark")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val token = "Bearer " + pref.getUser().token
        connection.setRequestProperty("Authorization", token)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonObject = JSONObject(response.toString())
                    val dataArray: JSONArray = jsonObject.getJSONArray("data")
                    try {

                        val dataList = ArrayList<dummyartikeldata>()
                        for (i in 0 until dataArray.length()) {
                            val dataObject = dataArray.getJSONObject(i)
                            val id = dataObject.getString("id")
                            val title = dataObject.getString("title")
                            val subtitle = dataObject.getString("subtitle")
                            val content = dataObject.getString("content")
                            val image = dataObject.getString("image")

                            val data = dummyartikeldata(id, title, subtitle, content, image)
                            dataList.add(data)
                        }

                        withContext(Dispatchers.Main) {
                            listArtikel.clear()
                            listArtikel.addAll(dataList)
                            notifyDataSetChanged()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

            } finally {
                connection.disconnect()
            }
        }
    }

}
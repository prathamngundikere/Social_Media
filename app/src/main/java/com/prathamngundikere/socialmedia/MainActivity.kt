package com.prathamngundikere.socialmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        // Sample Data
        val itemList = listOf(
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4"),
            MyItem("Title 5", "Subtitle 5"),
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4"),
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4"),
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4"),
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4"),
            MyItem("Title 1", "Subtitle 1"),
            MyItem("Title 2", "Subtitle 2"),
            MyItem("Title 3", "Subtitle 3"),
            MyItem("Title 4", "Subtitle 4")
        )

        adapter = MyAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
package com.example.vocab_pr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.example.vocab_pr.db.dbManager

class SearchViewActivity : AppCompatActivity() {
    private val db_manager = dbManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view)
        db_manager.dbOpen()
        var dictionaryListView = findViewById<ListView>(R.id.dictionary_list_view)
        var searchSpace = findViewById<SearchView>(R.id.search_space)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, db_manager.dbRead("word"))
        dictionaryListView.adapter = adapter
        searchSpace.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (db_manager.dbRead("word").contains(query)){
                    adapter.filter.filter(query)
                }
                else{
                    Toast.makeText(applicationContext, "No Word found...", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.directory_activity_static, R.anim.search_view_zoom_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        db_manager.dbClose()
    }
}
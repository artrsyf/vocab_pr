package com.example.vocab_pr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val directoryButton = findViewById<Button>(R.id.directory_button)
        val cardGameButton = findViewById<Button>(R.id.card_game_button)
        directoryButton.setOnClickListener {
            val intent: Intent = Intent(this, DirectoryActivity::class.java) // check Intent, ::class.java
            startActivity(intent)
        }
        cardGameButton.setOnClickListener {
            val intent: Intent = Intent(this, CardGameActivity::class.java)
            startActivity(intent)
        }
    }
}
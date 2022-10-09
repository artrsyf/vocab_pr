package com.example.vocab_pr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vocab_pr.db.dbManager

class CardGameActivity : AppCompatActivity() {
    private lateinit var card: ImageView
    val db_manager: dbManager = dbManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)
        db_manager.dbOpen()
        val wordList = db_manager.dbRead("word")
        val transcriptionList = db_manager.dbRead("transcription")
        val translateList = db_manager.dbRead("translate")
        var nextButton: Button = findViewById<Button>(R.id.next_button)
        val frame: View = findViewById(R.id.frameLayout)
        var word = findViewById<TextView>(R.id.guess_word_view_text)
        var direction = 1
        word.isEnabled = false
        card = findViewById(R.id.game_card)
        fun getRandomWordIndex(): Int{
            return (0..db_manager.dbRead("word").lastIndex).random()
        }
        var currentNumber = getRandomWordIndex()
        word.text = wordList[currentNumber]
        card.setOnClickListener {
            //card.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_animation)) // Not this rotate
            card.isEnabled = false // animation code is really difficult to read
            frame.animate().apply {
                rotationYBy(direction * 90f)
                duration = 300
            }.withEndAction {
                word.animate().apply {
                    rotationYBy(180f)
                    duration = 0
                }.start()
                if (word.text.toString() in wordList) word.text = translateList[currentNumber]
                else word.text = wordList[currentNumber]
                frame.animate().apply {
                    rotationYBy(direction * 90f)
                    duration = 300
                }.withEndAction {
                    card.isEnabled = true
                }.start()
                direction = -direction
            }.start()

        }
        nextButton.setOnClickListener {
            frame.animate().apply {
                frame.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.right_start_slide))
                duration = 500
            }.withEndAction {
                currentNumber = getRandomWordIndex()
                word.text = wordList[currentNumber]
                frame.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.right_end_slide))
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db_manager.dbClose()
    }
}
package com.example.vocab_pr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.vocab_pr.db.dbManager
import com.google.android.material.bottomsheet.BottomSheetDialog


class DirectoryActivity : AppCompatActivity() {
    private lateinit var NewWord: EditText
    private lateinit var Grid: TableLayout
    private val db_manager = dbManager(this)
//    private lateinit var deleteFab: FloatingActionButton
//    private lateinit var deleteGoButton: Button
//    private lateinit var deleteEditText: EditText
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var fabRotateForward: Animation
    private lateinit var fabRotateBackward: Animation
    //private var isFabOpen = false
    private var currentWordCursor = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        db_manager.dbOpen()
        this.init()
//        deleteFab = findViewById(R.id.delete_fab) // fab section begins there
//        deleteGoButton = findViewById(R.id.go_delete_button)
//        deleteEditText = findViewById(R.id.delete_word); deleteEditText.setText("QWERTY")
//        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
//        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
//        fabRotateForward = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_forward)
//        fabRotateBackward = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_backward)
//        deleteFab.setOnClickListener {
//            animateFab()
//
//            val deleteWord = deleteEditText.text.toString()
//
//        } // fab section ends here
        val menuButton = findViewById<ImageButton>(R.id.menu_button)
        val GoAddButton = findViewById<Button>(R.id.go_add_button)
        var NewRow: TableRow = TableRow(this)
        var newcell_1: TextView = TextView(this);val newcell_2: TextView = TextView(this); val newcell_3: TextView = TextView(this); val newcell_4: TextView = TextView(this);
        NewRow.addView(newcell_1); NewRow.addView(newcell_2); NewRow.addView(newcell_3); NewRow.addView(newcell_4)
        Grid.addView(NewRow)
        fun dbEditing() {
            newcell_1.text = "" // clean previous text; there begins SQL updating code
            newcell_2.text = ""
            newcell_3.text = ""
            newcell_4.text = ""
            val wordList = db_manager.dbRead("word")
            val transcriptionList = db_manager.dbRead("transcription")
            val translateList = db_manager.dbRead("translate")
            val wrList = db_manager.dbRead("wr")
            for (i in 0..wordList.lastIndex) {
                newcell_1.append(wordList[i])
                newcell_1.append("\n")
                newcell_2.append(transcriptionList[i])
                newcell_2.append("\n")
                newcell_3.append(translateList[i])
                newcell_3.append("\n")
                newcell_4.append(wrList[i])
                newcell_4.append("\n")
            } // there ends
        }
        dbEditing()
        menuButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.fragment_bottomsheet,
                findViewById(R.id.bottom_sheet_layout) as LinearLayout?)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
            fun showEditTextDialog(){
                bottomSheetView.findViewById<Button>(R.id.delete_button).setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    val dialogLayout = layoutInflater.inflate(R.layout.delete_edit_text_layout, null)
                    val editText = dialogLayout.findViewById<EditText>(R.id.delete_edit_text)
                    with (builder){
                        setTitle("Delete")
                        setPositiveButton("OK"){dialog, which ->
                            db_manager.dbDelete(editText.text.toString())
                            dbEditing()
                        }
                        setNegativeButton("Cancel"){dialog, which ->
                            // closing builder
                        }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            fun showSearchViewActivity(){
                bottomSheetView.findViewById<Button>(R.id.search_button).setOnClickListener {
                    val intent: Intent = Intent(this, SearchViewActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.search_view_zoom_in, R.anim.directory_activity_static)
                }
            }
            fun showDeleteDataBaseDialog(){
                bottomSheetView.findViewById<Button>(R.id.delete_data_base_button).setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    val dialogLayout = layoutInflater.inflate(R.layout.delete_data_base_text_view_layout, null)
                    with (builder){
                        setTitle("Delete Dictionary")
                        setPositiveButton("OK"){dialog, which ->
                            db_manager.dbDestroy()
                        }
                        setNegativeButton("Cancel"){ dialog, which ->
                            // Closing builder
                        }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
            showEditTextDialog()
            showSearchViewActivity()
            showDeleteDataBaseDialog()
        }
        GoAddButton.setOnClickListener {
            val TakeNew: String = NewWord.text.toString() // Input word
            // unit for translate and transcription
            val newname_4:String = "W/R"
            val newname_3:String = Localise(TakeNew, openDictionary()).replace(" ", "; ").replace("\t", "; ") // converting localisation sentence
            var newname_2:String = Transcript(openTranscriptDictionary());
            if (newname_2 != ""){
                if (newname_2 == "/("){
                    val text = "Unfortunately, this transcription is not in the dictionary" // Error text, word is not in dictionary
                    newname_2 =""
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
            }
            if (TakeNew != ""){
                if (newname_3 == "/)"){ // can add it for transcription; This block checks word existing in dictionary
                    val text = "Unfortunately, this word is not in the dictionary" // Error text, word is not in dictionary
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
                else if (TakeNew in db_manager.dbRead("word")){ // This block checking that word already exists in app's dictionary
                    val text = "The entered word is already included"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
                else{
                        db_manager.dbInsert(TakeNew, newname_2, newname_3, newname_4)
                        dbEditing()
                }
            }
        }
    }
    fun init(){
        Grid = findViewById(R.id.word_grid)
        NewWord = findViewById(R.id.new_word)
        val Row: TableRow = TableRow(this)
        val cell_1: TextView = TextView(this); val cell_2: TextView = TextView(this); val cell_3: TextView = TextView(this); val cell_4: TextView = TextView(this)
        val name_1:String = "New Word"; val name_2:String = "Transcription"; val name_3:String = "Localization"; val name_4:String = "Win Rate"
        cell_1.setText(name_1); cell_2.setText(name_2); cell_3.setText(name_3); cell_4.setText(name_4)
        //var TextType: Typeface = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf") // can change text font               // err
        //cell_1.setTypeface(TextType); cell_2.setTypeface(TextType); cell_3.setTypeface(TextType); cell_4.setTypeface(TextType) // err
        // change_color_step
        Row.addView(cell_1); Row.addView(cell_2); Row.addView(cell_3); Row.addView(cell_4)
        Grid.addView(Row)
    }
    fun openDictionary(): String{ // Open and convert in String assets Dictionary file
        val fileText: String = applicationContext.assets.open("ENRUS.TXT").bufferedReader().use {
            it.readText()
        }
        return fileText
    }
    fun openTranscriptDictionary(): String{ // Open and convert in String assets Transcription dictionary file
        val fileText: String = applicationContext.assets.open("trans.txt").bufferedReader().use{
            it.readText()
        }
        return fileText
    }
    fun Localise(word: String, text: String): String{
        val array: List<String> = text.split("\r\n")
//        Log.d("WORD", array[1][0].toString())
//        Log.d("WORD", array[2][1].toString())
//        Log.d("WORD", array[2][2].toString())
//        Log.d("WORD", array[2][3].toString())
//        Log.d("WORD", array[2][4].toString())
//        Log.d("WORD", array[2][5].toString())
//        Log.d("WORD", array[2][6].toString())
//        Log.d("WORD", array[2].lastIndex.toString())
//        Log.d("WORD", word)
//        Log.d("WORD", ((word + '\r') == array[2]).toString())

        if (array.contains(word)){ // can realise more form output; \r
            currentWordCursor = array.indexOf(word) + 1
            var return_word: String = array[currentWordCursor]
//            Log.d("WORD", return_word)
//            Log.d("WORD", (array.indexOf(word) + 1).toString())
//            if ("\t" in return_word){ // replace tab
//                return_word.replace('\t', ';', true)
//            }
//            if (" " in return_word){ // replace space
//                return_word.replace(' ', ';', true)
//            }
//            else return return_word
            return return_word

        }
        else{
            return "/)" // Error: none localisation
        }
    }
    fun Transcript(text: String): String{
        val array: List<String> = text.split("\r\n")
        var return_word: String = ""
        if (currentWordCursor != 0) {
            return_word = array[currentWordCursor - 1] // cursor stands on end word that is adding now
        }
        if (return_word != ""){
            if (return_word[0] == '[') return return_word
            else return "/(" /* Error: none Transcription */
        }
        else return ""
    }

//    fun animateFab(){ // bug with entering text in newWordTextEdit when deleteEditText has began and bug with entering text in deleteEditText. check it immediately
//        if (isFabOpen){ // opening click
//            deleteFab.startAnimation(fabRotateForward)
//            deleteEditText.startAnimation(fabClose)
//            deleteGoButton.startAnimation(fabClose)
//            deleteEditText.isEnabled = false
//            deleteGoButton.isEnabled = false
//            isFabOpen = false
//        }
//        else{ // ending click
//            deleteFab.startAnimation(fabRotateBackward)
//            deleteEditText.startAnimation(fabOpen)
//            deleteGoButton.startAnimation(fabOpen)
//            deleteEditText.isEnabled = true
//            deleteGoButton.isEnabled = true
//            isFabOpen = true
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        db_manager.dbClose()
    }
}
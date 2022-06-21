package org.tensorflow.lite.examples.detection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.detection.logic.Card2
import org.tensorflow.lite.examples.detection.logic.MovesFinder
import java.io.File.separator
import java.lang.StringBuilder


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        val Back_btn = findViewById<Button>(R.id.Back_btn)
        Back_btn.setOnClickListener(){
            val switchActivityIntent = Intent(this, DetectorActivity::class.java)
            startActivity(switchActivityIntent)

        }
        val Exit_btn = findViewById<Button>(R.id.Exit_btn)
        Exit_btn.setOnClickListener(){
            val switchActivityIntent = Intent(this, FrontPageActivity::class.java)
            startActivity(switchActivityIntent)

        }

        val resultTextView = findViewById<TextView>(R.id.Result)
        val firsttext = findViewById<TextView>(R.id.FirstResul)

        val allCardsCommaSeparatedList = intent.getStringArrayListExtra("EXTRA_CARDS_INPUT")



        val movesFinder = MovesFinder()
        val moves = movesFinder.findMovesForColumns(allCardsCommaSeparatedList)
        var allMovesText = "";
        println("mylog print all moves: " + moves.size)
        for (i in moves.indices) {
            val move = moves[i];
            println("mylog move: " + move.columnOnTop + " can be on top of " + move.columnAtBottom)


            //allMovesText +="* You can move column "+ moves[i].toString() +". "+ "\n\n"
            allMovesText+= "* You can move column (" +move.columnOnTop+" " + "to" +" "+ move.columnAtBottom +"). "+ "\n\n"
            println("mylog printline"+ allMovesText)

        }
        firsttext.setText(allMovesText)
        println("mylog print all moves-----done")

        val toast2 = Toast.makeText(this, "at lasttt: " + moves.size, Toast.LENGTH_SHORT)
        toast2.show()

    }


}

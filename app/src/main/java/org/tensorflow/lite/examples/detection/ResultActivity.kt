package org.tensorflow.lite.examples.detection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.detection.logic.MovesFinder


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

        val allCards = intent.getStringArrayListExtra("EXTRA_CARDS_INPUT")
        val movesFinder = MovesFinder()
        val moves = movesFinder.findMoves(allCards)
        var allMovesText = "";
        println("mylog print all moves: " + moves.size)
        for (i in moves.indices) {
            println("mylog move: " + moves[i].toString())
            allMovesText += moves[i].toString() + "; "
        }
        resultTextView.setText(allMovesText)
        println("mylog print all moves-----done")

        val toast2 = Toast.makeText(this, "at lasttt: " + moves.size, Toast.LENGTH_SHORT)
        toast2.show()

    }


}

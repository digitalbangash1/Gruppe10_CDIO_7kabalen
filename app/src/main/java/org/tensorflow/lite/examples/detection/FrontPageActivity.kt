package org.tensorflow.lite.examples.detection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.detection.logic.MovesFinder


class FrontPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frontpage)
        val nextButton = findViewById<Button>(R.id.next_btn)
        nextButton.setOnClickListener(){
            val switchActivityIntent = Intent(this, DetectorActivity::class.java)
            startActivity(switchActivityIntent)
        }


// Test
//        var allCards = arrayListOf<String>()
//
//        allCards.clear()
//        allCards.add("7S")
//        allCards.add("7C")
//        allCards.add("5D")
//        allCards.add("8D")
//        allCards.add("6D")
//        allCards.add("7D")
//        allCards.add("QH")
//        allCards.add("KC")
//
//        println("mylog allCards: $allCards")
//
//        val movesFinder = MovesFinder()
//        val moves = movesFinder.findMoves(allCards)
//        println("mylog print all moves: " + moves.size)
//        for (i in moves.indices) {
//            println("mylog move: " + moves[i].toString())
//        }
//        println("mylog print all moves-----done")

    }


}

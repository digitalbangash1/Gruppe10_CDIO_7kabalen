package org.tensorflow.lite.examples.detection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess


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

    }


}

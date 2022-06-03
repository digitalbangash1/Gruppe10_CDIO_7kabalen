package org.tensorflow.lite.examples.detection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class FrontPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frontpage)
        val nextButton = findViewById<Button>(R.id.next_btn)
        nextButton.setOnClickListener(){
            val switchActivityIntent = Intent(this, DetectorActivity::class.java)
            startActivity(switchActivityIntent)

        }

    }


}

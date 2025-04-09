package com.example.dicerollerxml

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var imageView1: ImageView
    lateinit var imageView2: ImageView
    lateinit var rollButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        rollButton = findViewById(R.id.rollButton)

        rollButton.setOnClickListener {
            val hasil = (1..6).random()
            val hasil2 = (1..6).random()

            setImage(imageView1, hasil)
            setImage(imageView2, hasil2)

            val message = if (hasil == hasil2) "Selamat, anda dapat dadu double!" else "Anda belum beruntung!"
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setImage(imageView: ImageView, hasil: Int) {
        val imageResId = when (hasil) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        imageView.setImageResource(imageResId)
    }
}
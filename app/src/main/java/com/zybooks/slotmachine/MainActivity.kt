package com.zybooks.slotmachine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


var credits : Int = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultTextView: TextView = findViewById(R.id.status)
        val resultTextView2: TextView = findViewById(R.id.money)
        val pullButton: Button = findViewById(R.id.button)
        val addBalance: Button = findViewById(R.id.button2)
        val payOutTable: Button = findViewById(R.id.button3)

        /**
         * Loads balance from previous play
         */
        loadData()
        // update balance with loaded data
        resultTextView2.text = "$credits credits"

        /**
         * Updates credits number depending on result of pull.
         */
        pullButton.setOnClickListener {

            if(credits === 0){
                resultTextView.text = "No balance, click Add Balance to add more!"
            }
            else {
                // makes pull and add balance unclickable to let animation run
                pullButton.setEnabled(false)
                pullButton.postDelayed(Runnable { pullButton.setEnabled(true) }, 1000)
                addBalance.setEnabled(false)
                addBalance.postDelayed(Runnable { addBalance.setEnabled(true) }, 1000)

                // displays and animates message depending on win or lose
                when (val win = pullSlot()) {
                    -1 -> {
                        resultTextView.text = "Unfortunately you lost, try again!"
                        val textFadeInAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fadein3)
                        resultTextView.startAnimation(textFadeInAnimation1)

                        credits += win
                        val textFadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fadein3)
                        resultTextView2.startAnimation(textFadeInAnimation2)
                        resultTextView2.text = " $credits credits"
                        saveData("credits", credits)
                    }
                    else -> {
                        resultTextView.text = "Congratulations! You won $win credits!"
                        val textFadeInAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fadein3)
                        resultTextView.startAnimation(textFadeInAnimation1)

                        credits += win
                        val textFadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fadein3)
                        resultTextView2.startAnimation(textFadeInAnimation2)
                        resultTextView2.text = "$credits credits"
                        saveData("credits", credits)
                    }
                }
            }

        }
        addBalance.setOnClickListener {
            credits += 25
            resultTextView2.text = "$credits credits"
            saveData("credits", credits)
        }

        payOutTable.setOnClickListener {
            val intent = Intent(this, PayOutTable::class.java)
            startActivity(intent)
        }

    }

    /**
     * Pull the Slot and update the screen with the result.
     */
    private fun pullSlot() : Int {
        val slotPull1 = (1..6).random()
        val slotPull2 = (1..6).random()
        val slotPull3 = (1..6).random()

        val win = slotResult(slotPull1, slotPull2, slotPull3)

        // Update the screen with slot pull results and animate it
        val diceImage: ImageView = findViewById(R.id.imageView)
        val myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein)
        diceImage.startAnimation(myFadeInAnimation)

        val diceImage2: ImageView = findViewById(R.id.imageView2)
        val myFadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fadein2)
        diceImage2.startAnimation(myFadeInAnimation2)

        val diceImage3: ImageView = findViewById(R.id.imageView3)
        val myFadeInAnimation3 = AnimationUtils.loadAnimation(this, R.anim.fadein3)
        diceImage3.startAnimation(myFadeInAnimation3)

        when (slotPull1) {
            1 -> diceImage.setImageResource(R.drawable.cherry)
            2 -> diceImage.setImageResource(R.drawable.lemon)
            3 -> diceImage.setImageResource(R.drawable.orange)
            4 -> diceImage.setImageResource(R.drawable.watermelon)
            5 -> diceImage.setImageResource(R.drawable.slot_machine)
            6 -> diceImage.setImageResource(R.drawable.casino)
        }

        when (slotPull2) {
            1 -> diceImage2.setImageResource(R.drawable.cherry)
            2 -> diceImage2.setImageResource(R.drawable.lemon)
            3 -> diceImage2.setImageResource(R.drawable.orange)
            4 -> diceImage2.setImageResource(R.drawable.watermelon)
            5 -> diceImage2.setImageResource(R.drawable.slot_machine)
            6 -> diceImage2.setImageResource(R.drawable.casino)
        }

        when (slotPull3) {
            1 -> diceImage3.setImageResource(R.drawable.cherry)
            2 -> diceImage3.setImageResource(R.drawable.lemon)
            3 -> diceImage3.setImageResource(R.drawable.orange)
            4 -> diceImage3.setImageResource(R.drawable.watermelon)
            5 -> diceImage3.setImageResource(R.drawable.slot_machine)
            6 -> diceImage3.setImageResource(R.drawable.casino)
        }
        return win
    }

    /**
     * Calculates result based on slot reels
     */
    private fun slotResult (result1: Int, result2: Int, result3: Int) : Int {
        val win = when {
            result1 === 1 && result2 !== 1 -> 2
            result1 === 1 && result2 === 1 && result3 !== 1 -> 5
            result1 === 1 && result2 === 1 && result3 === 1 -> 10
            result1 === 2 && result2 === 2 && result3 === 2 -> 15
            result1 === 3 && result2 === 3 && result3 === 3 -> 20
            result1 === 4 && result2 === 4 && result3 === 4 -> 50
            result1 === 5 && result2 === 5 && result3 === 5 -> 100
            result1 === 6 && result2 === 6 && result3 === 6 -> 500
            else -> -1
        }
    return win
    }

    /**
     * Saves balance after pulling
     */
    private fun saveData(key: String, value: Int) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(key, value)
            apply()
        }

    }

    /**
     * Loads balance from previous play
     */
    private fun loadData() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        var credits = sharedPref.getInt("credits", 0)
        }
}


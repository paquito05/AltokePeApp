package com.example.altokepeapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class ATKRadioButton(context: Context, attributeSet: AttributeSet) :AppCompatRadioButton(context,attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont(){

        //this is used to get the file from the assets folder and set it to the
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }

}
package com.chris.thomson.midlandsriders.Managers

import android.content.Context
import android.graphics.Typeface

object FontsManager {
    val SHADOW_CARD = "shadowcard.ttf"
    fun getTypeface(font: String = "", context: Context): Typeface = Typeface.createFromAsset(context.assets,"font/$font")
}


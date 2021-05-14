package com.tkdev.dogs.common

import android.content.Context
import androidx.annotation.StringRes

interface StringWrapper {
    fun getString(@StringRes stringResId: Int): String
}

class StringWrapperDefault(private val context: Context) : StringWrapper {

    override fun getString(stringResId: Int): String =
        context.getString(stringResId)

}

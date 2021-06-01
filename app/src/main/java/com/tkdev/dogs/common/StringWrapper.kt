package com.tkdev.dogs.common

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface StringWrapper {
    fun getString(@StringRes stringResId: Int): String
}

@Singleton
class StringWrapperDefault @Inject constructor(@ApplicationContext private val context: Context) : StringWrapper {

    override fun getString(stringResId: Int): String =
        context.getString(stringResId)

}

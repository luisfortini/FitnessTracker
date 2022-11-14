package co.tiagoaguiar.fitnesstracker

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MainItem (
    val id: Int,
    @DrawableRes val drawableID: Int,
    @StringRes val txtStringId: Int,
    val color: Int

        )
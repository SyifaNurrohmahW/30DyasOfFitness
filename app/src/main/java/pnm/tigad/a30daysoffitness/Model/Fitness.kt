package pnm.tigad.a30daysoffitness.Model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Fitness (
    @StringRes val title: Int,
    @DrawableRes val image: Int,
    @StringRes val desc: Int
)
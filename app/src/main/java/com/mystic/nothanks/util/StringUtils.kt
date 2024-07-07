package com.mystic.nothanks.util

import java.util.Locale

const val defaultBrandSeparator = ","

fun String.engLowercase() = lowercase(Locale.ENGLISH)
fun String.engUppercase() = uppercase(Locale.ENGLISH)


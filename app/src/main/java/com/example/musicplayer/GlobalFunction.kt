package com.example.musicplayer

import java.text.Normalizer
import java.util.regex.Pattern

class GlobalFunction {
    companion object{
        fun getTextSearch(input: String?): String? {
            val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            return pattern.matcher(nfdNormalizedString).replaceAll("")
        }
    }
}
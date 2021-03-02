package com.example.piano

fun halfToneKeyPositions(fullToneKeyWidth: Float, densityDPI: Float, position: Int, padding: Float, halfToneKeywidth: Float):Float {
    val paddingToDensitypixels = padding*densityDPI/160
    val pos = fullToneKeyWidth*position-halfToneKeywidth/2 + (paddingToDensitypixels/2)*position
    return pos*densityDPI/160
}
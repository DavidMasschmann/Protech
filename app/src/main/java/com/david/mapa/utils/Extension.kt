package com.david.mapa.utils

fun String.limitDescription(characters: Int): String {
    if(this.length > characters){
        val firstCharacter = 0
        return "${this.substring(firstCharacter, characters)}..."
    }
    return this
}
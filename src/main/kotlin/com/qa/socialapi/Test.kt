package com.qa.socialapi

import java.io.File
import javax.imageio.ImageIO

fun main() {
    val icon = object {}.javaClass.classLoader.getResource("app-development.png")
    val fileData = ImageIO.read(icon)
    val outputFile = File("saved.png")
    println(fileData.data)
    ImageIO.write(fileData, "png", outputFile)
}

package com.danielrodriguez.topofreddit

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Paths

object JsonReaderFromFile {

    private val rawDirectory = Paths.get("src/main/res/raw").toFile().absolutePath

    private fun readJsonFile(path: String): String {
        val br = BufferedReader(InputStreamReader(FileInputStream(path)))
        val sb = StringBuilder()
        var line: String? = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }
        return sb.toString()
    }

    fun readFromRaw(filename: String): String {
        return readJsonFile("$rawDirectory/$filename")
    }
}
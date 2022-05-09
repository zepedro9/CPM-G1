package com.cpm.g1.printer

import java.io.*

fun readStream(input: InputStream): String {
  var reader: BufferedReader? = null
  var line: String?
  val response = StringBuilder()
  try {
    reader = BufferedReader(InputStreamReader(input))
    while (reader.readLine().also{ line = it } != null)
      response.append(line)
  }
  catch (e: IOException) {
    return "com.cpm.g1.printer.readStream: " + e.message
  }
  finally {
    reader?.close()
  }
  return response.toString()
}
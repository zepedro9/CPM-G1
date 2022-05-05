package com.cpm.g1.theacmeelectronicsshop

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
    return "com.cpm.g1.theacmeelectronicsshop.readStream: " + e.message
  }
  finally {
    reader?.close()
  }
  return response.toString()
}
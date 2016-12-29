package com.outr.stripe

import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._
import io.circe.syntax._

object Pickler {
  private val entryRegex = """"(.+)": (.+)""".r
  private val snakeRegex = """_([a-z])""".r
  private val camelRegex = """([A-Z])""".r

  def read[T](jsonString: String)(implicit decoder: Decoder[T]): T = {
    // Snake to Camel
    val json = entryRegex.replaceAllIn(jsonString, (regexMatch) => {
      val key = snakeRegex.replaceAllIn(regexMatch.group(1), (snakeMatch) => {
        snakeMatch.group(1).toUpperCase
      })
      s""""$key": ${regexMatch.group(2)}"""
    })
    // Use Circe to decode the JSON into a case class
    decode[T](json).getOrElse(throw new PicklerException(s"Unable to decode $jsonString"))
  }

  def write[T](value: T)(implicit encoder: Encoder[T]): String = {
    val jsonString = value.asJson.spaces2
    entryRegex.replaceAllIn(jsonString, (regexMatch) => {
      val key = camelRegex.replaceAllIn(regexMatch.group(1), (camelMatch) => {
        s"_${camelMatch.group(1).toLowerCase}"
      })
      s""""$key": ${regexMatch.group(2)}"""
    })
  }
}

class PicklerException(message: String) extends RuntimeException(message)
package com.ethanmcdonough.PBot

import play.api.libs.json._
import java.time.Instant
import play.api.libs.functional.syntax._

case class Program(id: Long, createdStr: String, code: String) {
  val created: Instant = Instant.parse(createdStr)

  def epoch: Long = created.toEpochMilli
}

object Program {
  implicit val reads: Reads[Program] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "created").read[String] and
      (JsPath \ "revision" \ "code").read[String]
    ) (Program.apply _)
}
package com.ethanmcdonough.PBot

import play.api.libs.json.{JsValue, Json}
import scalaj.http.{Http, HttpRequest}

class CommentKaidIterator(id: Long, protected val session: Session) extends KAIter[String] {
  override protected val arrayName: String = "feedback"
  override protected val completeName: String = "isComplete"

  override def request: HttpRequest = Http(s"${root}api/internal/discussions/scratchpad/$id/comments").params(
    "casing" -> "camel", "sort" -> "1",
    "subject" -> "all", "limit" -> limit.toString,
    "page" -> page.toString, "cursor" -> cursor,
    "projection" -> """{"feedback":[{"normal":{"authorKaid":1}}],"isComplete":1,"cursor":1}""",
    "lang" -> "en", "_" -> System.currentTimeMillis.toString)
    .headers("X-KA-FKey" -> session.csrfToken).cookies(session.cookies)

  protected def extractFromJsValue(jsValue: JsValue): Option[String] = (jsValue \ "authorKaid").asOpt[String]
}

object CommentKaidIterator {
  def apply(id: Long, session: Session) = new CommentKaidIterator(id, session)
}
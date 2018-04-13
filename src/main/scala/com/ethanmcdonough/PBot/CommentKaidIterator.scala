package com.ethanmcdonough.PBot

import scala.collection.Iterator
import play.api.libs.json.{ Json, JsValue }
import scalaj.http.Http

class CommentKaidIterator(id: Long, session: Session) extends Iterator[List[String]] {
  private val root: String = "https://www.khanacademy.org/"
  private val limit: Int = 10
  private var page: Int = 0
  private var isComplete: Boolean = false
  private var cursor: String = ""
  override def hasNext: Boolean = !isComplete
  override def next: List[String] = {
    val json: JsValue = Json.parse(Http(s"${root}api/internal/discussions/scratchpad/$id/comments").params(
      "casing" -> "camel", "sort" -> "1",
      "subject" -> "all", "limit" -> limit.toString,
      "page" -> page.toString, "cursor" -> cursor,
      "projection" -> """{"feedback":[{"normal":{"authorKaid":1}}],"isComplete":1,"cursor":1}""",
      "lang" -> "en", "_" -> System.currentTimeMillis.toString)
      .headers("X-KA-FKey" -> session.csrfToken).cookies(session.cookies)
      .asString.body)
    (json \ "isComplete").asOpt[Boolean].map { jsonIsComplete =>
      page += 1
      isComplete = jsonIsComplete
      (json \ "cursor").asOpt[String].map(cursor = _)
    }
    (json \\ "authorKaid").flatMap(_.asOpt[String]).toList
  }
}

object CommentKaidIterator {
  def apply(id: Long, session: Session) = new CommentKaidIterator(id, session)
}
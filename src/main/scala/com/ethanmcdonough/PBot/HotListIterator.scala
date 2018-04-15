package com.ethanmcdonough.PBot

import play.api.libs.json.JsValue
import scalaj.http.{Http, HttpRequest}

import scala.util.matching.Regex

class HotListIterator(sort: HotListSortTypes.Value, protected val session: Session, topic: Topics.Value) extends KAIter[String] {
  private val urlRegex: Regex = raw"https?:\/\/(?:www|[a-zA-Z]{2})\.khanacademy\.org\/.+\/.+\/(\d+)".r
  override protected val limit: Int = 30
  override protected val completeName: String = "complete"
  override protected val arrayName: String = "scratchpads"

  override protected def extractFromJsValue(jsValue: JsValue): Option[String] = (jsValue \ "url").asOpt[String].flatMap {
    case urlRegex(id) => Option(id)
    case _ => None
  }

  override protected def request: HttpRequest = Http(s"${root}/api/internal/scratchpads/top").params(Map(
    "casing" -> "camel", "sort" -> sort.id.toString, "page" -> page.toString,
    "limit" -> limit.toString, "subject" -> "all", "topic_id" -> topic.toString,
    "lang" -> "en", "_" -> System.currentTimeMillis.toString,
    "projection" -> """{"complete":1,"cursor":1,"scratchpads":[{"url":1}]}""") ++ (if (cursor.length > 0) Map("cursor" -> cursor) else Map.empty[String, String]))
}

object HotListIterator {
  def apply(sort: HotListSortTypes.Value, session: Session, topic: Topics.Value): HotListIterator = new HotListIterator(sort, session, topic)
}
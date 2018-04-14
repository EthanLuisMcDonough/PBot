package com.ethanmcdonough.PBot

import scalaj.http.HttpRequest
import play.api.libs.json.{Json, JsValue}

trait KAIter[A] extends Iterator[List[A]] {
  protected val root: String = "https://www.khanacademy.org/"
  protected val limit = 10
  protected val completeName: String
  protected val session: Session
  protected var cursor: String = ""
  protected var isComplete: Boolean = false
  protected var page: Int = 0
  protected val arrayName: String

  protected def extractFromJsValue(jsValue: JsValue): Option[A]

  protected def request: HttpRequest

  def next: List[A] = Option(json).map { json =>
    (json \ completeName).asOpt[Boolean].foreach { jsonIsComplete =>
      page += 1
      isComplete = jsonIsComplete
      (json \ "cursor").asOpt[String].foreach(cursor = _)
    }
    (json \ arrayName).asOpt[List[JsValue]].map(_.flatMap(extractFromJsValue)).toList.flatten
  }.get

  def hasNext: Boolean = !isComplete

  private def json: JsValue = Json.parse(request.cookies(session.cookies).asString.body)
}

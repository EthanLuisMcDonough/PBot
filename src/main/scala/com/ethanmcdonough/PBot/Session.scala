package com.ethanmcdonough.PBot

import java.net.HttpCookie
import play.api.libs.json.Json
import scalaj.http.Http

class Session(val csrfToken: String, val KAID: String, val kaid: String, val user: User) {
  private val csrfCookie: HttpCookie = new HttpCookie("fkey", csrfToken)
  private val KAIDcookie: HttpCookie = new HttpCookie("KAID", KAID)
  val cookies = Seq(KAIDcookie, csrfCookie)

  private val root: String = "https://www.khanacademy.org/"

  def firstPages(sort: HotListSortTypes.Value, count: Int): List[String] = {
    val hlIter: HotListIterator = HotListIterator(sort, this, Topics.COMPUTER_PROGRAMMING)
    List.fill(count)(hlIter.next).flatten
  }

  def hasCommentedOn(programId: Long): Boolean = CommentKaidIterator(programId, this).flatten.contains(kaid)

  def program(programId: Long): Option[Program] = Json.parse(Http(s"${root}api/labs/scratchpads/$programId")
    .param("projection", """{"revision":{"code":1},"created":1,"id":1}""")
    .cookies(cookies).header("X-KA-FKey", csrfToken).asString.body).asOpt[Program](Program.reads)

  def commentOn(programId: Long, comment: String): Boolean = Option(Http(s"${root}api/internal/discussions/scratchpad/$programId/comments")
    .params("casing" -> "camel", "lang" -> "en", "_" -> System.currentTimeMillis.toString)
    .headers("X-KA-FKey" -> csrfToken, "Content-type" -> "application/json")
    .cookies(cookies)
    .postData(Json.obj("text" -> comment).toString).asString.code)
    .exists(e => e >= 200 && e < 300)
}

object Session {
  def apply(csrfToken: String, KAID: String, kaid: String, user: User): Session = new Session(csrfToken, KAID, kaid, user)
}
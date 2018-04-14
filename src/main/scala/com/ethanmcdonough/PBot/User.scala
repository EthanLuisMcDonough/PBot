package com.ethanmcdonough.PBot

import scalaj.http.Http
import scala.collection.Seq

import java.util.Objects

import play.api.libs.json.Json

class User(val identifier: Identifier, val password: String) {
  private val root: String = "https://www.khanacademy.org/"
  private val loginUrl: String = s"${root}login"

  override def toString: String = s"User($identifier, $password)"

  override def equals(o: Any): Boolean = o match {
    case user: User => user.identifier == identifier && user.password == password;
    case _ => false
  }

  override def hashCode: Int = Objects.hash(identifier, password)

  def login: Option[Session] = Http(loginUrl).asString.cookies
    .find(_.getName.toLowerCase == "fkey")
    .map(fkey => Http(loginUrl).postForm(Seq(
      "fkey" -> fkey.getValue,
      "identifier" -> identifier.toString,
      "password" -> password,
      "continue" -> "null")).cookie(fkey).asString.cookies :+ fkey)
    .flatMap(cookies => for {
      fkey <- cookies.find(_.getName == "fkey")
      kaidCookie <- cookies.find(_.getName == "KAID")
      kaid <- (Json.parse(Http(s"${root}api/internal/user/profile")
        .param(identifier.kind.toString.toLowerCase, identifier.toString)
        .headers("X-KA-FKey" -> fkey.getValue, "projection" -> """{"kaid":1}""")
        .cookies(cookies).asString.body) \ "kaid").asOpt[String]
    } yield Session(fkey.getValue, kaidCookie.getValue, kaid, this))
}

object User {
  def apply(identifier: Identifier, password: String) = new User(identifier, password)
}
package com.ethanmcdonough.PBot

import scala.util.matching.Regex

sealed abstract class IdentifierTypes(val name: String, val regex: Regex)

object IdentifierTypes {

  case object Username extends IdentifierTypes("USERNAME", raw"[a-zA-Z][a-zA-Z\d]{2,}".r)

  case object Email extends IdentifierTypes("EMAIL", "[a-zA-Z0-9.!#$%&\u2019*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*".r)

  val values: List[IdentifierTypes] = List(Username, Email)
}
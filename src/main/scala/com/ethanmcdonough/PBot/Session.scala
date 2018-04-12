package com.ethanmcdonough.PBot

import scala.annotation.tailrec

class Session(val csrfToken: String, val KAID: String, val kaid: String, val user: User) {
  def hasCommentedOn(programId: Long): Boolean = true
}

object Session {
  def apply(csrfToken: String, KAID: String, kaid: String, user: User): Session = new Session(csrfToken, KAID, kaid, user)
}
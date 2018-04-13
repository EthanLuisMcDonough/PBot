package com.ethanmcdonough.PBot

object Main {
  def main(args: Array[String]): Unit = {
    val user = User(Identifier(args(0)), args(1))
    val session: Session = user.login.get
    println(session.csrfToken)
    println(session.commentOn(6561461926133760l, System.currentTimeMillis + "ms"))
    println(session.hasCommentedOn(6561461926133760l))
  }
}
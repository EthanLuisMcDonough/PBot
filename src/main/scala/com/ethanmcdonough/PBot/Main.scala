package com.ethanmcdonough.PBot

import com.typesafe.config.{ Config, ConfigFactory }

object Main {
  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load("credentials")
    val identifier: Identifier = Identifier(config.getString("pbot.identifier"))
    val password: String = config.getString("pbot.password")
    val user: User = User(identifier, password)
    val session: Session = user.login.get
    println(session.csrfToken)
    //println(session.commentOn(6561461926133760l, System.currentTimeMillis + "ms"))
    println(session.hasCommentedOn(5120653246103552l))
  }
}
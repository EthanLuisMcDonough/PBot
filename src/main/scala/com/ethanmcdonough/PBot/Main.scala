package com.ethanmcdonough.PBot

import com.typesafe.config.{Config, ConfigFactory}

object Main {
  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load("credentials")
    val identifier: Identifier = Identifier(config.getString("pbot.identifier"))
    val password: String = config.getString("pbot.password")
    val user: User = User(identifier, password)
    val session: Session = user.login.get
    println(session.csrfToken)
    //println(session.commentOn(6561461926133760l, System.currentTimeMillis + "ms"))
    //println(session.hasCommentedOn(4844283525201920l))
    val v = new HotListIterator(HotListSortTypes.CONTESTS, session, Topics.COMPUTER_PROGRAMMING)
    for (i <- 1 to 3) { println(i); println(v.next) }

  }
}
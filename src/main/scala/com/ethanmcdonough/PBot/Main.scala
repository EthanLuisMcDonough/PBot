package com.ethanmcdonough.PBot

import com.typesafe.config.{Config, ConfigFactory}

object Main {
  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load("credentials")
    val identifier: Identifier = Identifier(config.getString("pbot.identifier"))
    val password: String = config.getString("pbot.password")
    val user: User = User(identifier, password)
    val session: Session = user.login.get

  }
}
package com.ethanmcdonough.PBot

import java.net._
import java.util.Scanner
import java.util.regex.Pattern

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.{ Success, Failure }
import ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {
    val user = User(Identifier(args(0)), args(1))
    val session: Session = user.login.get
    println(session.kaid)
    
  }
}
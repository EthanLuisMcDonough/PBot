package com.ethanmcdonough.PBot

import com.ethanmcdonough.KAClient._
import java.net._
import java.util.Scanner
import java.util.regex.Pattern

import org.apache.http.client.methods.CloseableHttpResponse

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.{Success, Failure}
import ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]) : Unit = {
    val identifier: String = args(0)
    val password: String  = args(1)
    
    val client: KAClient = new KAClient
    val login: Future[Boolean] = client.login(identifier, password)
    
    login onComplete {
      case Success(posts) => println(s"Logged in as $identifier")
      case Failure(t) => println("Error: " + t.getMessage + " " + t.getStackTrace)
    }
  }
}
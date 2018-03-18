package com.ethanmcdonough.KAClient

import scala.collection.JavaConversions._

import org.apache.http.HttpEntity
import org.apache.http.util.EntityUtils
import org.apache.http.cookie.Cookie

object Utils {
  def tryWith[E <: AutoCloseable](closable: E)(callback: E => Unit) : Unit = {
    try {
      callback(closable)
    } finally {
      if (closable != null)
        closable.close
    }
  }
  def withEntity[E <: HttpEntity](entity: E)(callback: E => Unit) : Unit = {
    try {
      callback(entity)
    } finally {
      EntityUtils.consume(entity)
    }
  }
  def getCookie(cookies: java.util.List[Cookie], name: String) : Cookie = {
    var cookie: Cookie = null
    for (c <- cookies)
      if (c != null && c.getName != null && c.getName.equals(name))
        cookie = c
    cookie
  }
}
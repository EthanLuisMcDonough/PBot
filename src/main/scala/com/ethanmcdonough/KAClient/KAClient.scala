package com.ethanmcdonough.KAClient

import scala.collection.JavaConversions._

import scala.collection.immutable.List

import org.apache.http.client.methods.{ CloseableHttpResponse, HttpGet, HttpPost }
import org.apache.http.impl.client.{ CloseableHttpClient, BasicCookieStore, HttpClientBuilder }
import org.apache.http.cookie.Cookie
import org.apache.http.util.EntityUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.{ BasicHeader, BasicNameValuePair }
import org.apache.http.{ HttpHeaders, Header, NameValuePair, HttpEntity }

import com.fasterxml.jackson.databind.{ ObjectMapper, JsonNode }

import scala.concurrent.{ Future, ExecutionContext }

class KAClient {
  private var identifier: String = null
  private var password: String = null
  private var fkey: String = null

  private val root: String = "https://www.khanacademy.org/"

  private val cookies: BasicCookieStore = new BasicCookieStore
  private val client: CloseableHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookies).build

  def loggedIn : Boolean = identifier != null && password != null && fkey != null

  def login(identifier: String, password: String, ctx: ExecutionContext = ExecutionContext.global): Future[Boolean] = {
    logout
    Future {
      var success: Boolean = false

      val getCSRFTokenReq: HttpGet = new HttpGet(root)
      Utils.tryWith(client.execute(getCSRFTokenReq).asInstanceOf[CloseableHttpResponse]) { e =>
        val fkeyCookie: Cookie = Utils.getCookie(cookies.getCookies, "fkey")
        if(fkeyCookie != null)
          fkey = fkeyCookie.getValue
      }

      val loginReq: HttpPost = new HttpPost(s"${root}login")
      loginReq.setHeaders(Array[Header](
        new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded"),
        new BasicHeader("X-KA-FKey", fkey)))
      val params: java.util.List[NameValuePair] = List[NameValuePair](
        new BasicNameValuePair("fkey", fkey),
        new BasicNameValuePair("identifier", identifier),
        new BasicNameValuePair("password", password))
      loginReq.setEntity(new UrlEncodedFormEntity(params))

      Utils.tryWith(client.execute(loginReq).asInstanceOf[CloseableHttpResponse]) { response =>
        if(response.getStatusLine.getStatusCode >= 200 && response.getStatusLine.getStatusCode < 300) {
          Utils.withEntity(response.getEntity) { entity =>
            if(entity.getContentType.getValue.toLowerCase.contains("json")) {
              Utils.tryWith(entity.getContent) { body =>
                val mapper: ObjectMapper = new ObjectMapper
                val response: JsonNode = mapper.readTree(body).asInstanceOf[JsonNode]
                if (!response.isNull && !response.get("continue").isNull) {
                  if(Utils.getCookie(cookies.getCookies, "KAID") != null) {
                    success = true
                  }
                }
              }
            }
          }
        }
      }

      success
    }(ctx)
  }

  def logout : Unit = {
    identifier = null
    password = null
    fkey = null
    cookies.clear
  }
}
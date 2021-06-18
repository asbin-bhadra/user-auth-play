package model

import org.joda.time.LocalDateTime

import java.util.UUID
import scala.collection.mutable

case class Session(token:String, username:String, expiration:LocalDateTime)

object SessionDAO {
  private val sessions : mutable.Map[String, Session] = mutable.Map()

  def getSession(token:String) ={
    sessions.get(token)
  }

  def generateToken(username:String)={
    val token = s"$username-token-${UUID.randomUUID().toString}"
    sessions.put(token, Session(token, username,LocalDateTime.now().plusHours(6)))
    token
  }

}

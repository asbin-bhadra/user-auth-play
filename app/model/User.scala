package model

import play.api.data.Form
import play.api.data.Forms.{mapping, text}

import scala.collection.mutable

case class User(name:String, username:String, password:String)

object UserDAO{
  private val users : mutable.Map[String,User] = mutable.Map()

  def getUser(username:String): Option[User] ={
    users.get(username)
  }

  def addUser(name:String, username:String,password:String): Option[User] ={
    if(users.contains(username)){
      None
    }
    else{
      val user = User(name, username,password)
      users.put(username,user)
      Some(user)
    }
  }

}

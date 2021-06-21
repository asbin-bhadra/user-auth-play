package dao

import model.{SessionRow, User, UserModel, UserRow}

import scala.concurrent.Future

trait UserDAO {

  def getUser(username:String): Future[Seq[UserRow]]
  def addUser(user:UserModel): Future[Int]
  def checkCred(username:String, password:String) : Future[Boolean]
  def getSessionData(token:String) : Future[Seq[SessionRow]]
  def getUser2(username:String): Future[Option[UserRow]]

}

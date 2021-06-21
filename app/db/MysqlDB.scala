package db

import dao.UserDAO
import model.{SessionRow, User, UserModel, UserRow}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class MysqlDB @Inject()(dbConfigProvider : DatabaseConfigProvider)(implicit ec : ExecutionContext) extends UserDAO {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._
  private val userQuery = TableQuery[UserTableSchema]
  private val sessionQuery = TableQuery[SessionTableSchema]

  override def getUser(username: String): Future[Seq[UserRow]] = {
    db.run(userQuery.filter(_.username === username).result)
  }
  override def getUser2(username: String): Future[Option[UserRow]] = {
    db.run(userQuery.filter(_.username === username).take(1).result.headOption)
  }

  override def addUser(user:UserModel): Future[Int] = {
    val result = isUsernameExist(user.username)
    result.flatMap{ exist =>
      if(!exist){
        db.run(userQuery += UserRow(UUID.randomUUID(),user.name, user.username, user.password))
      }
      else{
        Future(0)
      }
    }
  }

  def addSession(session : SessionRow)={
    db.run(sessionQuery+=session)
  }

  override def checkCred(username: String, password: String): Future[Boolean] = {
    db.run(userQuery.filter(user => (user.username === username && user.password === password)).exists.result)
  }


  override def getSessionData(token: String): Future[Seq[SessionRow]] = {
    db.run(sessionQuery.filter(_.token === token).result)
  }

  private def isUsernameExist(username:String) ={
    db.run(userQuery.filter(_.username === username).exists.result)
  }

}

package services

import db.MysqlDB
import model.{SessionDAO, SessionRow, UserRow}
import org.joda.time.DateTime

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class SessionService @Inject()(db:MysqlDB){

  def generateToken(username:String): Future[Option[String]] ={
    val token = s"token-${UUID.randomUUID().toString}"
    db.addSession(SessionRow(UUID.randomUUID(),token,username,DateTime.now().plusHours(12))).map{
      case 1 => Some(token)
      case 0 => None
    }
  }

  def checkSession(sessionToken: String):Future[Option[UserRow]] = {
    val sessionData = db.getSessionData(sessionToken)

    val expirationData: Future[Option[DateTime]] = sessionData.flatMap{
      data => data.headOption match {
        case Some(value) =>Future(Some(data.head.expiration))
        case None => Future(None)
      }
    }

    val checkValidity = expirationData.map{
      data => data match {
        case Some(date)=> if(date.isAfter(DateTime.now())) true else false
        case None => false
      }
    }
    val username = sessionData.flatMap{
      data => Future(data.head.username)
    }
    val userData: Future[Option[UserRow]] = checkValidity.flatMap{
      valid =>
        if(valid)
          username.flatMap(data => db.getUser2(data))
        else
          Future(None)
    }
    userData
  }

}

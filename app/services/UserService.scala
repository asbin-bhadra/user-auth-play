package services

import dao.UserDAO
import db.MysqlDB
import model.{Failed, Response, Successful, SuccessfulWithMessage, UserModel}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserService @Inject()(db : MysqlDB) {

  def adduser(user : UserModel): Future[Response] ={
    db.addUser(user).map{
      case 1 => Successful
      case 0 => Failed
    }
  }

  def checkLoginCred(username:String, password:String): Future[Response] ={
    db.checkCred(username,password).map{
      case true => Successful
      case false => Failed
    }
  }

}

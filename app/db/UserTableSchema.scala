package db

import model.UserRow
import slick.jdbc.MySQLProfile.api._

import java.util.UUID

class UserTableSchema(tag:Tag) extends Table[UserRow](tag, "USER_DETAILS"){

  def id = column[UUID]("USER_ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def username = column[String]("USERNAME")
  def password = column[String]("PASSWORD")

  override def * = (id, name, username, password) <> (UserRow.tupled, UserRow.unapply)
}



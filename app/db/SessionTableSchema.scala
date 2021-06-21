package db
import model.SessionRow
import org.joda.time.DateTime
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

class SessionTableSchema(tag:Tag) extends Table[SessionRow](tag, "Session_Details") {
  implicit def jodaTimeMapping: BaseColumnType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp.getTime)
  )

  def id = column[UUID]("SESSION_ID", O.PrimaryKey)
  def token = column[String]("TOKEN")
  def username = column[String]("USERNAME")
  def expiration = column[DateTime]("expiration")


  override def * = (id, token, username, expiration) <> (SessionRow.tupled, SessionRow.unapply)
}

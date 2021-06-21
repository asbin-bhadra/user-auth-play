package marshaling

import model.{SessionRow, UserModel, UserRow}
import org.joda.time.DateTime
import play.api.libs.json.{Json, Reads, Writes}

import model.SessionRow
import org.joda.time.DateTime
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

trait CustomJsonMarshaling {
  implicit def jodaTimeMapping: BaseColumnType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp.getTime)
  )

  implicit val userModelToJson: Writes[UserModel] = Json.writes[UserModel]
  implicit val userModelFromJson: Reads[UserModel] = Json.reads[UserModel]

  implicit val SessionModelToJson: Writes[SessionRow] = Json.writes[SessionRow]
  implicit val SessionModelFromJson: Reads[SessionRow] = Json.reads[SessionRow]

}

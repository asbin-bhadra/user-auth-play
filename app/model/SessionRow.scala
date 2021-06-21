package model

import org.joda.time.DateTime

import java.time.LocalDateTime
import java.util.UUID
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class SessionRow(id:UUID, token:String, username:String, expiration:DateTime)

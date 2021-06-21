package model

trait Response

case class SuccessfulWithMessage(message:String) extends Response
case class FailedWithMessage(message:String) extends Response
case object Successful extends Response
case object Failed extends Response

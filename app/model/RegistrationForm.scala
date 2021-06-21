package model
import play.api.data.Form
import play.api.data.Forms.{mapping, text}

case class RegisterForm(name:String, username: String, password: String)

object RegisterForm {
  val form: Form[RegisterForm] = Form(
    mapping(
      "name" -> text,
      "username" -> text,
      "password" -> text,
    )(RegisterForm.apply)(RegisterForm.unapply)
  )
}


package controllers

import model.{SessionDAO, User, UserDAO}
import org.joda.time.LocalDateTime

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

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


case class LoginForm(username: String, password: String)

object LoginForm {
  val form: Form[LoginForm] = Form(
    mapping(
      "username" -> text,
      "password" -> text,
    )(LoginForm.apply)(LoginForm.unapply)
  )
}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {


  def create(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(RegisterForm.form,None))
  }

  def save(): Action[AnyContent] = Action { implicit request =>
    val formData: RegisterForm = RegisterForm.form.bindFromRequest.get
    UserDAO
      .addUser(formData.name,formData.username,formData.password)
      .map(user => Ok(views.html.signup(RegisterForm.form,Some("User has been registered"))))
      .getOrElse(Ok(views.html.signup(RegisterForm.form,Some("Username is taken"))))
  }



  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    println(request.session.get("SessionToken"))
    Ok(views.html.index())
  }

  def dashboard(): Action[AnyContent] = Action{ implicit request:Request[AnyContent]=>

    val userOpt = extractuser(request)
    userOpt.map(user => Ok(views.html.dashboard(user)))
      .getOrElse(Unauthorized(views.html.defaultpages.unauthorized()))
  }

  def loginForm(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(LoginForm.form, None))
  }

  def login(): Action[AnyContent] = Action { implicit request:Request[AnyContent]=>
    val formData: LoginForm = LoginForm.form.bindFromRequest.get
    if(isValidLogin(formData.username,formData.password)){
      val token = SessionDAO.generateToken(formData.username)
      Redirect(routes.HomeController.dashboard).withSession(request.session + ("SessionToken" -> token))
    }
    else
      {
        Ok(views.html.login(LoginForm.form, Some("Username or password is incorrect")))
      }
  }
  def logout(): Action[AnyContent] = Action { implicit request:Request[AnyContent]=>
    Ok(views.html.logout()).withNewSession
  }

  private def isValidLogin(username: String, password: String) ={
    UserDAO.getUser(username).exists(_.password == password)
  }

  private def extractuser(req: Request[AnyContent])={
    val sessionTokenOpt = req.session.get("SessionToken")
    sessionTokenOpt
      .flatMap(token => SessionDAO.getSession(token))
      .filter(_.expiration.isAfter(LocalDateTime.now()))
      .map(_.username)
      .flatMap(UserDAO.getUser)
  }
}

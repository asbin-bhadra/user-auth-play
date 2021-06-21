package controllers

import marshaling.CustomJsonMarshaling
import model.{Failed, FailedWithMessage, LoginForm, RegisterForm, Response, SessionDAO, Successful, SuccessfulWithMessage, UserModel, UserRow}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.Results.BadRequest
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, Result}
import services.{SessionService, UserService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(cc : ControllerComponents, userService : UserService, sessionService : SessionService)(implicit ec : ExecutionContext) extends AbstractController(cc) with I18nSupport with CustomJsonMarshaling{


  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def create(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(RegisterForm.form,None))
  }

  def addUser()= Action async{ implicit request:Request[AnyContent] =>
    val formData: RegisterForm = RegisterForm.form.bindFromRequest.get
    userService.adduser(UserModel(formData.name,formData.username,formData.password)).map{
      case Successful => Ok(views.html.signup(RegisterForm.form,Some("User has been registered")))
      case Failed => Ok(views.html.signup(RegisterForm.form,Some("Username is taken")))
    }
  }


  def loginForm(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(LoginForm.form, None))
  }

  def login(): Action[AnyContent] = Action async { implicit request:Request[AnyContent]=>
    val formData: LoginForm = LoginForm.form.bindFromRequest.get
    val result = userService.checkLoginCred(formData.username, formData.password)
    println(result)
    result flatMap  {
      case Successful=>
        sessionService.generateToken(formData.username) map{
          case Some(token) => Redirect(routes.UserController.dashboard).withSession(request.session + ("SessionToken" -> token))
          case None => Ok(views.html.login(LoginForm.form, Some("Some Error Occurred")))
        }
      case Failed => Future(Ok(views.html.login(LoginForm.form, Some("Username or password is incorrect)"))))
    }
  }

  def dashboard(): Action[AnyContent] = Action async { implicit request:Request[AnyContent]=>
    val sessionToken = request.session.get("SessionToken").getOrElse("")
    val user = sessionService.checkSession(sessionToken)
    user.map{userData =>
      userData.map(data =>
        Ok(views.html.dashboard(data))
      )
      .getOrElse(Unauthorized(views.html.defaultpages.unauthorized()))}
  }
  def logout(): Action[AnyContent] = Action { implicit request:Request[AnyContent]=>
    Ok(views.html.logout()).withNewSession
  }




}

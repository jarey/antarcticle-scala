package controllers

import play.api.mvc.{Controller, Action}
import services.{ApplicationPropertiesServiceComponent, UsersServiceComponent, ArticlesServiceComponent}
import security.Authentication
import models.database.UserRecord
import scala.Some
import models.UserModels.UpdateUserRoleModel
import security.Permissions.Manage
import security.Entities.Users
import security.Result.{NotAuthorized, Authorized}

/**
 * Maintains all information from user profile: article list for a particular user,
 * preferences and so on. Authentication and permission management are out of scope,
 * please refer to [[controllers.AuthenticationController]] for further details.
 */
trait UserController {
  this: Controller with ArticlesServiceComponent with UsersServiceComponent with ApplicationPropertiesServiceComponent with PropertiesProvider with Authentication =>

  def viewProfile(userName: String, tags: Option[String] = None) = viewProfilePaged(userName, 1, tags)

  def viewProfilePaged(userName: String, page: Int, tags: Option[String] = None) = Action { implicit request =>
    usersService.getByName(userName) match {
      case user: Some[UserRecord] =>
        articlesService.getPageForUser(page, userName, tags).fold(
          fail => NotFound(views.html.errors.notFound()),
          succ = articles => Ok(views.html.profile(articles, user.get, tags))
        )
      case None =>
        NotFound(views.html.errors.notFound())
    }
  }

  def listUsers(tags: Option[String]) = listUsersPaged(tags)

  def listUsersPaged(search: Option[String], page: Int = 1) = withUser { user => implicit request =>
      if (user.can(Manage, Users)) {
        usersService.getPage(page, search).fold(
          fail => NotFound(views.html.errors.notFound()),
          succ = usersPage => Ok(views.html.userRoles(usersPage, search))
        )
      } else {
        Forbidden(views.html.errors.forbidden())
      }
  }

  def postChangedUserRole(id: Int) = Action(parse.json) {
    implicit request =>
      (request.body \ "role").asOpt[String].fold(BadRequest("")) {
        case role@("admin" | "user") =>
          usersService.updateUserRole(new UpdateUserRoleModel(id, "admin".equals(role))) match {
            case Authorized(result) => Ok("")
            case NotAuthorized() => Unauthorized("")
          }
        case _ => BadRequest("")
      }
  }
}

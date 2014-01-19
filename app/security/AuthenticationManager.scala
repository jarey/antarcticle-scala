package security

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import play.api.libs.ws.WS
import java.security.MessageDigest

private[security] trait AuthenticationManager {
  def authenticate(username: String, password: String): Future[Option[UserInfo]]
}

class FakeAuthenticationManager extends AuthenticationManager {
  def authenticate(username: String, password: String) = {
    future {
      (username, password) match {
        case ("admin", "admin") => UserInfo("admin", "firstName".some, "lastName".some).some
        case _ => none[UserInfo]
      }
    }
  }
}

class PoulpeAuthenticationManager(poulpeUrl: String) extends AuthenticationManager {
  def authenticate(username: String, password: String) = {
    val passwordHash = generateMd5Hash(password)

    for {
      response <- sendRequest(poulpeUrl, username, passwordHash)
      xmlResponseBody = response.xml
    } yield {
      for {
         status <- (xmlResponseBody \\ "status").headOption.map(_.text) if status == "success"
         firstName = (xmlResponseBody \\ "firstName").headOption.map(_.text)
         lastName = (xmlResponseBody \\ "lastName").headOption.map(_.text)
      } yield UserInfo(username, firstName, lastName)
    }
  }

  private def generateMd5Hash(str: String) = {
    val digest = MessageDigest.getInstance("MD5").digest(str.getBytes)
    new java.math.BigInteger(1, digest).toString(16)
  }

  private def sendRequest(poulpeUrl: String, username: String, password: String) = {
    WS.url(s"$poulpeUrl/rest/authenticate")
      .withQueryString(("username", username), ("passwordHash", password))
      .get()
  }
}

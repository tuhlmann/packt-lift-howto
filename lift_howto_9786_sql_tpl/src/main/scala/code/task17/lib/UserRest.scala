package code.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JValue
import code.model.User
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.common.Box
import net.liftweb.util.Helpers._

object UserRest extends RestHelper {

  case class UserData(id: Long, name: String, email: String)

  // This is what it looks like when you provide the full path in the case statement
  serve {
    case "api" :: "user" :: "list" :: Nil JsonGet _ if User.loggedIn_? => anyToJValue(listAllUsers)
    case "api" :: "user" :: "list" :: Nil Get _ if User.loggedIn_? => anyToJValue(listAllUsers)
    case "api" :: "user" :: "list" :: AsLong(id) :: Nil Get _ if User.loggedIn_? => anyToJValue(listUser(id))
  }

  // use this signature if you have multiple functions under the same URI path prefix
  serve ("api2" / "user" prefix {
    case "list" :: Nil Get _ if User.loggedIn_? => anyToJValue(listAllUsers)
    case "list" :: AsLong(id) :: Nil Get _ if User.loggedIn_? => anyToJValue(listUser(id))
  })

  def listAllUsers(): List[UserData] = {
    User.findAll.map{user => UserData(user.id.is, user.shortName, user.email.is)}
  }

  def listUser(id: Long): Box[UserData] = {
    for (user <- User.find(id)) yield {
      UserData(user.id.is, user.shortName, user.email.is)
    }
  }


}
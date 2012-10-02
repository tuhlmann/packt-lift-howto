package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import code.lib.TimeUtils._
import code.model.User
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds

object Selectors {

  def userName =
    "#user-name *" #> User.currentUser.map(_.niceName)

  def addExpenseClass =
    ".expense [class+]" #> "my-extra-class"

  def userLink = {
    ".user-link [href]" #> "/user_mgt/edit" &
    ".user-name" #> User.currentUser.map(_.email.is)
  }

  def ajaxSpice = {
    ".user-action [onclick]" #> SHtml.ajaxInvoke(()=>{
      val firstName = User.currentUser.map(_.firstName.is) openOr "Mister X"
      val text = "Hello %s, welcome to Lift!".format(firstName)
      JsCmds.Alert(text)
    })
  }

}
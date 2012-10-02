package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http.SessionVar
import net.liftweb.http.SHtml._
import code.comet.ChatServer
import net.liftweb.http.js.JsCmds._
import code.comet.Message
import java.util.Date
import code.model.User

object ChatFrontend {

  def render = {
    val userName = User.currentUser.map(_.shortName).openOr("Mister X")

    "#inp_chat" #> onSubmit(s => {
      if (s.trim.nonEmpty) {
        ChatServer ! Message(new Date(), userName, s.trim) // send the message to the comet server
      }
      SetValById("inp_chat", "") // clear the input box
    })
  }
}
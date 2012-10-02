package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import java.util.Date
import net.liftweb.http.RequestVar
import net.liftweb.http.SessionVar

object VarExample {

  object exampleRequestVar extends RequestVar[Int](randomInt(1000))
  object exampleSessionVar extends SessionVar[Int](randomInt(1000))

  def render = {
    ".request-var *" #> exampleRequestVar.is &
    ".session-var *" #> exampleSessionVar.is
  }

}
package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import java.util.Date
import net.liftweb.http.RequestVar
import net.liftweb.http.SessionVar
import scala.xml.NodeSeq
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.http.StatefulSnippet
import net.liftweb.common.Full

class ValidatedForm extends StatefulSnippet  {

  private var animal = ""
  private var legs = "0"

  def dispatch = {case _ => render}

  def render = {

    def process() {
      asInt(legs) match {
        case Full(l) if l < 2 => S.error("legs_msg", "Less then 2 legs, are you serious?")
        case Full(l) =>
          S.notice("Animal: "+animal)
          S.notice("Has Legs: "+legs)
          S.redirectTo("/")
        case _ => S.error("legs_msg", "The value you typed is not a number.")
      }
    }

    "@animal" #> SHtml.text(animal, animal = _) &
    "@legs" #> SHtml.text(legs, legs = _) &
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

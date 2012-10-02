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

object UnLiftifiedForm {

  def render(in: NodeSeq): NodeSeq = {
    for {
      r <- S.request if r.post_? // let's check that we have a POST request
      animal <- S.param("animal") // which animal is it?
      legs <- S.param("legs") // how many legs does it have?
    } {
      // That's the place to do something with the received data, we just put out a notice.
      // and redirect away from the form
      S.notice("Your Animal: "+animal)
      S.notice("Has Legs: "+legs)
      S.redirectTo("/")
    }

    // In case it's not a post request or we don't get the parameters
    // we just return the html
    in
  }
}

object SimpleLiftForm {

  def render = {

    var animal = ""
    var legs = 0

    def process() {
      if (legs < 2) {
        S.error("Less then 2 legs, are you serious?")
      } else {
        S.notice("Animal: "+animal)
        S.notice("Has Legs: "+legs)
        S.redirectTo("/")
      }
    }

    "@animal" #> SHtml.onSubmit(animal = _) &
    "@legs" #> SHtml.onSubmit(s => asInt(s).foreach(legs = _)) &
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}

class NiceLiftForm extends StatefulSnippet  {

  private var animal = ""
  private var legs = 0

  def dispatch = {case "render" => render}

  def render = {

    def process() {
      if (legs < 2) {
        S.error("Less then 2 legs, are you serious?")
      } else {
        S.notice("Animal: "+animal)
        S.notice("Has Legs: "+legs)
        S.redirectTo("/")
      }
    }

    "@animal" #> SHtml.text(animal, animal = _) &
    "@legs" #> SHtml.text(legs.toString, s => asInt(s).foreach(legs = _)) &
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}


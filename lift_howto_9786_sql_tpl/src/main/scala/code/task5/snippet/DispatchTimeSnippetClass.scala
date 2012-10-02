package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import java.util.Date
import scala.xml.NodeSeq
import net.liftweb.http.DispatchSnippet
import code.lib.TimeUtils.timeFormat

class DispatchTimeSnippetClass extends DispatchSnippet {

  override def dispatch = {
    case "time" => render
    case "hour" => anotherMethod
  }

  lazy val number = randomInt(1000)

  def render = {
    println("Number: "+number)
    ".current-time *" #> timeFormat.format(now) &
    ".random-number" #> number
  }

  def anotherMethod = {
    // Import hourFormat in that scope to fight ambiguity
    import code.lib.TimeUtils.hourFormat

    println("Number: "+number)
    ".current-hour *" #> hourFormat.format(now) &
    ".random-number" #> number
  }

}
package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import scala.xml.NodeSeq
import code.lib.TimeUtils._

class TimeSnippetClass {

  lazy val number = randomInt(1000)

  def render = {
    println("Number: "+number)
    ".current-time *" #> timeFormat.format(now) &
    ".random-number" #> number
  }

}
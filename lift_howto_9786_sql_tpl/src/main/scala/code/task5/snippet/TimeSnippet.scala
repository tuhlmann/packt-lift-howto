package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import code.lib.TimeUtils._

object TimeSnippet {

  def render = {
    ".current-time *" #> ("from default method: " + timeFormat.format(now))
  }

  def currentTime = {
    ".current-time *" #> timeFormat.format(now)
  }

}
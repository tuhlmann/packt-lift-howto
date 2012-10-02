package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import java.util.Date
import net.liftweb.http.S
import scala.xml.NodeSeq
import net.liftweb.common.Loggable

object EmbedTemplate extends Loggable {

  /**
   * Embeds another template into the current template
   */
  def render = {
    // which template should we embed
    (for (template <- S.attr("what")) yield {
      logger.info("Embed "+template)
      val path = "templates-hidden" :: template.split("/").toList.filterNot(_.isEmpty)
      logger.info("Path Elements: "+path.mkString(" , "))
      val html = S.runTemplate(path) openOr NodeSeq.Empty

      // Now let's make a small change to the template. Unhide a paragraph from the awesome.html file
      val cssSel =
        ".programmatic-access [style]" #> ""

      cssSel(html)

    }) openOr {
      // No template given, return empty
      logger.info("No Snippet found, return empty")
      NodeSeq.Empty
    }
  }


}
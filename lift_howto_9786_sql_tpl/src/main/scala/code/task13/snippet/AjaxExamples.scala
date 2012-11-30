package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http.RequestVar
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.PassThru
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js._
import scala.xml.Text
import net.liftweb.http.js.jquery.JqJsCmds._

object AjaxExamples {

  def example1 = {
    "#ajax_example1" #> ajaxButton("Click Me", ()=>Alert("You clicked me"), "class" -> "positive")
  }

  def example2 = {

    var name = ""
    var city = ""

    def process(): JsCmd = {
      val id = nextFuncName
      val result = "%s lives in %s".format(name, city)
      val html = <li id={id} style="display:none">{result}</li>
      AppendHtml("ajax_example2_list", html) & FadeIn(id, 0 second, 1 second)
    }

    "@name" #> ajaxText(name, s => {name = s; SetHtml("ajax_example2_name", Text(name))}) &
    "@city" #> (text(city, city = _) ++ hidden(process))
  }

}

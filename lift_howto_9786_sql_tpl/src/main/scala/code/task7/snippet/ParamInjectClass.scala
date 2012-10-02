package code.snippet

import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat
import code.lib.TimeUtils._
import code.model.User
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.http.RequestVar
import java.util.Date
import net.liftweb.util.ClearClearable
import bootstrap.liftweb.DocumentInfo

class ParamInjectClass(docInfo: DocumentInfo) {

  def render() = "*" #> docInfo.docId


}
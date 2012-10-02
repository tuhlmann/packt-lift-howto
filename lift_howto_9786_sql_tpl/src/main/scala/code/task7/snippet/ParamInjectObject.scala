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
import net.liftweb.sitemap.Menu
import bootstrap.liftweb.DocumentInfo
import net.liftweb.common.Full
import net.liftweb.sitemap.Loc.If
import net.liftweb.http.RedirectResponse
import bootstrap.liftweb.BootHelpers
import net.liftweb.sitemap.Loc.Hidden

object ParamInjectObject {

  // because this is a snippet object we have to access the param directly
  lazy val menu = Menu.param[DocumentInfo]("ShowDoc", "Show Document", s => Full(DocumentInfo(s)), dinfo => dinfo.docId) / "examples" / "show" >>
                  BootHelpers.loggedIn >> Hidden
  lazy val loc = menu.toLoc

  def render = "*" #> loc.currentValue.map(_.docId)

}
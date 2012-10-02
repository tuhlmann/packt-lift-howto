package code
package snippet

import lib.AppHelpers
import scala.xml.NodeSeq
import net.liftweb._
import common._
import http.{LiftRules, S}
import sitemap.SiteMap
import util.Helpers._
import net.liftweb.sitemap.Loc
import code.config.LocIcon
import scala.xml.Text

object GroupMenu extends AppHelpers {
  def render(in: NodeSeq): NodeSeq = {
    for {
      group <- S.attr("group") ?~ "Group not specified"
      sitemap <- LiftRules.siteMap ?~ "Sitemap is empty"
      request <- S.request ?~ "Request is empty"
      curLoc <- request.location ?~ "Current location is empty"
    } yield ({
      val currentClass = S.attr("current_class").openOr("current")
      sitemap.locForGroup(group) flatMap { loc =>
        if (curLoc.name == loc.name) {
          <li class={currentClass}>{buildLink(loc)}</li>
        } else {
          <li>{buildLink(loc)}</li>
        }
      }
    }): NodeSeq
  }

  def buildLink(loc: Loc[_]): NodeSeq = {
    val linkText = loc.linkText openOr Text(loc.name)
    val locIcons = findIcons(loc)
    val iconSpan = if (locIcons.size > 0) {
      <i class={locIcons.flatMap(_.cssIconClass).mkString(" ")}></i>
    } else NodeSeq.Empty
    <a href={loc.createDefaultLink}>{iconSpan}<span>&nbsp;{linkText}</span></a>
  }

  def findIcons(loc: Loc[_]): List[LocIcon] = loc.params.filter(p => p.isInstanceOf[LocIcon]).map(_.asInstanceOf[LocIcon])

}
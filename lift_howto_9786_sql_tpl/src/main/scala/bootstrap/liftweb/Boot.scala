package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import mapper._
import code.model._
import scala.xml.Text
import scala.xml.NodeSeq
import code.snippet.ParamInjectObject
import code.snippet.PostingUser
import code.snippet.MainUserPostsPage
import code.snippet.AUserPost
import code.lib.UserRest

case class DocumentInfo(docId: String)

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {

  def boot {

    // Set up a database connection
    // We check first if the connection information is available over JNDI,
    // if not we read the settings from a property file (default.props depending on the RunMode)
    // or, if not settings are found we fall back to the H2 database
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
                   Props.get("db.url") openOr "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
                   Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Mapper is Lift's own persistence framework targeted towards relational databases
    // You don't have to use Mapper, you can use JPA or something different.
    // But Mapper provides a nice way to automatically detect changes in you
    // model classes and can transform the appropriate actions in the database model
    Schemifier.schemify(true, Schemifier.infoF _, User, UserPost)

    // Add the top level packages here that contain the model, snippet, lib or comet packages
    LiftRules.addToPackages("code")

    import BootHelpers._

    // Build SiteMap
    def sitemap: SiteMap = SiteMap(
      Menu.i("Home") / "index" >> User.AddUserMenusAfter >> LocGroup("main"), // the simple way to declare a menu

      Menu.i("4 - Templates") / "examples" / "task4" / "templates" >> LocGroup("examples") >> Title(i=>Text("Templates Task")) >> loggedIn,
      Menu.i("5 - Snippets") / "examples" / "task5" / "snippets"  >> LocGroup("examples") >> Title(i=>Text("Snippets Task")) >> loggedIn,
      Menu.i("6 - Selectors") / "examples" / "task6" / "selectors"  >> LocGroup("examples") >> loggedIn,
      Menu.i("7 - Dynamic Content") / "examples" / "task7" / "dynamic"  >> LocGroup("examples") >> loggedIn,

      Menu("Static", "Static Content") / "static" / ** >> LocGroup("main") >> loggedIn,

      ParamInjectObject.menu,
      Menu.param[DocumentInfo]("ShowDoc2", "Show Document", s => Full(DocumentInfo(s)), dinfo => dinfo.docId) / "examples" / "task7" / "show2" >> LocGroup("main") >> loggedIn >> Hidden,

      MainUserPostsPage.menu >> loggedIn,
      User.listUsersMenu >> loggedIn,
      AUserPost.menu >> loggedIn,

      Menu.i("11 - Old Fashioned Form") / "examples" / "task11" / "form1"  >> LocGroup("examples") >> loggedIn,
      Menu.i("11 - Simple Lift Form") / "examples" / "task11" / "form2"  >> LocGroup("examples") >> loggedIn,
      Menu.i("11 - Nice Lift Form") / "examples" / "task11" / "form3"  >> LocGroup("examples") >> loggedIn,
      Menu.i("12 - Validated Form") / "examples" / "task12" / "validated-form"  >> LocGroup("examples") >> loggedIn,

      Menu.i("13 - Ajax") / "examples" / "task13" / "ajax"  >> LocGroup("examples") >> loggedIn,
      Menu.i("14 - Comet Chat") / "examples" / "task14" / "comet"  >> LocGroup("examples") >> loggedIn,

      Menu.i("17 - REST Example") / "examples" / "task17" / "rest"  >> LocGroup("examples") >> loggedIn

    )

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)


    // REST Example
    // We use the stateful approach, the rest calls are associated with a servlet container session
    LiftRules.dispatch.append(UserRest)
    // To use the API without a session being created, use:
    // LiftRules.statelessDispatchTable.append(UserRest)


  }
}

object BootHelpers {

  // A Loc function making sure only logged in users see the menu item
  val loggedIn = If(() => User.loggedIn_?, () => RedirectResponse("/user_mgt/login"))

}



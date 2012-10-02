package code.snippet

import net.liftweb.sitemap.Menu
import net.liftweb.util.Helpers._
import net.liftweb.common._
import code.model.User
import net.liftweb.sitemap._
import scala.xml.Text
import net.liftweb.sitemap.Loc.LocGroup
import code.model.UserPost
import net.liftweb.mapper.By
import net.liftweb.http.SHtml._
import net.liftweb.http.S

object MainUserPostsPage {

  lazy val menu = Menu.i("9 - Dynamic Menus (User List)") / "examples" / "task9" / "users" >> LocGroup("examples")

  def render = "li *" #> User.findAll.zipWithIndex.map{ case (user, idx) =>
    "a *+" #> (idx + ": " + user.shortName) &
    "a [href]" #> User.listUsersMenu.calcHref(user)
  }

}

class PostingUser(user: User) {

  def listUsers = {
    ".back-to-users [href]" #> MainUserPostsPage.menu.loc.calcDefaultHref &
    "li *" #> UserPost.findAll(By(UserPost.userId, user.id.is)).map(post =>
              "a *+" #> post.title & "a [href]" #> AUserPost.menu.calcHref(user -> post))
  }

  def postForm = {
    var title = ""
    var content = ""

    def process() = {
      if (title.nonEmpty && content.nonEmpty) {
        println("Saving: %s = %s".format(title, content))
        UserPost.create.title(title).contents(content).userId(user.id.is).save
        S.notice("Post saved.")
      } else {
        S.error("Post empty, not saved.")
      }
    }

    (if (itsMyUser(user)) {
      "@post-title" #> text(title, title = _) &
      "@post-text" #> textarea(content, content = _)
    } else {
      ".post-area" #> ""
    }) &
    "@submit" #> onSubmitUnit(()=>process)
  }

  def itsMyUser(user: User) = User.currentUser.map(_.id.is == user.id.is).openOr(false)

}



object AUserPost {
  lazy val menu = Menu.params[(User, UserPost)]("AUserPost", Loc.LinkText(tpl => Text("Post: "+tpl._2.title)),
                  {
                    case User(u) :: UserPost(up) :: Nil => Full(u -> up)
                    case _ => Empty
                  },
                  (tpl: (User, UserPost)) => tpl._1.id.is.toString :: tpl._2.id.is.toString :: Nil) / "examples" / "task9" / "users" / * / "posts" / * >> LocGroup("PostingUsers")
}

class AUserPost(p: (User, UserPost)) {
  val (user, post) = p

  def render =
    ".back-to-posts [href]" #> User.listUsersMenu.calcHref(user) &
    "@user *" #> user.shortName &
    "@title *" #> post.title.is &
    "@content *" #> post.contents.is

}


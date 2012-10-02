package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.common._
import com.foursquare.rogue.Rogue._
import code.model.User
import scala.xml.NodeSeq
import code.model.Note
import java.util.regex.Pattern
import scala.collection.mutable.ListBuffer
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.S

object RogueExamples {

  // Snippet functions

  def allUsers = "#rogue-ex1-body *" #> forAllUsers3

  def dotComUsers = "#rogue-ex2-body *" #> findAllComEmail.map{ case (name, email) =>
      <tr><td>{name}</td><td>{email}</td></tr>
    }

  def notesLast24h = "*" #> countNotesInLast24h


  def notesByUser = {
    "@email-address" #> ajaxText("", e => {
      if (e.trim.nonEmpty) {
        val html = findNotesOfUser(e).flatMap(mapToRow)
        SetHtml("rogue-ex3-body", html)
      } else Noop
    })
  }

  // Helper functions that access the database

  /**
   * Do something for all users.
   * The following code could be done more functional with some other method,
   * but we wanted to showcase this one :)
   */
  def forAllUsers1: NodeSeq = {
    val rows = ListBuffer[NodeSeq]()
    User.foreach{ user =>
      println("Do something with "+user.shortName)
      rows += mapToRow(user)
    }
    rows.flatten
  }

  /**
   * That does the same as the code above but is more functional and easier to read.
   */
  def forAllUsers2: NodeSeq = (User fetch).flatMap(user => mapToRow(user))

  /**
   * The same as above, except even a bit more terse. Scala recognizes when the argument of the closure
   * can be passed into the method you call (mapToRow in this case) and pass it in automatically
   * without you having to specify that.
   */
  def forAllUsers3: NodeSeq = (User fetch).flatMap(mapToRow)


  /**
   * Find a user by email address and return the table row markup
   * for him.
   */
  def findUserByEmail(email: String): Box[User] = User where (_.email eqs email) get()

  /**
   * Select all email addresses that end with .com and return only the name and the email address
   */
  def findAllComEmail(): List[(String, String)] = {
    val pattern = """.*\.com""".r
    User where (_.email matches pattern) select(_.firstName, _.email) fetch()
  }

  /**
   * Count all users with more notes than limit
   * @param limit Number of notes, Users with more or equal notes are counted
   */
  def countUserOnNotes(limit: Int): List[User] = {
    Nil
  }

  def findNotesOfUser(user: User): List[Note] = {
    // alternative 1
    val alt1 = user.notes.objs

    // alternative 2
    val alt2 = (Note where (_.userId eqs user.id) fetch)

    alt2
  }

  def countNotesInLast24h = Note where (_.date after 24.hours.ago) count

  /**
   * An alternative way to do all in one
   */
  def findNotesOfUser(email: String): List[Note] = {
    val user: Box[User] = (User where (_.email eqs email) get())
    user.map(u => (Note where (_.userId eqs u.id) fetch())).openOr(Nil)
  }


  def mapToRow(user: User): NodeSeq = {
    <tr><td>{user.shortName}</td><td>{user.email.is}</td></tr>
  }

  /**
   * Map the not into a html row for presentation. We could also have this inside Note
   * or create a presenter wrapper object that takes care of the conversions:
   * http://debasishg.blogspot.de/2010/06/scala-implicits-type-classes-here-i.html
   */
  def mapToRow(note: Note): NodeSeq = {
    <tr><td>{note.date.is.getTime.toLocaleString}</td><td>{note.note.is}</td></tr>
  }


}
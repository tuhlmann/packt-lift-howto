package code.model

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.record.field.TextareaField
import scala.xml.Text
import net.liftweb.util.Helpers._
import com.foursquare.rogue.Rogue._
import net.liftweb.mongodb.record.MongoMetaRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.json.JsonDSL._
import code.lib.MetaMegaProtoUser
import code.lib.MegaProtoUser
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.mongodb.record.field.ObjectIdRefListField
import java.util.Date
import org.joda.time.DateTime
import java.util.Calendar

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with  MetaMegaProtoUser[User] with MongoMetaRecord[User] {

  override def screenWrap = Full(<lift:surround with="default" at="content"><lift:bind /></lift:surround>)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(firstName, lastName, email, locale, timezone, password)

  // comment this line out to require email validations
  override def skipEmailValidation = true

  override def globalUserLocParams = LocGroup("main") :: super.globalUserLocParams

  /**
   * This is called when the user is saved. Before saving it we create a bunch
   * of notes and attach them to the user.
   */
  override def actionsAfterSignup(theUser: TheUserType, func: () => Nothing): Nothing = {
    println("Create Notes")
    for (i <- 1 to (randomInt(10)+1)) {
      val note = Note.createRecord.userId(theUser.id).date(Calendar.getInstance).note("This is dummy note number "+i).save
      theUser.notes.atomicUpdate(note.id.is :: _)
    }
    theUser.save(true)
    super.actionsAfterSignup(theUser, func)
  }


}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User private() extends MegaProtoUser[User] with MongoRecord[User] {

  def meta = User

  // define an additional field for a personal essay
  object textArea extends TextareaField(this, 2048) {
    override def textareaRows  = 10
    override def textareaCols = 50
    override def displayName = "Personal Essay"
  }

  /*
   * For the Rogue example.
   * We will create a few random notes when the user gets created and use that sample
   * data for query examples.
   */
  object notes extends ObjectIdRefListField(this, Note)

}


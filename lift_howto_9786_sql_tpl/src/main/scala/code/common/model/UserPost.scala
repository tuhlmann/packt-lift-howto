package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.sitemap._
import scala.xml.Text

object UserPost extends UserPost with LongKeyedMetaMapper[UserPost]  {
}

class UserPost extends LongKeyedMapper[UserPost] {

  def getSingleton = UserPost // what's the "meta" server

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object userId extends MappedLongForeignKey(this, User)
  object title extends MappedString(this, 140)
  object contents extends MappedText(this)


}


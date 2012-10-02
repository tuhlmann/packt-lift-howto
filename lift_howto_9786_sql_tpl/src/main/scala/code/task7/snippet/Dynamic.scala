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

object Dynamic {

  object lineCounter extends RequestVar[Int](0)

  def renderList =  "#record-line" #> List("one ", "two ", "three ")

  def renderEmptyList =  "#record-line" #> Nil

  def renderListToChildren = "#record-line *" #> List("one ", "two ", "three ")

  def renderTableOneRow = {
    ".expense-row *" #> ( ".date *" #> getExpenseDate &
                          ".desc *" #> getExpenseDesc
                        ) &
    ClearClearable
  }

  def renderTableTwoRows = {
    ".expense-row *" #> List(( ".date *" #> getExpenseDate &
                               ".desc *" #> getExpenseDesc),
                             ( ".date *" #> getExpenseDate &
                               ".desc *" #> getExpenseDesc
                            ))
  }

  case class Expense(date: Date, desc: String)

  def records = Expense(getRandomDate(100), getExpenseDesc) :: Expense(getRandomDate(100), getExpenseDesc) ::
                Expense(getRandomDate(100), getExpenseDesc) :: Nil

  def renderTableWithMap = {

    ".expense-row *" #> records.map { record =>

      ".date *" #> getExpenseDate(record.date) &
      ".desc *" #> record.desc

    }
  }

  def renderTableWithMapConditionally = {
    val itsRecords = records
    if (itsRecords.size > 0) {
      ".expense-row *" #> records.map { record =>
        ".date *" #> getExpenseDate(record.date) &
        ".desc *" #> record.desc
      }
    } else {
      ".expense-row *" #> <td></td><td>No records found</td>
    }
  }



  def getExpenseDate: String = getExpenseDate(getRandomDate(100))
  def getExpenseDate(date: Date): String = dateFormat.format(date)

  def getRandomDate(span: Int): Date = randomInt(span).days.ago.noTime

  def getExpenseDesc = {
    lineCounter(lineCounter.is + 1)
    "Description Line " + lineCounter.is
  }

}
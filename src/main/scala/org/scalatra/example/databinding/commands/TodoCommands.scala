package org.scalatra.example.commands.commandsupport

// the model code from this application
import org.scalatra.example.commands.models._

// the Scalatra command handlers
import org.scalatra.commands._
import org.scalatra.validation.Validators._

// Scalatra's JSON-handling code
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
import scalaz._
import Scalaz._

/** Set up an abstract class to inherit from, so we don't need to keep on
 *  repeating the `extends ModelCommand[T]` in every command we make.
 */
abstract class TodosCommand[S](implicit mf: Manifest[S]) extends ModelCommand[S] with JsonCommand {
  
}

/** A command to validate and create Todo objects. */
class CreateTodoCommand extends TodosCommand[Todo] { 

  val startsWithCap: Validator[String] = { v: String => 
    if("""^[A-Z,0-9]""".r.findFirstIn(v).isDefined) v.successNel else "%s must start with a capital letter".failNel
  }

  // add json format handling so the command can do automatic conversions.
  protected implicit val jsonFormats = DefaultFormats

  // the validation conditions on the name field.
  val name: Field[String] = asType[String]("name").validateWith(List(nonEmptyString(),minLength(3),startsWithCap))
  
}
  

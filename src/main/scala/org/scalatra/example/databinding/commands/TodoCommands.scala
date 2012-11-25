package org.scalatra.example.databinding.commands

// the model code from this application
import org.scalatra.example.databinding.models._

// the Scalatra databinding handlers
import org.scalatra.databinding._

// JSON-handling code
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}

class TodosBinding[T](b: FieldDescriptor[T]) {

  def startsWithCap(message: String = "%s must start with a capital letter.") = b.validateWith(_ => 
    _ flatMap { Validators.startsWithCap(b.name, message).validate(_) }
  )
}

/** Set up an abstract class to inherit from, so we don't need to keep on
 *  repeating the `extends ModelCommand[T]` in every command we make.
 */
abstract class TodosCommand[S](implicit mf: Manifest[S]) extends ModelCommand[S] with JsonCommand {
  implicit def todoValidators[T](b: FieldDescriptor[T]) = new TodosBinding[T](b)
}

/** A command to validate and create Todo objects. */
class CreateTodoCommand extends TodosCommand[Todo] { 

  // add json format handling so the command can do automatic conversions.
  protected implicit val jsonFormats = DefaultFormats

  val name: Field[String] = asType[String]("name").notBlank.minLength(3).startsWithCap

}
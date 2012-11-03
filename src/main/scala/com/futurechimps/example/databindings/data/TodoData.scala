package com.futurechimps.example.databindings.data

import com.futurechimps.example.databindings.models._
import com.futurechimps.example.databindings.commands._
import com.futurechimps.example.databindings.utils.Logging
import scalaz._
import Scalaz._
import scala.util.control.Exception._
import org.scalatra.validation._
import java.util.concurrent.atomic.AtomicInteger

/* 
 * CommandHandler, in a larger app, might be in a service layer. 
 * Since we've just got a small app, we'll bring it into our fake little
 * data layer, here:
 */
object TodoData extends Logging with CommandHandler {

  val idCounter = new AtomicInteger(3)

  /**
   * Some fake todos data so we can simulate retrievals.
   */
  var all = List(
      Todo(1, "Shampoo the cat"),
      Todo(2, "Wax the floor"),
      Todo(3, "Scrub the rug"))

  /**
   * Checks what kind of command is coming in the door and handles whatever
   * work the Command should do when executed.
   * 
   * By the time you get into the cases, you can start handling the work
   * you want the command to do. When it gets to that point, it's already 
   * successfully validated.
   */
  def handle: Handler  = {
    case c: CreateTodoCommand => 
      // handle the command, when it gets here it's been validated etc.
      // so most likely you want to persist the fields of the command somehow
      add(newTodo(~c.name.value))
      // I want to do something like TodoData.add(todo) here, but I can't
      // because the type system wants this case to spit out a
      // com.futurechimps.example.databindings.commands.models.package.ModelValidation[?>: Nothing <: Any]
  }
  
  private def newTodo(name: String) = Todo(idCounter.incrementAndGet, name)
  
  /**
   * Adds a new Todo object to the existing list of todos.
   * 
   * The method returns a ModelValidation[Todo], which is carried around in the
   * todo.successNel. Think of "successNel" as being like a two part variable 
   * name. The result is either 
   * Success[Model] or Failure[NonEmptyList[ValdationError]]. So you're getting
   * back either "success" OR a non-empty list ("Nel"). This type signature is
   * in turn dictated by the return value needed by the `handle` method, above.
   * 
   * If any exceptions happen as we're doing work here, the errorFail method 
   * will be called, due to the allCatch.withApply (which is equivalent to a
   * try {} catch {} block. 
   */
  private def add(todo: Todo): ModelValidation[Todo] = {
    allCatch.withApply(errorFail) {
      all ::= todo
      todo.successNel
    }
  }

  def errorFail(ex: Throwable) = ValidationError(ex.getMessage, UnknownError).failNel
}

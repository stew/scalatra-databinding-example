package org.scalatra.example.databinding.data

import org.scalatra.example.databinding.models._
import org.scalatra.example.databinding.commands._
import org.scalatra.example.databinding.utils.Logging
import scalaz._
import Scalaz._
import scala.util.control.Exception._
import org.scalatra.validation._
import org.scalatra.databinding._
import java.util.concurrent.atomic.AtomicInteger

/** A fake datastore which keeps Todo objects in RAM.
 *
 * CommandHandler is a built-in part of Scalatra which gives you a generic
 * chunk of logic for command execution. You can write your own 
 * CommandHandler base class if you want to, and this might be quite
 * useful in a larger application.
 * 
 * Logging is just here to satisfy the compiler. 
 */
object TodoData extends Logging with CommandHandler {

  /** A counter variable to fake out auto-incrementing keys for us **/
  val idCounter = new AtomicInteger(3)

  /** Some fake todos data so we can simulate retrievals. */
  var all = List(
      Todo(1, "Shampoo the cat", true),
      Todo(2, "Wax the floor"),
      Todo(3, "Scrub the rug"))

  /** Returns the number of Todos which are not yet Todone. **/
  def remaining = {
    all.filterNot(_.done == true).length
  }

  /** Handles execution of Command requests.
   *
   * Checks what kind of command is coming in the door and handles whatever
   * work the Command should do when executed.
   * 
   * By the time you get into the cases, you can start handling the work
   * you want the command to do. When it gets to that point, it's already 
   * successfully validated.
   */
  def handle: Handler  = {
    case c: CreateTodoCommand => 
      add(newTodo(~c.name.value))
  }
  
  private def newTodo(name: String) = Todo(idCounter.incrementAndGet, name)
  
  /** Adds a new Todo object to the existing list of todos.
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
      all = all.sort((e1, e2) => (e1.id < e2.id))
      todo.successNel
    }
  }

  /** Throw a validation error if something explodes when adding a Todo **/
  def errorFail(ex: Throwable) = ValidationError(ex.getMessage, UnknownError).failNel
}

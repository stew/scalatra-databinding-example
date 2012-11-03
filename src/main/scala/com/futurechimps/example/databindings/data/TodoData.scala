package com.futurechimps.example.databindings.data

import com.futurechimps.example.databindings.models._
import com.futurechimps.example.databindings.commands._
import com.futurechimps.example.databindings.utils.Logging
import scalaz._
import Scalaz._
import scala.util.control.Exception._
import org.scalatra.validation._

/* 
 * CommandHandler, in a larger app, might be in a service layer. 
 * Since we've just got a small app, we'll bring it into our fake little
 * data layer, here:
 */
object TodoData extends Logging with CommandHandler {

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
   */
  def handle: Handler  = {
    case c: CreateTodoCommand => 
      // handle the command, when it gets here it's been validated etc.
      // so most likely you want to persist the fields of the command somehow
      add(Todo(4, ~c.name.value))
  }
  
  
  /**
   * Adds a new Todo object to the existing list of todos.
   * 
   * If the validation doesn't apply, the errorFail method will be called, 
   * and the lines inside the allCatch block won't run.
   *  
   * The method returns a ModelValidation[Todo], which is carried around in the
   * todo.successNel. Think of "successNel" as being like a two part variable 
   * name. The result is either 
   * Success[Model] or Failure[NonEmptyList[ValdationError]]. So you're getting
   * back either "success" OR a non-empty list ("Nel").
   */
  def add(todo: Todo): ModelValidation[Todo] = {
    allCatch.withApply(errorFail) {
      all ::= todo
      todo.successNel
    }
  }

  def errorFail(ex: Throwable) = ValidationError(ex.getMessage, UnknownError).failNel
}

package com.futurechimps.example.databindings
package commands

import org.scalatra.databinding._
import com.futurechimps.example.databindings.utils.Logging
import scala.util.control.Exception.allCatch
import commands._
import models._
import scalaz._
import Scalaz._
import org.scalatra.validation.{ ValidationError, UnknownError, NotImplemented }

// A bit of type wizardry to make the compiler happy.
abstract class TodosCommand[S](implicit mf: Manifest[S]) extends ParamsOnlyCommand


// This will probably end up getting pushed down into the framework, as it
// should be relatively generic.
trait CommandHandler { self: Logging ⇒

  /** Performs validation and executes the command request. 
   *
   * Calls validation using cmd.isValid and executes the command using
   * the protected handle.lif(cmd) method.
   */
  def execute[S: Manifest](cmd: TodosCommand[S]): ModelValidation[S] = {
    logger.debug("Executing [%s].\n%s" format (cmd.getClass.getName, cmd))
    if (cmd.isValid) {
      val res = (allCatch withApply (serverError(cmd.getClass.getName, _))) {
        handle.lift(cmd).map(_.map(_.asInstanceOf[S])) | ValidationError("Don't know how to handle: " + cmd.getClass.getName, UnknownError).failNel
      }
      val ftext = "with %d failures\n%s".format(~res.fail.toOption.map(_.list.size), ~res.fail.toOption.map(_.list))
      logger.debug("Command [%s] executed %s." format (cmd.getClass.getName, res.isSuccess ? "successfully." | ftext))
      res
    } else {
      val f = cmd.errors.map(_.validation) collect {
        case Failure(e) ⇒ e
      }
      logger.debug("Command [%s] executed with %d failures.\n%s" format (cmd.getClass.getName, f.size, f.toList))
      nel(f.head, f.tail: _*).fail
    }
  }

  private[this] def serverError[R](cmdName: String, ex: Throwable): ModelValidation[R] = {
    logger.error("There was an error while executing " + cmdName, ex)
    ValidationError("An error occurred while handling: " + cmdName, UnknownError).failNel[R]
  }

  // TODO: it's unclear to me what this partial function is really doing. 
  type Handler = PartialFunction[TodosCommand[_], ModelValidation[_]]


  /** Does the actual work part of the command (as opposed to validation).
   *
   * This protected method, when called in a Command subclass, 
   * should do the actual work that you want done when you call execute().
   *
   * This method can be implemented as a protected val if you don't want 
   * to create a new instance of that partial function every time. 
   */
  protected def handle: Handler
}
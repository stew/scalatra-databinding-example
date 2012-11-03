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

package object models {
  type ModelValidation[T] = Validation[NonEmptyList[ValidationError], T]
}

trait CommandHandler { self: Logging ⇒
  def execute[S: Manifest](cmd: ParamsOnlyCommand[S]): ModelValidation[S] = {
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

  type Handler = PartialFunction[ParamsOnlyCommand[_], ModelValidation[_]]

  protected def handle: Handler
}
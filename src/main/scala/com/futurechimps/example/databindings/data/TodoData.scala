package com.futurechimps.example.databindings.data

import com.futurechimps.example.databindings.models._
import com.futurechimps.example.databindings.commands._
import com.futurechimps.example.databindings.utils.Logging
import scalaz._
import Scalaz._

/* 
 * CommandHandler, in a larger app, might be in a service layer. 
 * Since we've just got a small app, we'll bring it into our fake little
 * data layer, here:
 */
object TodoData extends Logging with CommandHandler {

  /**
   * Some fake flowers data so we can simulate retrievals.
   */
  var all = List(
      Todo(1, "Shampoo the cat"),
      Todo(2, "Wax the floor"),
      Todo(3, "Scrub the rug"))

  def handle: Handler  = {
    case c: CreateTodoCommand => 
      // handle the command, when it gets here it's been validated etc.
      // so most likely you want to persist the fields of the command somehow
      Todo(4, ~c.name.value).successNel
  }
}

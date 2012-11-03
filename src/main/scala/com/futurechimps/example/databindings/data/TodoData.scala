package com.futurechimps.example.databindings.data

import com.futurechimps.example.databindings.models._

/* 
 * CommandHandler, in a larger app, might be in a service layer. 
 * Since we've just got a small app, we'll bring it into our fake little
 * data layer, here:
 */
object TodoData extends CommandHandler {

  /**
   * Some fake flowers data so we can simulate retrievals.
   */
  var all = List(
      Todo(1, "Shampoo the cat"),
      Todo(2, "Wax the floor"),
      Todo(3, "Scrub the rug"))
}

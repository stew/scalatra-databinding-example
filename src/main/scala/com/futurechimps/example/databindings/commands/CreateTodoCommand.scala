package com.futurechimps.example.databindings.commands

import org.scalatra.databinding._
import com.futurechimps.example.databindings.models._

class CreateTodoCommand(name: String) extends ParamsOnlyCommand {

  def execute = {
    // Something amazing will happen here, but how we turn the params
    // into an actual Todo is unclear to me.
    // Fake it for now:

    val todo = new Todo(4, name)

    // Once we have one, though, I guess we could just push it onto the 
    // end of the TodoData.all list:
    // TodoData.add(todo)

    // Then we can just return our new Todo, I suppose
    todo
  }

}
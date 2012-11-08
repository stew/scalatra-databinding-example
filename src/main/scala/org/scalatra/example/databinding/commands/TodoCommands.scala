package org.scalatra.example.databinding.commands

import org.scalatra.databinding._
import org.scalatra.example.databinding.models._


/** Set up an abstract class to inherit from, so we don't need to keep on
 *  repeating the `extends ModelCommand[T] with ParamsOnlyCommand` in 
 *  every command we make.
 */
abstract class TodosCommand[S](implicit mf: Manifest[S]) extends ModelCommand[S] with ParamsOnlyCommand

/** A command to validate and create Todo objects. */
class CreateTodoCommand extends TodosCommand[Todo] { 
  val name: Field[String] = asType[String]("name").notBlank.minLength(3) 
}
package org.scalatra.example.databinding.commands

import org.scalatra.databinding._
import org.scalatra.example.databinding.models._

/** A command to validate and create Todo objects. */
class CreateTodoCommand extends TodosCommand[Todo] { 
  val name: Field[String] = asType[String]("name").notBlank.minLength(3) 
}
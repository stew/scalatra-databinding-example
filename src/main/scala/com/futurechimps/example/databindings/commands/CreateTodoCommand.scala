package com.futurechimps.example.databindings.commands

import org.scalatra.databinding._
import com.futurechimps.example.databindings.models._

/** A command to validate and create Todo objects. */
class CreateTodoCommand extends TodosCommand[Todo] { 
  val name: Field[String] = asType[String]("name").notBlank.minLength(3) 
}
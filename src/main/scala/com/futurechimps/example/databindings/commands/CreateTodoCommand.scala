package com.futurechimps.example.databindings.commands

import org.scalatra.databinding._
import com.futurechimps.example.databindings.models._

class CreateTodoCommand extends TodosCommand[Todo] { 
  val name: Field[String] = asType[String]("name").notBlank.minLength(3) 
}
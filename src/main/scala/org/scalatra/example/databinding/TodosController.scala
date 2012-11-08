package org.scalatra.example.databinding

import org.scalatra._
import scalate.ScalateSupport

// Json handling
import json._
import org.json4s.{DefaultFormats, Formats}

// Command support
import databinding._

// Our models
import models._

// Data
import data._

// Commands
import commands._

// Logger
import utils._

class TodosController extends ScalatraServlet with ScalateSupport 
  with JacksonJsonParsing with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  get("/") {
    contentType="text/html"
    ssp("/todos/index", "todos" -> TodoData.all, "remaining" -> TodoData.remaining)
  }

  post("/todos") {
    val cmd = command[CreateTodoCommand]
    TodoData.execute(cmd).fold(
      errors => halt(400, errors), // probably better serialize somehow
      todo => redirect("/")
    )
  }

  get("/todos/:id") {
    TodoData.all find (_.id == params("id").toInt) match {
      case Some(todo) => todo
      case None => halt(404)
    }
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}

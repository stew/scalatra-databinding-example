package com.futurechimps.example.databindings

import org.scalatra._

// Json handling
import json._
import org.json4s.{DefaultFormats, Formats}

// Command support
import databinding._
import scalate.ScalateSupport

// Our models
import models._

// Data
import data._

// Commands
import commands._

class TodosController extends ScalatraServlet with ScalateSupport 
  with CommandSupport with JacksonJsonParsing with JacksonJsonSupport with JValueResult {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats


  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  get("/") {
    contentType = "text/html"

    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  post("/todos") {
    val cmd = commandOrElse(new CreateTodoCommand(params("name")))
    if(cmd.isValid) {
      val todo = cmd.execute
      redirect("todos/" + todo.id)
    } else {

    }
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

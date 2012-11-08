package org.scalatra.example.databinding
package commands

import org.scalatra.databinding._
import org.scalatra.example.databinding.utils.Logging
import scala.util.control.Exception.allCatch
import commands._
import models._
import scalaz._
import Scalaz._
import org.scalatra.validation.{ ValidationError, UnknownError, NotImplemented }

// A bit of type wizardry to make the compiler happy.
abstract class TodosCommand[S](implicit mf: Manifest[S]) extends ModelCommand[S] with ParamsOnlyCommand


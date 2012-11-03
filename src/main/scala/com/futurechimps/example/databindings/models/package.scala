package com.futurechimps.example.databindings

import scalaz._
import Scalaz._
import org.scalatra.databinding._
import org.scalatra.validation.ValidationError


package object models {
  type ModelValidation[T] = Validation[NonEmptyList[ValidationError], T]
}

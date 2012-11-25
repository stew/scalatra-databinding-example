package org.scalatra.example.databinding.models

import org.scalatra.validation.Validators.{PredicateValidator, Validator}

object Validators {
  def startsWithCap(fieldName: String, message: String = "%s must start with a capital letter."): Validator[String] =
    new PredicateValidator[String](fieldName, """^[A-Z,0-9]""".r.findFirstIn(_).isDefined, message)
}

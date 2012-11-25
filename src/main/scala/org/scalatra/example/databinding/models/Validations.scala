package org.scalatra.example.databinding.models

import org.scalatra.validation.Validators.{PredicateValidator, Validator}

object Validators {
  def startsWithCap(fieldName: String, message: String = "%s must start with a capital letter."): Validator[String] =
    new PredicateValidator[String](fieldName, s => { 
      val capitalLetter = """([A-Z])""".r 
      val matches = capitalLetter.findAllIn(s)
      matches.length > 0
    }, message)
}

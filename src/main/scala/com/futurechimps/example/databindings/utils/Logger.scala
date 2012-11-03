package com.futurechimps.example.databindings
package utils

import grizzled.slf4j.Logger

trait Logging {

  @transient lazy val logger: Logger = Logger(getClass)
}
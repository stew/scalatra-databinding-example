package org.scalatra.example.databinding
package utils

import grizzled.slf4j.Logger

trait Logging {
  @transient lazy val logger: Logger = Logger(getClass)
}
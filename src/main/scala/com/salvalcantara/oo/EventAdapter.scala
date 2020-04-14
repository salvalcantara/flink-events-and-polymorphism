package com.salvalcantara.oo

import com.salvalcantara._
import scala.language.implicitConversions

// Log adapter (case class)
case class LogEvent(payload: Log) extends Event {
  def timestamp: Int = payload.timestamp
}

object LogEvent {
  implicit def fromLog(log: Log): Event = LogEvent(log)
}

// Metric adapter (regular class)
object MetricAdapter {

  implicit class MetricEvent(val payload: Metric) extends Event {
    def timestamp: Int = payload.timestamp
  }
}

package com.salvalcantara.fp

import com.salvalcantara._
import scala.language.implicitConversions

// create instances for the raw events
object EventInstance {

  implicit val logEvent = new LikeEvent[Log] {
    def timestamp(log: Log): Int = log.timestamp
  }

  implicit val metricEvent = new LikeEvent[Metric] {
    def timestamp(metric: Metric): Int = metric.timestamp
  }
}

// add ops to the raw event classes (regular class)
object EventSyntax {

  implicit class Event[T: LikeEvent](val payload: T) {
    val le = implicitly[LikeEvent[T]]
    def timestamp: Int = le.timestamp(payload)
  }
}

// couldn't make it work with Flink!
// add ops to the raw event classes (case class)
object EventSyntax2 {

  case class Event[T: LikeEvent](payload: T) {
    val le = implicitly[LikeEvent[T]]
    def timestamp: Int = le.timestamp(payload)
  }

  implicit def fromPayload[T: LikeEvent](payload: T): Event[T] = Event(payload)  
}
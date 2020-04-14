package com.salvalcantara

import com.salvalcantara.oo._
import com.salvalcantara.oo.LogEvent._
import com.salvalcantara.oo.MetricAdapter._

import org.apache.flink.api.scala._

/**
 * Implements the "EventCount" program using Object Oriented paradigm
 *
 * This example shows how to use subtyping polymorphism:
 *
 *   - Define an Event trait specifying the commonalities
 *   - Define an "adapter" for each specific (raw) event
 *   - The "adapter" serves two purposes:
 *     - Implement the Event trait (subtyping) 
 *     - Wrap the underlying event (favouring composition when possible)
 */
 object EventCountOO {

  def main(args: Array[String]): Unit = {
    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // underlying (raw) events
    val events: DataSet[Event] = env.fromElements(
      Metric(1586736000, "cpu_usage", 0.2),
      Log(1586736005, 1, "invalid login"),
      Log(1586736010, 1, "invalid login"),
      Log(1586736015, 1, "invalid login"),
      Log(1586736030, 2, "valid login"),
      Metric(1586736060, "cpu_usage", 0.8),
      Log(1586736120, 0, "end of world"),
    )

    // count events per hour
    val eventsPerHour = events
    .map { e => (getMinute(e), e) }
    .groupBy(0).reduceGroup { g =>
      val gl = g.toList
      val (hour, count) = (gl.head._1, gl.size)
      (hour, count)
    }

    eventsPerHour.print()
  }

  def getMinute(e: Event): Int = (e.timestamp % 3600) / 60 
}

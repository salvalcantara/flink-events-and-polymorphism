package com.salvalcantara

import com.salvalcantara.fp._
import com.salvalcantara.fp.EventInstance._
import com.salvalcantara.fp.EventSyntax._

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._

/**
 * Implements the "EventCount" program using Functional Programming
 *
 * This example shows how to use adhoc polymorphism (type classes):
 *
 *   - Define type class as a parametric trait 
 *   - Create instances for the specific types at hand
 *   - Provide with a syntax to "attach" the new ops
 */
 object EventCountFP {

  def main(args: Array[String]): Unit = {
    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // underlying (raw) events
    val events: DataSet[Event[_]] = env.fromElements(
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
    .map(new GetMinuteEventTuple())
    .groupBy(0).reduceGroup { g =>
      val gl = g.toList
      val (hour, count) = (gl.head._1, gl.size)
      (hour, count)
    }

    eventsPerHour.print()
  }

  class GetMinuteEventTuple extends RichMapFunction[Event[_], (Int, Event[_])] {

    def map(e: Event[_]): (Int, Event[_]) = (getMinute(e), e)
    
    def getMinute(e: Event[_]): Int = (e.timestamp % 3600) / 60 
  }
}

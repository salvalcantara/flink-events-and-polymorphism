package com

package object salvalcantara {

  // underlying (raw) events, don't have control over them
  case class Log(timestamp: Int, severity: Int, message: String)
  case class Metric(timestamp: Int, name: String, value: Double)
}
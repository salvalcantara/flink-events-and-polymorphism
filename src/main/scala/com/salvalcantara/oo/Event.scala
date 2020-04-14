package com.salvalcantara.oo

// Define trait
trait Event {
  def timestamp: Int
  def payload: Product with Serializable // Any case class
}

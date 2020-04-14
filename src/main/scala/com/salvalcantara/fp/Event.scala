package com.salvalcantara.fp

// Define type class
trait LikeEvent[T] {
  def timestamp(payload: T): Int
}

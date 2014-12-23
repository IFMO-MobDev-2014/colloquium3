package ru.ifmo.md

import java.util.Random

import android.view.View
import android.widget.TextView

package object colloquium3 {
  val rand: Random = new Random()
  // wow so javaway
  def cast[A, B](x: A): B = x match {
    case a: B => a
    case _ => throw new ClassCastException
  }

  def setText(view: View, text: String): Unit = {
    if (view != null) cast[View, TextView](view).setText(text)
  }
}
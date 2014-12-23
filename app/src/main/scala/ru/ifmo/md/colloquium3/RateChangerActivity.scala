package ru.ifmo.md.colloquium3

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.{AsyncTaskLoader, Loader}
import android.os.Bundle
import android.util.Log

class RateChangerActivity extends Activity with LoaderCallbacks[ExchangeRate]{
  var mDatabaseHelper: DatabaseHelper = null
  var mRate: ExchangeRate = null
  var mStatus: ExchangeRate = null

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    mDatabaseHelper = new DatabaseHelper(getApplicationContext)
    mStatus = mDatabaseHelper.mWrapper.getStatus

    ()
  }

    override def onCreateLoader(p1: Int, p2: Bundle): Loader[ExchangeRate] =
    new AsyncTaskLoader[ExchangeRate](RateChangerActivity.this) {
      override def loadInBackground(): ExchangeRate = mDatabaseHelper.mWrapper.getRate
  }

  override def onLoadFinished(p1: Loader[ExchangeRate], p2: ExchangeRate): Unit =
    if (p2 == null) {
      Log.d(this.toString, "generating the rate")
      mDatabaseHelper.mWrapper.putRate(new ExchangeRate(89, 64, 54, 3, 129, 0))
      mRate = mDatabaseHelper.mWrapper.getRate
    } else {
      Log.d(this.toString, "Got rate: " + p2.toString)
      mRate = p2
    }

  override def onLoaderReset(p1: Loader[ExchangeRate]): Unit = null
}

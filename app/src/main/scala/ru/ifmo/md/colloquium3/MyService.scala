package ru.ifmo.md.colloquium3

import android.app.IntentService
import android.content.Intent
import android.os.{ResultReceiver, Handler, Bundle}

object MyService {
  val OK = 0
  val FAULT = 1

  val SERVICE_NAME = "MyLoadService"
  val REFRESH_MODE = "refresh_mode"
  val MODE_SMALL = 0
  val MODE_GIANT = 1

  val RECEIVER1 = "receiver1"
  val RECEIVER2 = "receiver2"
}

class MyService extends IntentService("MyLoadService") {
  override def onHandleIntent(intent: Intent): Unit = {
    import ru.ifmo.md.colloquium3.MyService._
    val dbHelper = new HelperWrapper(getContentResolver)
    val receiver1: ResultReceiver = intent.getParcelableExtra(RECEIVER1)
    val receiver2: ResultReceiver = intent.getParcelableExtra(RECEIVER2)
    val mode = intent.getIntExtra(REFRESH_MODE, -1)
    val rate = dbHelper.getRate
    if (rate == null) {
      if (receiver1 != null) receiver1.send(FAULT, null)
      if (receiver2 != null) receiver2.send(FAULT, null)
    } else {
      mode match {
        case MODE_SMALL => dbHelper.putRate(dbHelper.getRate.randomize())
        case MODE_GIANT => dbHelper.putRate(dbHelper.getRate.randomizeGreat())
      }
      if (receiver1 != null) receiver1.send(OK, null)
      if (receiver2 != null) receiver2.send(OK, null)
    }
  }
}

trait Receiver {
  def onReceiveResult(resCode: Int, resData: Bundle): Unit
}

class LoadReceiver(handler: Handler) extends ResultReceiver(handler) {
  private var mReceiver: Receiver = null
  def setReceiver(r: Receiver) = mReceiver = r
  override def onReceiveResult(resCode: Int, resData: Bundle) = if (mReceiver != null) mReceiver.onReceiveResult(resCode, resData)
}

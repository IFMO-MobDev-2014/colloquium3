package ru.ifmo.md.colloquium3

import java.text.DecimalFormat

import android.app.{PendingIntent, AlarmManager, Activity}
import android.app.LoaderManager.LoaderCallbacks
import android.content.{Context, Intent, AsyncTaskLoader, Loader}
import android.os.{Handler, SystemClock, Bundle}
import android.util.Log
import android.view.{Menu, MenuItem, View, ViewGroup}
import android.widget.AdapterView.OnItemClickListener
import android.widget.{AdapterView, BaseAdapter, ListView}

class RatePreviewActivity extends Activity with LoaderCallbacks[ExchangeRate] with Receiver {
  var mDatabaseHelper: DatabaseHelper = null
  var mRate: ExchangeRate = null
  var mRateList: ListView = null
  var mRateListAdapter: BaseAdapter = null
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    // FIXME: ApplicationContext?
    setContentView(R.layout.rate_preview)
    mRateList = cast(findViewById(R.id.preview_rate_list))
    mRateList.setOnItemClickListener(new OnItemClickListener {
      override def onItemClick(p1: AdapterView[_], p2: View, p3: Int, p4: Long): Unit = {
        // TODO Start exchange activity
      }
    })
    mRateListAdapter = new BaseAdapter {
      override def getItemId(p1: Int): Long = p1
      override def getCount: Int = if (mRate == null) 0 else 5
      override def getView(p1: Int, p2: View, p3: ViewGroup): View = {
        val view = getLayoutInflater.inflate(R.layout.course_pair, p3, false)
        setText(view.findViewById(R.id.currency_name), mRate.getItem(p1)._2)
        setText(view.findViewById(R.id.currency_rate), ((mRate.getItem(p1)._1 * 1000).toInt.toDouble / 1000).toString)
        view
      }
      override def getItem(p1: Int): AnyRef = mRate.getItem(p1)
    }
    mRateList.setAdapter(mRateListAdapter)
    mDatabaseHelper = new DatabaseHelper(getApplicationContext)
    getLoaderManager.restartLoader(0, null, this).forceLoad()
    Log.d(this.toString, "started loader")

    val intent: Intent = new Intent(Intent.ACTION_SYNC, null, getApplicationContext, classOf[MyService])
    intent.putExtra(MyService.REFRESH_MODE, MyService.MODE_SMALL)
    val receiver1 = new LoadReceiver(new Handler())
    receiver1.setReceiver(cast(this))
    intent.putExtra(MyService.RECEIVER1, receiver1)
    cast[Object, AlarmManager](getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(
      AlarmManager.ELAPSED_REALTIME,
      SystemClock.elapsedRealtime(), 1000,
      PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))

//    val intent2: Intent = new Intent(Intent.ACTION_SYNC, null, getApplicationContext, classOf[MyService])
//    intent2.putExtra(MyService.REFRESH_MODE, MyService.MODE_GIANT)
//    intent2.putExtra(MyService.RECEIVER1, receiver1)
//    cast[Object, AlarmManager](getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(
//      AlarmManager.ELAPSED_REALTIME,
//      SystemClock.elapsedRealtime(), 10000,
//      PendingIntent.getService(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT))
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_wallet => ??? // TODO Start wallet activity
    }
  }

  override def onCreateLoader(p1: Int, p2: Bundle): Loader[ExchangeRate] =
    new AsyncTaskLoader[ExchangeRate](RatePreviewActivity.this) {
      override def loadInBackground(): ExchangeRate = mDatabaseHelper.mWrapper.getRate
  }

  override def onLoadFinished(p1: Loader[ExchangeRate], p2: ExchangeRate): Unit =
    if (p2 == null) {
      Log.d(this.toString, "generating the rate")
      mDatabaseHelper.mWrapper.putRate(new ExchangeRate(89, 64, 54, 3, 129, 0))
      mRate = mDatabaseHelper.mWrapper.getRate
      mRateListAdapter.notifyDataSetChanged()
    } else {
      Log.d(this.toString, "Got rate: " + p2.toString)
      mRate = p2
      mRateListAdapter.notifyDataSetChanged()
    }

  override def onLoaderReset(p1: Loader[ExchangeRate]): Unit = null
  override def onReceiveResult(resCode: Int, resData: Bundle): Unit =
    if (resCode == MyService.OK) getLoaderManager.restartLoader(0, null, this).forceLoad()
}

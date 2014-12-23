package ru.ifmo.md.colloquium3

import android.content.{ContentResolver, Context}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.provider.BaseColumns
import android.util.Log

object DatabaseHelper extends BaseColumns {
  private val DATABASE_VERSION: Int = 1
  val DATABASE_NAME = "mydatabase.db"
  val FIRST_TABLE_NAME = "currency"
  val CURRENCY_GBR = "gbr"
  val CURRENCY_USD = "usd"
  val CURRENCY_EUR = "eur"
  val CURRENCY_UAN = "uan"
  val CURRENCY_YEN = "yen"
  val CURRENCY_RUB = "rub"

  // Be careful with spaces!!!
  val CREATE_FIRST_TABLE = "create table " + FIRST_TABLE_NAME + " (" +
    BaseColumns._ID + " integer primary key autoincrement, " +
    CURRENCY_GBR + " real, " +
    CURRENCY_USD + " real, " +
    CURRENCY_EUR + " real, " +
    CURRENCY_UAN + " real, " +
    CURRENCY_YEN + " real," +
    CURRENCY_RUB + " real)"
}

class DatabaseHelper(context: Context) extends SQLiteOpenHelper(context, DatabaseHelper.DATABASE_NAME, null, 1) with BaseColumns {
  val mWrapper = new HelperWrapper(context.getContentResolver)

  override def onCreate(db: SQLiteDatabase): Unit = {
    import ru.ifmo.md.colloquium3.DatabaseHelper._
    db.execSQL(CREATE_FIRST_TABLE)
  }

  override def onUpgrade(p1: SQLiteDatabase, p2: Int, p3: Int): Unit = throw new UnsupportedOperationException("CANNOT UPGRADE DB")
}

class HelperWrapper(mContentResolver: ContentResolver) {
  def putRate(rate: ExchangeRate) = {
    mContentResolver.delete(MyProvider.FIRST_CONTENT_URI, DatabaseHelper.CURRENCY_RUB + "=0", null)
    mContentResolver.insert(MyProvider.FIRST_CONTENT_URI, rate.getValues)
  }

  def getRate = {
    Log.d(this.toString, "getting the rate")
    val cursor = mContentResolver.query(MyProvider.FIRST_CONTENT_URI, null, DatabaseHelper.CURRENCY_RUB + "=0", null, null)
    cursor.moveToFirst()
    cursorToRate(cursor)
  }

  def cursorToRate(cursor: Cursor) =
    if (cursor.isAfterLast)
      null
    else new ExchangeRate(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6))

  def setStatus(status: ExchangeRate) = {
    mContentResolver.delete(MyProvider.FIRST_CONTENT_URI, DatabaseHelper.CURRENCY_RUB + "<>0", null)
    mContentResolver.insert(MyProvider.FIRST_CONTENT_URI, status.getValues)
  }

  def getStatus: ExchangeRate = {
    val cursor = mContentResolver.query(MyProvider.FIRST_CONTENT_URI, null, DatabaseHelper.CURRENCY_RUB + "<>0", null, null)
    cursor.moveToFirst()
    cursorToRate(cursor)
  }
  //  def cursorToWeather(cursor: Cursor): Weather =
//    if (cursor.isAfterLast)
//      null
//    else new Weather(
//      cursor.getString(1),
//      cursor.getString(2),
//      (cursor.getInt(3), cursor.getInt(4)),
//      new WeatherState(cursor.getInt(5), cursor.getString(6)),
//      cursor.getInt(7).toDouble / 100,
//      cursor.getInt(8),
//      cursor.getString(9),
//      cursor.getInt(10),
//      new Date(1000l * cursor.getInt(11).toLong)
//    )
//  def addWeather(weather: Weather): Unit = mContentResolver.insert(MyProvider.MAIN_CONTENT_URI, weather.getValues)
//
//  def getCities(): List[String] = {
//    Log.d(this.toString, "Getting city list")
//    mContentResolver.synchronized {
//      var cursor: Cursor =
//        mContentResolver.query(MyProvider.CITIES_CONTENT_URI, Array(DatabaseHelper.CITY), null, null, null)
//      cursor.moveToFirst()
//      compose(cursor, (curs: Cursor) => curs.getString(0))
//    }
//  }
//
//  def getWeatherByCity(cityname: String) = {
//    mContentResolver.synchronized {
//      val cursor: Cursor =
//        mContentResolver.query(MyProvider.MAIN_CONTENT_URI, null, WEATHER_CITY + "='" + cityname + "'", null, null)
//      cursor.moveToFirst()
//      compose(cursor, cursorToWeather).reverse
//    }
//  }
//
//  def deleteWeatherByCity(cityname: String) =
//    mContentResolver.delete(MyProvider.MAIN_CONTENT_URI, DatabaseHelper.WEATHER_CITY + "='" + cityname + "'", null)
//
//  def addCity(cityname: String) = {
//    val values = new ContentValues()
//    values.put(DatabaseHelper.CITY, cityname)
//    mContentResolver.insert(MyProvider.CITIES_CONTENT_URI, values)
//  }
//
//  def deleteCity(cityname: String) = mContentResolver.delete(MyProvider.CITIES_CONTENT_URI, DatabaseHelper.CITY + "='" + cityname + "'", null)
//
//  def updateCity(oldName: String, newName: String) = {
//    val values = new ContentValues()
//    values.put(DatabaseHelper.CITY, newName)
//    mContentResolver.update(MyProvider.CITIES_CONTENT_URI, values, DatabaseHelper.CITY + "='" + oldName + "'", null)
//  }
//
//  private def compose[A](cursor: Cursor, foo: (Cursor) => A): List[A] =
//    if (cursor.isAfterLast) {cursor.close(); Nil}
//    else foo(cursor) :: compose({
//      cursor.moveToNext()
//      cursor
//    }, foo)
//

}
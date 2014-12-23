package ru.ifmo.md.colloquium3

import android.content.ContentValues

class ExchangeRate(gbr: Double, usd: Double, eur: Double, uan: Double, yen: Double, rub: Double) {
  def randomize(): ExchangeRate = new ExchangeRate(gbr + rand.nextDouble() % 0.1,
    usd + rand.nextDouble() % 0.1,
    eur + rand.nextDouble() % 0.1,
    uan + rand.nextDouble() % 0.1,
    yen + rand.nextDouble() % 0.1,
    rub)
  def randomizeGreat(): ExchangeRate = new ExchangeRate(gbr + rand.nextDouble() % 1,
    usd + rand.nextDouble() % 1,
    eur + rand.nextDouble() % 1,
    uan + rand.nextDouble() % 1,
    yen + rand.nextDouble() % 1,
    rub)
  def getValues = {
    import ru.ifmo.md.colloquium3.DatabaseHelper._
    val values: ContentValues = new ContentValues()
    values.put(CURRENCY_GBR, Double.box(gbr))
    values.put(CURRENCY_EUR, Double.box(eur))
    values.put(CURRENCY_USD, Double.box(usd))
    values.put(CURRENCY_UAN, Double.box(uan))
    values.put(CURRENCY_YEN, Double.box(yen))
    values.put(CURRENCY_RUB, Double.box(rub))
    values
  }
  def getItem(index: Int): (Double, String) = index match {
    case 0 => (gbr, "GBR")
    case 1 => (usd, "USD")
    case 2 => (eur, "EUR")
    case 3 => (uan, "UAN")
    case 4 => (yen, "YEN")
    case _ => (-1, "ERROR")
  }

  override def toString = "GBR: " + gbr.toString
}
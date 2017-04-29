package com.vogonjeltz.tradingnetworks.lib

import scala.collection.mutable.ListBuffer

/**
  * StocksPredictor
  *
  * Created by fredd
  */
trait StocksPredictor {

  def trainingStage(): Unit

  def predictDay(): Double

  def correctForDay(prediction: Double, day: (String, Double, Double)): Double

  /**
    *
    * @param data Stock to evaluate on
    * @param choiceThreshold pc threshold
    * @return (profitLoss, error)
    */
  def evaluate(data: StockHistory, choiceThreshold: Double = 0.02): (List[Double], List[Double]) = {

    trainingStage()
    correctForDay(predictDay(), data.head)

    var dayBefore = data.head
    var cash = 100d
    val plOverTime = ListBuffer[Double](cash)
    val errorOverTime = ListBuffer[Double]()

    var numOwned = 0d

    for (day <- data.tail) {

      val prediction = predictDay()
      correctForDay(prediction, day)

      val sign =  if (prediction > dayBefore._2 * (1 + choiceThreshold)) 1
                  else if (prediction < dayBefore._2 * (1 - choiceThreshold)) -1
                  else 0

      val percentageError = (prediction - day._2) / day._2
      errorOverTime.append(percentageError)

      if (sign == 1) {
        //If currently in SHORT position then close that and buy
        if (numOwned <= 0) {
          cash += numOwned * day._2
          numOwned = cash / day._2
          cash = 0
        }

      } else if (sign == -1) {

        if (numOwned >= 0) {
          cash += numOwned * day._2
          numOwned = -cash / day._2
          cash += -numOwned * day._2
        }

      } else {
        //Close all positions to reduce exposure
        //cash += numOwned * day._2
        //numOwned = 0
      }

      plOverTime.append(cash + numOwned * day._2)
      dayBefore = day

    }


    cash += numOwned * dayBefore._2
    plOverTime.append(cash)
    (plOverTime.toList, errorOverTime.toList)

  }

}

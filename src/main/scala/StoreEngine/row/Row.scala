package StoreEngine.row

import StoreEngine.value.{IntValue, StringValue, Value}

/**
  * Created by jianwei.yang on 2017/4/22.
  */
class Row {
  var key: IntValue = null             //Btree cluster index's key
  var valueList: Seq[Value] = null


  def addRow(value: Seq[Value]): Row = {
    key=value(0).asInstanceOf[IntValue]
    valueList = value
    this
  }

  def get(index: Int): Any = {
    val value = valueList(index)
    if (value.isInstanceOf[IntValue]) {
      getInt(valueList(index))
    }
    else if (value.isInstanceOf[StringValue]) {
      getString(valueList(index))
    }
  }


  def getInt(value: Value): Int = {
    value.asInstanceOf[IntValue].get()
  }

  def getString(value: Value): String = {
    value.asInstanceOf[StringValue].get()
  }
}

object Row {
  def apply(value: Seq[Value]) = new Row().addRow(value)

  def apply() = new Row()
}
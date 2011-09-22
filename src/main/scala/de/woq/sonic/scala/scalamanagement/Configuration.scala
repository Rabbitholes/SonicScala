package de.woq.sonic.scala.scalamanagement

import scala.collection.JavaConverters._
import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase
import com.sonicsw.mx.config.{IAttributeMap, IConfigBean}

abstract class Configuration(o: Configuration, n: String)
{
  val owner = o
  val name = n

  def children : List[Configuration] = List()

  def owns(testee: IConfigBean): Boolean = {
    if (testee == null) false
    else if (owner == null) false
    else if (
      owner match {
        case ref: ConfigRef => ref.value == testee
        case _ => false
      }
    ) true
    else (owner.owns(testee))
  }

  def attribute (name: String, attr: Any) : Configuration = attr match
  {
    case s: String => StringConfiguration(this, name, s)
    case b: Boolean => BooleanConfiguration(this, name, b)
    case n: Number => NumericConfiguration(this, name, n)
    case ref: IConfigBean if !(ref.isInstanceOf[IMgmtBeanBase]) =>
    {
      ConfigRef(this, name, ref)
    }
    case attrMap: IAttributeMap => MapConfiguration(this, "AttrMap", attrMap)
  }
}

case class NumericConfiguration(o: Configuration, n: String, v: Number) extends Configuration(o, n)
{
  val value = v
  override def toString = "Numeric(" + name + "->" + v + ")"
}

case class BooleanConfiguration(o: Configuration, n: String, v: Boolean) extends Configuration(o, n)
{
  val value = v
  override def toString = "Boolean(" + name + "->" + v + ")"
}

case class StringConfiguration(o: Configuration, n: String, v: String) extends Configuration(o, n)
{
  val value = v
  override def toString = "String(" + name + "->" + v + ")"
}

case class ConfigRef(o: Configuration, n: String, v: IConfigBean) extends Configuration(o, n)
{
  val value = v

  override def toString = "ConfigRef(" + name + "->" + children.toString() + ")"

  override def children =
    if (this.owns(value))
      StringConfiguration(this, "REFERENCE", value.getName) :: List()
    else
      MapConfiguration(this, "REFERENCE", value.asInstanceOf[IAttributeMap]) :: List()
}

case class MapConfiguration(o: Configuration, n: String, v: IAttributeMap) extends Configuration(o, n)
{
  val value = v;

  override def toString = "MapConfig(" + name + "->" + children.toString() + ")"

  override def children =
  {
    val attrNames = value.getAttributeNames.asScala.map(_.asInstanceOf[String]).toList
    for(attrName <- attrNames) yield
    {
      attribute(attrName, value.getAttribute(attrName))
    }
  }
}

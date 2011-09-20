package de.woq.sonic.scala.scalamanagement

abstract class ConfigAttribute
case class UnknownAttribute() extends ConfigAttribute
case class NumericAttribute(value: Number) extends ConfigAttribute
case class BooleanAttribute(value: Boolean) extends ConfigAttribute
case class StringAttribute(value: String) extends ConfigAttribute
case class MapAttribute(value: Map[String, ConfigAttribute]) extends ConfigAttribute
case class ListAttribute(value: List[ConfigAttribute]) extends ConfigAttribute

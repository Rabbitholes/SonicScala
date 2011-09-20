package de.woq.sonic.scala.scalamanagement

abstract class AbstractConfigBean
case class ConfigBean(attributes: Map[String, ConfigAttribute]) extends AbstractConfigBean
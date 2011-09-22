package de.woq.sonic.scala.scalamanagement

import scala.collection.JavaConverters._
import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase

class SonicDomainQueries(connector : SonicDomainConnector) {

  val sonicConnector = connector

  lazy val brokerBeanNames =
    sonicConnector.mqBeanFactory.getBrokerBeanNames.asScala.map(_ + "/_Default")

  lazy val containerBeanNames =
    sonicConnector.mfBeanFactory.getContainerBeanNames.asScala.map(_.asInstanceOf[String])

  lazy val esbContainerBeanNames =
    sonicConnector.xqBeanFactory.getXQContainerBeanNames.asScala.map(_.asInstanceOf[String])

  def getConfigBean(configBeanName: String): IMgmtBeanBase =
    try {
      connector.mqBeanFactory.getBean(configBeanName)
    } catch {
      case ex1 =>
      try {
        connector.xqBeanFactory.getBean(configBeanName)
      } catch {
        case ex2 => connector.mfBeanFactory.getBean(configBeanName)
      }
    }
}
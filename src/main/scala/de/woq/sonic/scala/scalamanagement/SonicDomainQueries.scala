package de.woq.sonic.scala.scalamanagement

import scala.collection.JavaConverters._
import com.sonicsw.mq.mgmtapi.config.IBrokerBean
import com.sonicsw.xqimpl.mgmtapi.config.IXQContainerBean

class SonicDomainQueries(connector : SonicDomainConnector) {

  val sonicConnector = connector

  lazy val brokerBeanNames =
    sonicConnector.mqBeanFactory.getBrokerBeanNames.asScala.map(_.asInstanceOf[String])

  lazy val esbContainerBeanNames =
    sonicConnector.xqBeanFactory.getXQContainerBeanNames.asScala.map(_.asInstanceOf[String])

  def getBrokerInfo(brokerName : String) : IBrokerBean =
    connector.mqBeanFactory.getBrokerBean(brokerName)

  def getEsbContainerInfo(esbContainerName : String) : IXQContainerBean =
    connector.xqBeanFactory.getXQContainerBean(esbContainerName)
}
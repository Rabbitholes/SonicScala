package de.woq.sonic.scala.scalamanagement.scripts

import de.woq.sonic.scala.scalamanagement.{MgmtBeanBase, SonicDomainQueries, SonicDomainConnector}

object SimpleConnectionTest {

  def main(args : Array[String]) {

    val connector = new SonicDomainConnector("dmModel", "tcp://192.168.22.141:2608")
    val queries = new SonicDomainQueries(connector)

    val beans = (
      queries.brokerBeanNames       :::
      queries.esbContainerBeanNames :::
      queries.containerBeanNames
    ).map(queries.getConfigBean)

    beans.foreach(bean => println(new MgmtBeanBase(bean).configuration))
    connector.close()
  }
}
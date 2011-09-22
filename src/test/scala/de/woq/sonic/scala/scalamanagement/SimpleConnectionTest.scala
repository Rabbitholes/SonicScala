package de.woq.sonic.scala.scalamanagement

object SimpleConnectionTest {

  def main(args : Array[String]) {

    val connector = new SonicDomainConnector("dmModel", "tcp://192.168.22.141:2608")
    val queries = new SonicDomainQueries(connector)

    // val beans = queries.brokerBeanNames.map(name => connector.mqBeanFactory.getBrokerBean(name))
    // val beans = queries.brokerBeanNames.map(queries.getConfigBean)
    // val beans = queries.esbContainerBeanNames.map(queries.getConfigBean)
    val beans = queries.containerBeanNames.map(queries.getConfigBean)

    beans.foreach(bean => println(new MgmtBeanBase(bean).configuration))

    connector.close()
  }
}
package de.woq.sonic.scala.scalamanagement

object SimpleConnectionTest {

  def main(args : Array[String]) {

    val connector = new SonicDomainConnector("dmModel", "tcp://192.168.22.141:2608")
    val queries = new SonicDomainQueries(connector)

    val brokers = queries.brokerBeanNames.map(queries.getBrokerInfo)
    val esbContainers = queries.esbContainerBeanNames.map(queries.getEsbContainerInfo)

    println(brokers.mkString("\n"))
    println(esbContainers.mkString("\n"))

    connector.close()
  }
}
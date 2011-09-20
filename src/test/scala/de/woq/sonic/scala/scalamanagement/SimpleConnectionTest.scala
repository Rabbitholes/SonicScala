package de.woq.sonic.scala.scalamanagement

import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase
import com.sonicsw.mx.config.ConfigAttributeException

object SimpleConnectionTest {

  def main(args : Array[String]) {

    val connector = new SonicDomainConnector("dmModel", "tcp://192.168.22.141:2608")
    val queries = new SonicDomainQueries(connector)

    val brokers = queries.brokerBeanNames.map(queries.getBrokerInfo)

    val beans = brokers.map(broker => {
      val bean = new MgmtBeanBase(broker)
      bean.attributes
    })

    println(beans.mkString("\n"))
    connector.close()
  }


}
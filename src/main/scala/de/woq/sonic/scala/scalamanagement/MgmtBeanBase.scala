package de.woq.sonic.scala.scalamanagement

import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase
class MgmtBeanBase(bean: IMgmtBeanBase) {

  private val mgmtBean = bean

  lazy val configBeanName = mgmtBean.getConfigBeanName
  lazy val configBean = mgmtBean.getConfigBean
  lazy val configuration = ConfigRef(null, mgmtBean.getConfigBeanName, configBean)
}
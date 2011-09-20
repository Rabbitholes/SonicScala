package de.woq.sonic.scala.scalamanagement

import scala.collection.JavaConverters._
import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase
import com.sonicsw.mx.config.impl.{AttributeListImpl, AttributeMapImpl}
import com.sonicsw.mx.config.{ConfigAttributeException, IConfigBean, IAttributeList, IAttributeMap}

class MgmtBeanBase(bean: IMgmtBeanBase) {

  private val mgmtBean = bean

  lazy val configBeanName = mgmtBean.getConfigBeanName
  lazy val configBean = mgmtBean.getConfigBean

  lazy val attributes = {
    var result: Map[String, Any] = Map()

    configBean.getAttributeNames.asScala.map(_.asInstanceOf[String]).foreach( attrName => {
      result += (attrName -> attribute(configBean.getAttribute(attrName)))
    })

    result
  }

  def attribute (attr: Any) : ConfigAttribute = attr match
  {
    case s: String => StringAttribute(s)
    case b: Boolean => BooleanAttribute(b)
    case n: Number => NumericAttribute(n)
    case attrMap: AttributeMapImpl => {
      println(attrMap)
      var attributes: Map[String, ConfigAttribute] = Map()
      attrMap.getAllAttributeNames.asScala.map(_.asInstanceOf[String]).foreach(attrName => {
        val a = attrMap.get(attrName)
        if (a != null) {
          attributes += (attrName -> attribute(a))
        }
      })
      MapAttribute(attributes)
    }
    case list: AttributeListImpl => {
      var attributes: List[ConfigAttribute] = List()
      (0 to list.size() - 1).foreach(i => {
        attributes = (attribute(list.get(i))) :: attributes
      })
      ListAttribute(attributes)
    }

    case _ => UnknownAttribute()
  }
}
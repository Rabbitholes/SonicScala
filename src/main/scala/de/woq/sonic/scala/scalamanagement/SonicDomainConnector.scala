package de.woq.sonic.scala.scalamanagement

import com.sonicsw.mq.mgmtapi.config.MQMgmtBeanFactory
import com.sonicsw.mf.mgmtapi.config.MFMgmtBeanFactory
import com.sonicsw.mx.config.util.SonicFSFileSystem
import com.sonicsw.mf.jmx.client.{DirectoryServiceProxy, JMSConnectorAddress, JMSConnectorClient}
import com.sonicsw.mf.mgmtapi.runtime.impl.AgentManagerProxy
import javax.management.ObjectName
import com.sonicsw.deploy.storage.DSArtifactStorage
import com.sonicsw.xqimpl.mgmtapi.config.XQMgmtBeanFactory
import java.util.Hashtable
import javax.naming.{InitialContext, Context}

class SonicDomainConnector (
  domain   : String,
  url      : String,
  user     : String,
  pwd      : String
)
{
  val domainName = domain
  val sonicUrl = url
  val userName = user
  val password = pwd

  private var hasMQBeanFactory = false
  private var hasMFBeanFactory = false
  private var hasXQBeanFactory = false
  private var hasConnectorClient = false
  private var hasContext = false
  private var hasDS = false
  private var hasDSStore = false


  def this() = this("Domain", "tcp://localhost:2506", "Administrator", "Administrator")
  def this(domain: String, url : String) = this(domain, url, "Administrator", "Administrator")

  /**
   * Initialize the MQ Management Bean Factory on demand and return it. The initialization
   * code is run only once.
   */
  lazy val mqBeanFactory = {
    val result = new MQMgmtBeanFactory
    result.connect(domainName, sonicUrl, userName, password)
    hasMQBeanFactory = true
    result
  }

  lazy val mfBeanFactory = {
    val result = new MFMgmtBeanFactory()
    result.connect(domainName, sonicUrl, userName, password)
    hasMFBeanFactory = true
    result
  }

  lazy val jmsConnectorClient = {

    val env = new Hashtable[String, String]()

    env.put("ConnectionURLs", sonicUrl);
    env.put("DefaultUser", userName);
    env.put("DefaultPassword", password);

    val address = new JMSConnectorAddress(env)

    val result = new JMSConnectorClient();
    result.setRequestTimeout(10000);
    result.connect(address, 10000);

    hasConnectorClient = true

    result
  }

  lazy val directoryService = {
    val objectName = ".DIRECTORY SERVICE:ID=DIRECTORY SERVICE";
    val result = new DirectoryServiceProxy(jmsConnectorClient, new ObjectName(domainName + objectName))
    hasDS = true
    result
  }

  lazy val agentManager = {
    val id = "AGENT MANAGER"
    val objectName = "." + id + ":ID=" + id
    val result = new AgentManagerProxy(jmsConnectorClient, new ObjectName(domainName + objectName))
    result
  }

  lazy val sonicFileSystem = new SonicFSFileSystem(directoryService, userName)

  lazy val xqBeanFactory = {
    val result = new XQMgmtBeanFactory()
    result.connect(domainName, sonicUrl, userName, password)
    hasXQBeanFactory = true
    result
  }

  lazy val dsArtifactStore = {
    val result = new DSArtifactStorage()
    result.connect(domainName, sonicUrl, userName, password)
    hasDSStore = true
    result
  }

  lazy val initialContext = {
    val env = new Hashtable[String, String]()
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sonicsw.jndi.mfcontext.MFContextFactory");
    env.put(Context.PROVIDER_URL, sonicUrl);
    env.put(Context.SECURITY_PRINCIPAL, userName);
    env.put(Context.SECURITY_CREDENTIALS, password);
    env.put("com.sonicsw.jndi.mfcontext.domain", domainName);
    env.put("com.sonicsw.jndi.mfcontext.idleTimeout", "120000");

    hasContext = true
    new InitialContext(env)
  }

  def close() {

    if (hasDSStore) dsArtifactStore.disconnect()
    if (hasMQBeanFactory) mqBeanFactory.disconnect()
    if (hasMFBeanFactory) mfBeanFactory.disconnect()
    if (hasXQBeanFactory) xqBeanFactory.disconnect()
    if (hasConnectorClient) jmsConnectorClient.disconnect()
    if (hasContext) initialContext.close()
  }

  override def toString = "SonicConnector(" + domainName + "," + sonicUrl + "," + userName + ")"
}
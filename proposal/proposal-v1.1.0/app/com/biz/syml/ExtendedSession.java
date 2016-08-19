package com.biz.syml;

import org.apache.xmlrpc.XmlRpcException;

import com.debortoliwines.openerp.api.OpenERPXmlRpcProxy.RPCProtocol;
import com.debortoliwines.openerp.api.OpenERPXmlRpcProxy;
import com.debortoliwines.openerp.api.Session;
import com.debortoliwines.openerp.api.Version;

public class ExtendedSession extends Session {

	private RPCProtocol protocol;
	private String host;
	private int port;

	public ExtendedSession(RPCProtocol protocol, String host, int port, String databaseName, String userName, String password) {
		super(protocol, host, port, databaseName, userName, password);
		this.protocol = protocol;
		this.host = host;
		this.port = port;
	}

	/** Will connect with {@link RPCProtocol#RPC_HTTP} */
	public ExtendedSession(String host, int port, String databaseName, String userName, String password) {
		this(RPCProtocol.RPC_HTTP, host, port, databaseName, userName, password);
	}

	@Override
	public Version getServerVersion() throws XmlRpcException {
		  return OpenERPXmlRpcProxy.getServerVersion(protocol, host, port);
	}
}

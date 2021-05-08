/**
 * 
 */
package bgu.spl.net.api.bidi.impl;

import java.util.concurrent.ConcurrentHashMap;
 
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {
	private ConcurrentHashMap<Integer,ConnectionHandler<T>> mainMap;
	
	public ConnectionsImpl() {
		this.mainMap=new ConcurrentHashMap<Integer,ConnectionHandler<T>>();
	}

	@Override
	public boolean send(int connectionId, T msg) {
		if(this.mainMap.containsKey(connectionId)) {
			this.mainMap.get(connectionId).send(msg);
			return true;
		}
		return false;
	}

	@Override
	public void broadcast(T msg) {
		for(ConnectionHandler<T> cli:this.mainMap.values())
			cli.send(msg);
	}

	@Override
	public void disconnect(int connectionId) {
		if(this.mainMap.get(connectionId)!=null) {
			this.mainMap.remove(connectionId);
		}
		
	}
	
	public void addNewClient(ConnectionHandler<T> client, int connectionID) {
		this.mainMap.put(connectionID, client);
	}
	
	public ConcurrentHashMap<Integer,ConnectionHandler<T>> getConnectionsMap(){
		return this.mainMap;
	}

}

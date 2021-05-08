/**
 * 
 */ 
package bgu.spl.net.api.bidi.impl;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Operation;

/**
 *
 *
 */
public class bidiMessageProtocolImpl implements BidiMessagingProtocol<Operation> {

	private boolean shouldTerminate = false;
	private int connectionId;
	private Connections<Operation> connections;
	private DataBase usersData;

	public bidiMessageProtocolImpl(DataBase data) {
		this.usersData = data;
	}

	@Override
	public void start(int connectionId, Connections<Operation> connections) {
		this.connectionId = connectionId;
		this.connections = connections;

	}

	@Override
	public void process(Operation message) {
		//Only LOGOUT operation returns a "Terminate" string and that if it succeeded.
		String s;
		s = message.process(connections, this.connectionId, this.usersData);
		if (s != null && s.equals("Terminate"))
			this.shouldTerminate = true;
		if (this.shouldTerminate == true) {
			this.connections.disconnect(this.connectionId);
		}

	}

	@Override
	public boolean shouldTerminate() {
		return this.shouldTerminate;
	}

	public DataBase getData() {
		return this.usersData;
	}

}

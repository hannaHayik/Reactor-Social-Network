package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationStat extends Operation {

	private String name;

	public OperationStat(byte[] msg) {
		super(msg);
		byte[] nameInBytes = new byte[msg.length - 1];
		for (int i = 0; i < nameInBytes.length; i++)
			nameInBytes[i] = msg[i];
		this.name = new String(nameInBytes, StandardCharsets.UTF_8);
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if (database.checkIfOnline(numberOfConnection)) {
			if (database.checkIfExists(this.name)) {
				connections.send(numberOfConnection,
						new OperationAck(this.getOperationCode(), database.NumberOfPosts(this.name),
								database.NumberOfFollwers(this.name), database.NumberOfFollowing(this.name)));
			} else {
				connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
			}
		} else {
			connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
		}
		return null;
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encode(Operation message) {
		// TODO Auto-generated method stub
		return null;
	}

}

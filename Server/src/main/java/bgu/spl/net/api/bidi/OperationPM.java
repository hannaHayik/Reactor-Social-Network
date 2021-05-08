package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationPM extends Operation {
	private String nameOfReceiver;
	private String message;

	public OperationPM(byte[] msg) {
		super(msg);
		Vector<Byte> toWork = new Vector<Byte>();
		int i = 0;
		while (msg[i] != 0) {
			toWork.add(msg[i]);
			i++;
		}
		byte[] nameInBytes = new byte[toWork.size()];
		for (int x = 0; x < toWork.size(); x++)
			nameInBytes[x] = toWork.get(x);
		this.nameOfReceiver = new String(nameInBytes, StandardCharsets.UTF_8);
		Vector<Byte> toWork2 = new Vector<Byte>();
		i++;
		while (msg[i] != 0) {
			toWork2.add(msg[i]);
			i++;
		}
		byte[] contentInBytes = new byte[toWork2.size()];
		for (int x = 0; x < toWork2.size(); x++)
			contentInBytes[x] = toWork2.get(x);
		this.message = new String(contentInBytes, StandardCharsets.UTF_8);

	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if (database.checkIfOnline(numberOfConnection)) {
			if (!(database.checkIfExists(this.nameOfReceiver))) {
				connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
				return null;
			}
			// If PMed user online, send the message right to his socket through
			// connections.
			if (database.CheckIfOnline(this.nameOfReceiver) == -1) {
				database.addMessageToUser(message, this.nameOfReceiver, database.getUserName(numberOfConnection));
				connections.send(numberOfConnection, new OperationAck(this.getOperationCode()));
				return null;
			}
			//If offline, save the message in the database to show it to him when he logs in.
			else {
				connections.send(database.CheckIfOnline(this.nameOfReceiver),
						new OperationNotification(false, database.getUserName(numberOfConnection), this.message));
				connections.send(numberOfConnection, new OperationAck(this.getOperationCode()));
				return null;

			}

		} else {
			connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
			return null;
		}

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

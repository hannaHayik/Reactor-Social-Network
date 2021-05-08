package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationError extends Operation {

	private int numOfError;

	public OperationError(int numOfMsg) {
		super(null);
		this.numOfError = numOfMsg;
	}

	public int getOperationCode() {
		return 11;
	}

	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		connections.send(numberOfConnection, this);
		return null;
	}

	public String getOperationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encode(Operation message) {
		byte[] OpNumInBytes = this.shortToBytes((short) this.getOperationCode());
		byte[] NumOfErrorInBytes = this.shortToBytes((short) this.numOfError);
		byte[] toReturn = new byte[OpNumInBytes.length + NumOfErrorInBytes.length];
		toReturn[0] = OpNumInBytes[0];
		toReturn[1] = OpNumInBytes[1];
		toReturn[2] = NumOfErrorInBytes[0];
		toReturn[3] = NumOfErrorInBytes[1];
		return toReturn;

	}

}

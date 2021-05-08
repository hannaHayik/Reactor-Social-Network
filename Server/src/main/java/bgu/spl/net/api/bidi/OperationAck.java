package bgu.spl.net.api.bidi;

import java.util.List;
import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationAck extends Operation {
	private int OpNumber;
	private int listSize;
	private List<String> listOfNames;
	private int numberOfPosts;
	private int numberOfFollowers;
	private int numberOfFollowing;

	public OperationAck(int OpNumber) { //3 Different constructors according to which ACK we want.
		super(null);
		this.OpNumber = OpNumber;
	}

	public OperationAck(int operationCode, int numberOfPosts, int numberOfFollowers, int numberOfFollowing) {
		super(null);
		this.numberOfFollowers = numberOfFollowers;
		this.numberOfFollowing = numberOfFollowing;
		this.numberOfPosts = numberOfPosts;
		this.OpNumber = operationCode;

	}

	public OperationAck(int OpNumber, int listSize, List<String> listOfNames) {
		super(null);
		this.OpNumber = OpNumber;
		this.listSize = listSize;
		this.listOfNames = listOfNames;
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) { //ACK is send-only.
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encode(Operation message) { //ACK encodes itself according to operation type.
		byte[] ackNumberBytes = this.shortToBytes((short) this.getOperationCode());
		byte[] toReturn;
		String zeroByte = "\0";
		if (this.OpNumber == 1 || this.OpNumber == 2 || this.OpNumber == 3 || this.OpNumber == 5 || this.OpNumber == 6
				|| this.OpNumber == 9) {
			byte[] OpNumBytes = this.shortToBytes((short) this.OpNumber);
			toReturn = new byte[4];
			toReturn[0] = ackNumberBytes[0];
			toReturn[1] = ackNumberBytes[1];
			toReturn[2] = OpNumBytes[0];
			toReturn[3] = OpNumBytes[1];
			return toReturn;
		} else if (this.OpNumber == 4 || this.OpNumber == 7) {
			Vector<Byte> tmp = new Vector<Byte>();
			tmp.add(this.shortToBytes((short) this.getOperationCode())[0]);
			tmp.add(this.shortToBytes((short) this.getOperationCode())[1]);
			tmp.add(this.shortToBytes((short) this.OpNumber)[0]);
			tmp.add(this.shortToBytes((short) this.OpNumber)[1]);
			tmp.add(this.shortToBytes((short) this.listSize)[0]);
			tmp.add(this.shortToBytes((short) this.listSize)[1]);
			for (int i = 0; i < this.listOfNames.size(); i++) {
				for (int j = 0; j < this.listOfNames.get(i).getBytes().length; j++)
					tmp.add(this.listOfNames.get(i).getBytes()[j]);
				tmp.add(zeroByte.getBytes()[0]);
			}
			toReturn = new byte[tmp.size()];
			for (int i = 0; i < tmp.size(); i++)
				toReturn[i] = tmp.get(i);
			return toReturn;
		} else if (this.OpNumber == 8) {
			toReturn=new byte[10];
			toReturn[0]=this.shortToBytes((short) this.getOperationCode())[0];
			toReturn[1]=this.shortToBytes((short) this.getOperationCode())[1];
			toReturn[2]=this.shortToBytes((short) this.OpNumber)[0];
			toReturn[3]=this.shortToBytes((short) this.OpNumber)[1];
			toReturn[4]=this.shortToBytes((short) this.numberOfPosts)[0];
			toReturn[5]=this.shortToBytes((short) this.numberOfPosts)[1];
			toReturn[6]=this.shortToBytes((short) this.numberOfFollowers)[0];
			toReturn[7]=this.shortToBytes((short) this.numberOfFollowers)[1];
			toReturn[8]=this.shortToBytes((short) this.numberOfFollowing)[0];
			toReturn[9]=this.shortToBytes((short) this.numberOfFollowing)[1];
			return toReturn;
		}
		return null;
	}

}

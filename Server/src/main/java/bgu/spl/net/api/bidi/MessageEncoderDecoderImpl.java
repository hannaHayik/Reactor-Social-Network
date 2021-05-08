package bgu.spl.net.api.bidi;

import java.util.Vector;

import bgu.spl.net.api.MessageEncoderDecoder;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Operation> {

	private int OpNumberCounter = 0; //if == 2, means we got an OP number.
	private Vector<Byte> bytesVector = new Vector<Byte>(); //Instead of using char[1024] we use a dynamic Data structure.
	private int OpNumber = 0;
	private int zeroBytesCounter = 0;
	private int followCounter = 0;
	private int followNumOfUsers = 0;

	@Override
	public Operation decodeNextByte(byte nextByte) {
		if (this.OpNumber == 0) {
			this.bytesVector.add(nextByte);
			this.OpNumberCounter++;
		}
		if (this.OpNumberCounter < 2) {
			return null;
		} else if (this.OpNumberCounter == 2) {
			byte[] numOfOperation = new byte[2];
			numOfOperation[0] = bytesVector.get(0);
			numOfOperation[1] = bytesVector.get(1);
			this.OpNumber = this.bytesToShort(numOfOperation);
			if (this.OpNumber == 7) {
				this.OpNumber = 0;
				this.OpNumberCounter = 0;
				this.bytesVector.clear();
				return (new OperationUserList(null));
			}
			if (this.OpNumber == 3) {
				this.OpNumber = 0;
				this.OpNumberCounter = 0;
				this.bytesVector.clear();
				return (new OperationLogout(null));
			}
			this.bytesVector.clear();
			this.OpNumberCounter++;
			return null;
		} else {
			if (this.OpNumber == 1 || this.OpNumber == 2 || this.OpNumber == 6) {
				this.bytesVector.add(nextByte);
				if (nextByte == 0)
					this.zeroBytesCounter++;
				if (zeroBytesCounter == 2) {
					byte[] toReturn = new byte[this.bytesVector.size()];
					for (int i = 0; i < toReturn.length; i++)
						toReturn[i] = this.bytesVector.get(i);
					this.bytesVector.clear();
					this.zeroBytesCounter = 0;
					if (this.OpNumber == 1) {
						this.OpNumber = 0;
						this.OpNumberCounter = 0;
						return (new OperationRegister(toReturn));
					}
					if (this.OpNumber == 2) {
						this.OpNumber = 0;
						this.OpNumberCounter = 0;
						return (new OperationLogin(toReturn));
					}
					if (this.OpNumber == 6) {
						this.OpNumber = 0;
						this.OpNumberCounter = 0;
						return (new OperationPM(toReturn));
					}
				} else {
					return null;
				}
			} else if (this.OpNumber == 4) {
				this.bytesVector.add(nextByte);
				this.followCounter++;
				if (followCounter == 3) {
					byte[] tmp = new byte[2];
					tmp[0] = this.bytesVector.get(1);
					tmp[1] = this.bytesVector.get(2);
					this.followNumOfUsers = (int) this.bytesToShort(tmp);
				} else if (this.followCounter > 3) {
					if (nextByte == 0)
						this.zeroBytesCounter++;
					if (this.zeroBytesCounter == this.followNumOfUsers) {
						byte[] toSend = new byte[this.bytesVector.size()];
						for (int i = 0; i < this.bytesVector.size(); i++)
							toSend[i] = this.bytesVector.get(i);
						this.OpNumber = 0;
						this.bytesVector.clear();
						this.followCounter = 0;
						this.followNumOfUsers = 0;
						this.zeroBytesCounter = 0;
						this.OpNumberCounter = 0;
						return (new OperationFollow(toSend));

					} else {
						return null;
					}
				} else {
					return null;
				}
			} else if (this.OpNumber == 5 || this.OpNumber == 8) {
				this.bytesVector.add(nextByte);
				if (nextByte == 0) {
					byte[] toSend = new byte[this.bytesVector.size()];
					for (int i = 0; i < this.bytesVector.size(); i++)
						toSend[i] = this.bytesVector.get(i);
					this.bytesVector.clear();
					if (this.OpNumber == 5) {
						this.OpNumber = 0;
						this.OpNumberCounter = 0;
						return (new OperationPost(toSend));
					}
					if (this.OpNumber == 8) {
						this.OpNumber = 0;
						this.OpNumberCounter = 0;
						return (new OperationStat(toSend));
					}

				} else
					return null;
			}
		}
		return null;
	}

	@Override
	public byte[] encode(Operation message) { //Every Operation has it's own encoding.
		return message.encode(message);
	}

	public short bytesToShort(byte[] byteArr) {
		short result = (short) ((byteArr[0] & 0xff) << 8);
		result += (short) (byteArr[1] & 0xff);
		return result;
	}

	public byte[] shortToBytes(short num) {
		byte[] bytesArr = new byte[2];
		bytesArr[0] = (byte) ((num >> 8) & 0xFF);
		bytesArr[1] = (byte) (num & 0xFF);
		return bytesArr;
	}

}

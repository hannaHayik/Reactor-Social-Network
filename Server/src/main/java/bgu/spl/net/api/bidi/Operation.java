package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.impl.DataBase;

public abstract class Operation {
	@SuppressWarnings("unused")
	private byte[] msg;
	
	public Operation(byte[] msg) {
		this.msg=msg;
	}
	
	public abstract int getOperationCode();

	public abstract String process(Connections<Operation> connections, int numberOfConnection, DataBase database);
	
	public abstract String getOperationName();
	
	public abstract byte[] encode(Operation message);
	
	public short bytesToShort(byte[] byteArr)
	{
	    short result = (short)((byteArr[0] & 0xff) << 8);
	    result += (short)(byteArr[1] & 0xff);
	    return result;
	}
	
	public byte[] shortToBytes(short num)
	{
	    byte[] bytesArr = new byte[2];
	    bytesArr[0] = (byte)((num >> 8) & 0xFF);
	    bytesArr[1] = (byte)(num & 0xFF);
	    return bytesArr;
	}

}

package bgu.spl.net.api.bidi;

import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationNotification extends Operation {
	private boolean isPublic;
	private String sender;
	private String message;
	
	public OperationNotification(boolean isPublic,String sender,String message) {
		super(null);
		this.isPublic=isPublic;
		this.sender=sender;
		this.message=message;
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 9;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encode(Operation message) {//If public encode 1 otherwise encode zero byte.
		Vector<Byte> tmp=new Vector<Byte>();
		String zeroByte="\0";
		tmp.add(this.shortToBytes((short)this.getOperationCode())[0]);
		tmp.add(this.shortToBytes((short)this.getOperationCode())[1]);
		if(this.isPublic) {
			tmp.add(this.shortToBytes((short)1)[1]);
		}
		else {
			tmp.add(this.shortToBytes((short)0)[0]);
		}
		for(int i=0; i<this.sender.getBytes().length; i++)
			tmp.add(this.sender.getBytes()[i]);
		tmp.add(zeroByte.getBytes()[0]);
		for(int i=0 ;i<this.message.getBytes().length; i++)
			tmp.add(this.message.getBytes()[i]);
		tmp.add(zeroByte.getBytes()[0]);
		byte[] toReturn=new byte[tmp.size()];
		for(int i=0; i<tmp.size(); i++)
			toReturn[i]=tmp.get(i);
		return toReturn;
	}

}

package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationRegister extends Operation {
	
	private String name;
	private String password;
	
	public OperationRegister(byte[] msg) {
		super(msg);
		Vector<Byte> userNameVector=new Vector<Byte>();
		Vector<Byte> passwordVector=new Vector<Byte>();
		int i=0;
		while(msg[i]!=0) {
			userNameVector.add(msg[i]);
			i++;
		}
		i++;
		while(msg[i]!=0) {
			passwordVector.add(msg[i]);
			i++;
		}
		byte[] userBytes=new byte[userNameVector.size()];
		byte[] passBytes=new byte[passwordVector.size()];
		for(int t=0; t<userNameVector.size(); t++)
			userBytes[t]=userNameVector.get(t);
		for(int s=0; s<passwordVector.size(); s++)
			passBytes[s]=passwordVector.get(s);
		this.name=new String(userBytes,StandardCharsets.UTF_8);
		this.password=new String(passBytes,StandardCharsets.UTF_8);
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if(!database.checkIfExists(this.name)) {
			database.registerUser(this.name, this.password);
			connections.send(numberOfConnection, new OperationAck(this.getOperationCode()));
			return null;
		}
		connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
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

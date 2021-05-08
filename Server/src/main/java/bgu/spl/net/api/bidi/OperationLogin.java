package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationLogin extends Operation {
	
	private String userName;
	private String password;
	
	public OperationLogin(byte[] msg) {
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
		this.userName=new String(userBytes,StandardCharsets.UTF_8);
		this.password=new String(passBytes,StandardCharsets.UTF_8);
	}

	@Override
	public int getOperationCode() {
		return 2;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if(database.checkPassword(this.userName, this.password)) {
			if(database.checkIfOnline(numberOfConnection)) {
				connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
				return null;
			}
			//If offline send ACK 2 and send the posts/pm's that received while offline.
			database.connectUser(this.userName,numberOfConnection);
			connections.send(numberOfConnection, new OperationAck(this.getOperationCode()));
			List<String> lst1=database.getOfflineMessages(database.getUserName(numberOfConnection));
			for(int i=0; i<lst1.size(); i=i+2) {
				connections.send(numberOfConnection, new OperationNotification(false,lst1.get(i),lst1.get(i+1)));
			}
			List<String> lst2=database.getOfflinePosts(database.getUserName(numberOfConnection));
			for(int i=0; i<lst2.size(); i=i+2)
				connections.send(numberOfConnection, new OperationNotification(true,lst2.get(i),lst2.get(i+1)));
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

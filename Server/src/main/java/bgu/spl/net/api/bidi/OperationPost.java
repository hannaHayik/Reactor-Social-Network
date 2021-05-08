package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
 
import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationPost extends Operation {
	private List<String> lstOfReceivers;
	private String message;

	public OperationPost(byte[] msg) {
		super(msg);
		this.lstOfReceivers=new ArrayList<String>();
		byte[] toWork = new byte[msg.length - 1];
		for (int i = 0; i < msg.length-1; i++)
			toWork[i] = msg[i];
		this.message = new String(toWork, StandardCharsets.UTF_8);
		for (int i = 0; i < this.message.length(); i++) {
			if (this.message.charAt(i) == '@') {
				String nameToAdd = "";
				boolean flag = true;
				while (flag) {
					nameToAdd = nameToAdd + this.message.charAt(i+1);
					i++;
					if(this.message.charAt(i)==' ') {
						nameToAdd=nameToAdd.substring(0, nameToAdd.length()-1);
						flag=false;
					}
				}
				this.lstOfReceivers.add(nameToAdd);
			}
		}
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if (database.checkIfOnline(numberOfConnection)) {
			List<String> listOfFollowers = database.getListOfFollowers(database.getUserName(numberOfConnection));
			//Merge usernames in the post content with the followers list to send it to all in one FOR loop.
			for (int i = 0; i < listOfFollowers.size(); i++)
				this.lstOfReceivers.add(listOfFollowers.get(i));
			for (int i = 0; i < this.lstOfReceivers.size(); i++) {
				if (database.CheckIfOnline(this.lstOfReceivers.get(i)) != -1) {
					connections.send(database.CheckIfOnline(this.lstOfReceivers.get(i)),
							new OperationNotification(true, database.getUserName(numberOfConnection), message));
				} else {
					database.addPostToUser(message, this.lstOfReceivers.get(i),
							database.getUserName(numberOfConnection));
				}
			}
			database.addPostToSystem(database.getUserName(numberOfConnection), this.message);
		} else {
			connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
			return null;
		}
		connections.send(numberOfConnection, new OperationAck(this.getOperationCode()));
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

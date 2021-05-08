package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bgu.spl.net.api.bidi.impl.DataBase;

public class OperationFollow extends Operation {
	private int flip;
	private List<String> names;

	public OperationFollow(byte[] msg) {//Every Operation decodes it's own bytes the way it should.
		super(msg);
		this.names = new ArrayList<String>();
		this.flip = msg[0];
		byte[] numOfUsersBytes = new byte[2];
		numOfUsersBytes[0] = msg[1];
		numOfUsersBytes[1] = msg[2];
		int numberOfUsers = (int) this.bytesToShort(numOfUsersBytes);
		int i = 0;
		int x = 3;
		while (i < numberOfUsers) {
			Vector<Byte> toWork = new Vector<Byte>();
			while (msg[x] != 0) {
				toWork.add(msg[x]);
				x++;
			}
			x++;
			byte[] nameInBytes = new byte[toWork.size()];
			for (int s = 0; s < toWork.size(); s++)
				nameInBytes[s] = toWork.get(s);
			this.names.add(new String(nameInBytes, StandardCharsets.UTF_8));
			i++;
		}

	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public String process(Connections<Operation> connections, int numberOfConnection, DataBase database) {
		if (database.checkIfOnline(numberOfConnection)) {
			if (flip == 0) {//To Follow block.
				List<String> lst = database.followUser(database.getUserName(numberOfConnection), names);
				if (lst.size() > 0) {
					connections.send(numberOfConnection, new OperationAck(this.getOperationCode(), lst.size(), lst));
					return null;
				} else {
					connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
					return null;

				}
			} else {//To Unfollow block
				List<String> lst2 = database.unfollowUser(database.getUserName(numberOfConnection), names);
				if (lst2.size() != 0) {
					connections.send(numberOfConnection, new OperationAck(this.getOperationCode(), lst2.size(), lst2));
					return null;
				} else {
					connections.send(numberOfConnection, new OperationError(this.getOperationCode()));
					return null;

				}
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

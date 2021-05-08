package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.impl.DataBase;
import bgu.spl.net.api.bidi.impl.bidiMessageProtocolImpl;
import bgu.spl.net.srv.Server;

public class TPCMain {
 
	public static void main(String[] args) {
		DataBase db = new DataBase();
		Server.threadPerClient(Integer.parseInt(args[0]), () -> new bidiMessageProtocolImpl(db), MessageEncoderDecoderImpl::new).serve();

	}

}

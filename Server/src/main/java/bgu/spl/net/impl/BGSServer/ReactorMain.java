package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.impl.DataBase;
import bgu.spl.net.api.bidi.impl.bidiMessageProtocolImpl;
import bgu.spl.net.srv.Server;
 
public class ReactorMain {

	public static void main(String[] args) {
		DataBase db = new DataBase();
		Server.reactor(Integer.parseInt(args[1]), Integer.parseInt(args[0]), () -> new bidiMessageProtocolImpl(db),
				MessageEncoderDecoderImpl::new).serve();

	}

}

package bgu.spl.net.impl.newsfeed;

import java.nio.charset.StandardCharsets;

import bgu.spl.net.api.bidi.impl.bidiMessageProtocolImpl;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;


@SuppressWarnings("unused")
public class NewsFeedServerMain {
	
	public static short bytesToShort(byte[] byteArr)
	{
	    short result = (short)((byteArr[0] & 0xff) << 8);
	    result += (short)(byteArr[1] & 0xff);
	    return result;
	}
	
	public static byte[] shortToBytes(short num)
	{
	    byte[] bytesArr = new byte[2];
	    bytesArr[0] = (byte)((num >> 8) & 0xFF);
	    bytesArr[1] = (byte)(num & 0xFF);
	    return bytesArr;
	}

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        NewsFeed feed = new NewsFeed(); //one shared object

// you can use any server... 
//        Server.threadPerClient(
//                7777, //port
//                () -> new BidiMessagingProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();
        

    }
}

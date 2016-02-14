package uk.aplifi.pusher.android;

import uk.aplifi.pusher.android.logging.LoggingHandler;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;

public class PusherAdapter {

	private static final String TAG = "PusherAdapter";
	private Pusher pusher;
	
	public PusherAdapter() {
	}
	
	public void setup(String key, String authUrl) {
		LoggingHandler.instance().log(TAG, "setup ");
		
		HttpAuthorizer2 authorizer = new HttpAuthorizer2(authUrl);
		PusherOptions options = new PusherOptions().setAuthorizer(authorizer).setEncrypted(true);
		pusher = new Pusher(key, options);
	}
	
	public void connect(ConnectionEventListener connectionEventListener) {
		LoggingHandler.instance().log(TAG, "connect ");
		pusher.connect(connectionEventListener, ConnectionState.ALL);
	}
	
	public void disconnect() {
		LoggingHandler.instance().log(TAG, "disconnect ");
		pusher.disconnect();
	}
	
	public Channel subscribeChannel(String channelName) {
		LoggingHandler.instance().log(TAG, "subscribeChannel ");
		return pusher.subscribe(channelName);
	}
	
	public PrivateChannel subscribePrivateChannel(String channelName) {
		LoggingHandler.instance().log(TAG, "subscribeChannel ");
		return pusher.subscribePrivate(channelName);
	}
	
	public void unsubscribeChannel(String channelName) {
		LoggingHandler.instance().log(TAG, "unsubscribeChannel ");
		pusher.unsubscribe(channelName);
	}
}

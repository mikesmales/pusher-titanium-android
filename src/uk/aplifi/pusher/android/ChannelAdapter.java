package uk.aplifi.pusher.android;

import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.json.JSONException;
import org.json.JSONObject;

import uk.aplifi.pusher.android.logging.LoggingHandler;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;

@Kroll.proxy(creatableInModule = PusherTitaniumAndroidModule.class)
public class ChannelAdapter extends KrollProxy {

	private static final String TAG = "ChannelAdapter";
	private Channel channel;
	private HashMap<String, SubscriptionEventListenerAdapter> events = new HashMap<String, SubscriptionEventListenerAdapter>();
	
	public ChannelAdapter() {
		super();
	}
	
	public void setup(Channel channel) {
		this.channel = channel;
	}
	
	
	@Kroll.method
	public void sendEvent(String eventName, String payload) {
		LoggingHandler.instance().log(TAG, "sendEvent " + eventName);
		((PrivateChannel) this.channel).trigger(eventName, payload);
	}
	
	private class SubscriptionEventListenerAdapter implements PrivateChannelEventListener {
		
		KrollFunction callback;
		
		public SubscriptionEventListenerAdapter(KrollFunction callback) {
			this.callback = callback;
		}
		
		@Override
	    public void onEvent(String channel, String event, String data) {
	    	LoggingHandler.instance().log(TAG, "Received event with data: " + data);
	    	notifyListeners(this.callback, data);
	    }

		@Override
		public void onSubscriptionSucceeded(String arg0) {
			LoggingHandler.instance().log(TAG, "onSubscriptionSucceeded : " );
			
			if (arg0 != null)
			LoggingHandler.instance().log(TAG, "onSubscriptionSucceeded : " + arg0);
		}

		@Override
		public void onAuthenticationFailure(String arg0, Exception e) {
			LoggingHandler.instance().log(TAG, "onAuthenticationFailure : " + arg0);
			
			if (arg0 != null)
			LoggingHandler.instance().log(TAG, "onAuthenticationFailure : " + arg0);
			
			if (e != null)
			LoggingHandler.instance().log(TAG, "e" + e.getMessage());
			
		}
		
	};
	
	
	@Kroll.method
	public void addEventListener(String eventName, KrollFunction callback) {
		
		LoggingHandler.instance().log(TAG, "addEventListener " + eventName);
		
		SubscriptionEventListenerAdapter listener = new SubscriptionEventListenerAdapter(callback);
		channel.bind(eventName, listener);
		events.put(eventName, listener);
	}
	
	private void notifyListeners(KrollFunction callback, String payload) {
	    
		LoggingHandler.instance().log(TAG, "notifyListeners " + payload);
		JSONObject json;
		try {
			json = new JSONObject(payload);
		} catch (JSONException e) {
			LoggingHandler.instance().log(TAG, "not json " + e.getMessage());
			return;
		}
		
		if (callback != null) {
			KrollDict data = new KrollDict();
	        data.put("data", KrollDict.fromJSON(json));
	        
	        callback.call(getKrollObject(), data);
	    }
	}
	
	@Kroll.method
	public void removeEventListener(String eventName, KrollFunction callback) {
		
		LoggingHandler.instance().log(TAG, "removeEventListener " + eventName);
		
		if (events.containsKey(eventName)) {
			
			LoggingHandler.instance().log(TAG, "removeEventListener 1 ");
			SubscriptionEventListenerAdapter listener = events.get(eventName);
			channel.unbind(eventName, listener);
			events.remove(eventName);
		}
		
	}
	
	@Kroll.method
	public void removeEventListener(String eventName) {
		
		LoggingHandler.instance().log(TAG, "** removeEventListener " + eventName);
		
		if (events.containsKey(eventName)) {
			
			LoggingHandler.instance().log(TAG, "** removeEventListener 1 ");
			SubscriptionEventListenerAdapter listener = events.get(eventName);
			channel.unbind(eventName, listener);
			events.remove(eventName);
		}
		
	}
}

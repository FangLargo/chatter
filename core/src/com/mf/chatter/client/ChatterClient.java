package com.mf.chatter.client;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mf.chatter.Settings;
import com.mf.chatter.server.ServerPacket.*;

public class ChatterClient {

	public static Client client;
	
	public static void initChatterClient(String address) throws IOException {
		client = new Client();
		registerPackets();

		ClientListener listener = new ClientListener();
		listener.init(client);
		client.addListener(listener);
		client.setKeepAliveTCP(300);
		new Thread (client).start();
		
		client.connect(500000, address, 8486, 8487);
	}
	
	public static void initChatterClient(InetAddress address) throws IOException {
		client = new Client();
		registerPackets();

		ClientListener listener = new ClientListener();
		listener.init(client);
		client.addListener(listener);
		client.setKeepAliveTCP(300);
		new Thread (client).start();
		
		client.connect(500000, address, 8486, 8487);
	}
	
	public static void initChatterClient() throws IOException {
		client = new Client();
		registerPackets();

		ClientListener listener = new ClientListener();
		listener.init(client);
		client.addListener(listener);
		client.setKeepAliveTCP(300);
		new Thread (client).start();
		
		//client.discoverHost(8487, 10000);
		
		client.connect(500000, client.discoverHost(8487, 10000), 8486, 8487);
	}
	
	
	private static void registerPackets() {
		Kryo kryo = client.getKryo();
		
		kryo.register(Name.class);
		kryo.register(Message.class);
	}
	
	public static void sendMessage(String message) {
		Message m = new Message();
		m.name = Settings.name;
		m.content = message;
		
		client.sendTCP(m);
	}
	
}

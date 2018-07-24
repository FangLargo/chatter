package com.mf.chatter.server;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import com.mf.chatter.server.ServerPacket.*;

public class ChatterServer {
	public static Server server;

	public static void initChatterServer() throws IOException {
		server = new Server();
		registerPackets();
		ServerListener listener = new ServerListener();
		listener.init(server);
		
		server.addListener(listener);
		server.bind(8486, 8487);
		server.start();

	}

	private static void registerPackets() {
		Kryo kryo = server.getKryo();

		kryo.register(Name.class);
		kryo.register(Message.class);
	}
}

package com.mf.chatter.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mf.chatter.server.ServerPacket.*;

public class ServerListener extends Listener {
private Server server;
	
	public void init(Server server) {
		this.server = server;
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof Name) {
			server.sendToAllTCP(object);
		}
		if(object instanceof Message) {
			server.sendToAllTCP(object);
		}


		super.received(connection, object);
	}
}

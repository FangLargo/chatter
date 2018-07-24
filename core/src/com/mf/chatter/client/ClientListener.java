package com.mf.chatter.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mf.chatter.ChatLog;
import com.mf.chatter.Settings;
import com.mf.chatter.server.ServerPacket.*;

public class ClientListener extends Listener{
private Client client;
	
	public void init(Client client) {
		// TODO Auto-generated method stub
		this.client = client;
	}
	
	@Override
	public void connected(Connection connection) {
		// TODO Auto-generated method stub

		Name name = new Name();
		name.content = Settings.name;
		name.connecting = true;
		client.sendTCP(name);
		
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		// TODO Auto-generated method stub
		
		if(object instanceof Name) {
			//System.out.println(((Name) object).content);
			boolean con = ((Name) object).connecting;
			if(con){
				ChatLog.messages.add(((Name) object).content + " has connected");
			}
			else {
				ChatLog.messages.add(((Name) object).content + " has disconnected");
			}
		}
		if(object instanceof Message) {
			String name = ((Message) object).name;
			String content = ((Message) object).content;
			String Log = name + ": " + content;
			ChatLog.messages.add(Log);
		}
		
		super.received(connection, object);
	}
}

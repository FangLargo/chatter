package com.mf.chatter.server;

public class ServerPacket {
	public static class Name {
		public String content;
		public boolean connecting;
	}
	public static class Message {
		public String name;
		public String content;
	}
}

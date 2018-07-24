package com.mf.chatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mf.chatter.client.ChatterClient;
import com.mf.chatter.server.ChatterServer;

public class MainScreen implements Screen{
final Chatter game;
	
	OrthographicCamera camera;
	
	float screenHeight = 800;
	float pixelsPerUnit = Gdx.graphics.getHeight()/screenHeight;
	float screenWidth = Gdx.graphics.getWidth()/pixelsPerUnit;
	
	//Settings settings;
	Array<String> messages;
	
	Stage stage;
	Skin skin;
	Table table;
	
	
	ScrollPane messagesPane;
	Table messagesTable;
	TextField messageField;
	TextButton submitMessageButton;
	TextButton backButton;
	
	
	TextField nameField;
	TextField ipField;
	TextButton submitIPButton;
	TextButton connectButton;
	TextButton serverButton;
	Label errorLabel;
	
	ChatterServer chatterServer;
	ChatterClient chatterClient;
	
	Json json = new Json();
	
	public MainScreen (Chatter gam) {
		game = gam;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		camera.update();
		
		stage = new Stage(new ExtendViewport(400, 600, camera));
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		
		ChatLog.messages = new Array<String>();
		
		initResources();
		
		setupMenu();
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void initResources() {
		table = new Table(skin);
		
		ipField = new TextField("", skin);
		
		submitIPButton = new TextButton("Connect to IP", skin);
		submitIPButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				captureSettings();
				if (ipField.getText() != "") {
					try {
						ChatterClient.initChatterClient(ipField.getText());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				setupChat();
				return false;
		 	}
		});
		
		nameField = new TextField("Name", skin);
		
		connectButton = new TextButton("Connect to random", skin);
		connectButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				//settings = new Settings(nameField.getText(), ipField.getText());
				captureSettings();

				try {
					ChatterClient.initChatterClient();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				setupChat();
				return false;
		 	}
		});
		
		serverButton = new TextButton("Create Server", skin);
		serverButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				//System.out.println("Start server");
				captureSettings();
				try {
//					chatterServer = new ChatterServer();
//					chatterClient = new ChatterClient("127.0.0.1");
					
					ChatterServer.initChatterServer();
					ChatterClient.initChatterClient("127.0.0.1");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setupChat();
				return false;
		 	}
		});
		
		stage.addActor(table);

		messagesTable = new Table(skin);
		

		messagesPane = new ScrollPane(messagesTable, skin);

		
		//messagesTable.add("Yo");
//		Name name = new Name();
//		name.content = "Wombat";
//		ChatterServer.server.sendToAllTCP(name);
		
		messageField = new TextField("", skin);

		submitMessageButton = new TextButton("Submit", skin);
		submitMessageButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				ChatterClient.sendMessage(messageField.getText());
				messageField.setText("");
				
				return false;
			}
		});
		
		backButton = new TextButton("Back", skin);
		backButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if(ChatterServer.server != null) {
					ChatterServer.server.stop();
				}
				ChatterClient.client.stop();
				
				lastLength = 0;
				ChatLog.messages.clear();
				
				messagesTable.clear();
				
				
				setupMenu();
				
				return false;
			}
		});
	}
	
	private void setupMenu() {
		
		table.clear();
		table.setDebug(true);
		table.setFillParent(true);
		table.align(Align.center);

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			try {
				table.add(InetAddress.getLocalHost().toString()).colspan(2);
				table.row();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (Gdx.app.getType() == ApplicationType.Android) {
			//Do Stuff Later
		}
		
		table.add("Name");
		table.add(nameField).height(40);
		table.row();
		
		table.add(ipField).height(40);
		table.add(submitIPButton).height(40);
		table.row();
		
		table.add(connectButton).colspan(2).height(50);
		table.row();
		
		table.add(serverButton).colspan(2).height(50);
		table.row();
		
//		if(Gdx.files.local("settings/settings.cfg").exists()) {
//			System.out.println(json.fromJson(Settings.class, Gdx.files.local("settings/settings.cfg")).name);
//			//Settings.ip = json.fromJson(Settings.class, Gdx.files.local("settings/settings.cfg")).ip;
//			
//			//nameField.setText(Settings.name);
//			//ipField.setText(Settings.ip);
//		}
//		else {
//
//		}
	}

	
	private void captureSettings() {
		Settings.name = nameField.getText();
		Settings.ip = ipField.getText();
		
//		Settings settings = new Settings();
//		settings.name = Settings.name;
//		settings.ip = Settings.ip;
		
//		FileHandle file = Gdx.files.local("settings/settings.cfg");
//		json.setUsePrototypes(false);
//		file.writeString(json.prettyPrint(Settings.class), false);
	}

	private void setupChat() {

		ChatLog.messages.clear();
		
		table.clear();
		table.setDebug(true);
		table.setFillParent(true);
		table.align(Align.center);
		
		//messages = new Array<String>();
		
		messagesTable.setFillParent(false);
		messagesTable.align(Align.topLeft);
		
		
		table.add(messageField).width(300);
		table.add(submitMessageButton).height(35);
		table.row();
		
		table.add(messagesPane).width(300 + submitMessageButton.getWidth()).colspan(2).height(400);
		table.row();
		
		table.add(backButton).colspan(2).width(160).height(35);
		table.row();

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			try {
				table.add(InetAddress.getLocalHost().toString()).colspan(2);
				table.row();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	int lastLength = 0;
	
	public void updateChatLog() {
		if (ChatLog.messages.size > lastLength) {

//			messagesTable.clear();
//			for (int i = 0; i < ChatLog.messages.size; i++) {
//				messagesTable.add(ChatLog.messages.get(i)).align(Align.left);
//				messagesTable.row();
//			}
			messagesTable.add(ChatLog.messages.get(lastLength)).align(Align.left);
			messagesTable.row();
			messagesPane.layout();
			messagesPane.setScrollPercentY(100);
			
			lastLength = ChatLog.messages.size;
		}
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		//System.out.println(ChatLog.messages.size);
		
		if(messagesTable != null) {
			updateChatLog();
		}
		//updateChatLog();
		
		stage.act(delta);
		stage.draw();

	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}

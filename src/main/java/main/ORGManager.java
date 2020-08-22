package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import admin.HttpAdapter;
import commands.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;

public class ORGManager {
	
	private final String TOKEN;
	private final String UPKEY;
	private final String UPPRIVATE;

	private CommandClientBuilder builder;
	private CommandClient client;
	public static HttpAdapter httpAdapter;
	public static Map<String, Integer> serverMap = new HashMap<>();
	
	public ORGManager(String token, String UPKey, String UPPrivate) {
		this.TOKEN = token;
		this.UPKEY = UPKey;
		this.UPPRIVATE = UPPrivate;
	}
	
	public void start() {
		try {
			if(Files.exists(Paths.get((System.getProperty("user.dir") + "/resources")))) {
				Scanner s = new Scanner(new File(System.getProperty("user.dir") + "/resources/serverlist.cfg"));
				while(s.hasNextLine()) {
					String line = s.nextLine();
					serverMap.put(line.split(";")[0], Integer.parseInt(line.split(";")[1]));
				}
			} else {
				Files.createDirectory(Paths.get((System.getProperty("user.dir") + "/resources")));
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Creando archivo de servidores");
			try {
				Files.createFile(Paths.get(System.getProperty("user.dir") + "/resources/serverlist.cfg"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		builder = new CommandClientBuilder();
		httpAdapter = new HttpAdapter(this.UPKEY, this.UPPRIVATE);
		
		builder = new CommandClientBuilder();
		builder.setActivity(Activity.playing("UNPlayer - GTA V"));
		builder.setPrefix("UP.");
		builder.setAlternativePrefix("up.");
		builder.addCommands(
				//Comandos
				new Org(),
				new Yo(),
				new Usuario()
				);
		builder.setOwnerId("137013304961794048");
		client = builder.build();
		
		try {
			JDABuilder.create(EnumSet.allOf(GatewayIntent.class))
				.setToken(this.TOKEN)
				.addEventListeners(client, new Listener())
				.build();
		} catch (LoginException e) {
			System.out.println("Hubo un error en el login.");
		}
	}
	
}

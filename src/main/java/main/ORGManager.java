package main;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import admin.DBAdapter;
import admin.HttpAdapter;
import commands.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;

public class ORGManager {
	
	private final String TOKEN;
	//private final String UPKEY;
	//private final String UPPRIVATE;

	private CommandClientBuilder builder;
	private CommandClient client;
	public static HttpAdapter httpAdapter;
	public static DBAdapter dbAdapter;
	//public static Map<String, Integer> serverMap = new HashMap<>();
	
	public ORGManager(String token, String dbpwd) {
		this.TOKEN = token;
		dbAdapter = new DBAdapter(dbpwd);
		//this.UPKEY = UPKey;
		//this.UPPRIVATE = UPPrivate;
	}
	
	public void start() {
		builder = new CommandClientBuilder();
		httpAdapter = new HttpAdapter(/*this.UPKEY, this.UPPRIVATE*/);
		
		builder = new CommandClientBuilder();
		builder.setActivity(Activity.playing("UNPlayer - GTA V"));
		builder.setPrefix("UP.");
		builder.setAlternativePrefix("up.");
		builder.addCommands(
				//Comandos
				new Org(),
				new Yo(),
				new Usuario(),
				new Actualizar(),
				new ActualizarNombres(),
				new Online()
				);
		builder.setOwnerId("137013304961794048");
		client = builder.build();
		
		try {
			JDABuilder.create(EnumSet.allOf(GatewayIntent.class))
				.setToken(this.TOKEN)
				.addEventListeners(client, new Listener())
				.build();
			System.out.println("------------------------------------------BOT LISTO------------------------------------------");
		} catch (LoginException e) {
			System.out.println("Hubo un error en el login.");
		}
		
	}
	
}

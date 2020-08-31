package commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import main.ORGManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();
		System.out.println("Bot se uni� a guild " + guild.getId());
		TextChannel channel = guild.createTextChannel("ORG-Manager-Temp")
				.addPermissionOverride(guild.getPublicRole(), 0, Permission.VIEW_CHANNEL.getRawValue()).complete();

		EmbedBuilder eb = new EmbedBuilder();
		eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
		eb.setTitle("Bienvenida");
		String desc = "\u00A1Bienvenido/a a **ORG Manager**!\n\nEste bot te va a ayudar a llevar un manejo mucho m\u00E1s organizado "
				+ "del servidor de Discord de tu ORG, permiti\u00E9ndote automatizar la gesti\u00F3n de nombres y rangos de acuerdo a la informaci\u00F3n real del juego"
				+ "\n\n**IMPORTATE**: Actualmente s\u00F3lo funciona con ORGs de GTA V";
		eb.setDescription(desc);
		channel.sendMessage(eb.build()).complete();

		eb.setTitle("Organizaci\u00F3n");
		eb.setDescription(
				"Para comenzar y registrar tu ORG, ingresa al siguiente enlance: *https://unplayer.com/gtav/orgs*\nAnot\u00E1 el n\u00FAmero de tu ORG en la cual sos l\u00EDder y va a pertenecer este servidor");
		channel.sendMessage(eb.build()).complete();

		eb.setDescription("");
		eb.setImage("https://i.imgur.com/xuF3aIB.png");
		channel.sendMessage(eb.build()).complete();
		
		eb.setImage(null);
		eb.setDescription(
				"Una vez que tengas el n\u00FAmero, escribime por este canal dicho n\u00FAmero de la siguiente forma:\n\n`UP.org 130`\n\nY reemplaz\u00E1 el 130 por el n\u00FAmero de tu org");
		channel.sendMessage(eb.build()).complete();
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		System.out.println("Bot sali� de la guild " + event.getGuild().getId());
		try {
			ORGManager.dbAdapter.removeServer(event.getGuild().getId());
		} catch (SQLException e) {
			System.out.println("Hubo un error el servidor de la BD");
			e.printStackTrace();
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(event.getMember().getUser().isBot()) return;
		Guild guild = event.getGuild();
		String id = event.getMember().getId();
		int orgId = -1;
		try {
			ResultSet rs = ORGManager.dbAdapter.getServer(id);
			if(rs.next()) {
				orgId = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		
		int rank = ORGManager.httpAdapter.getMemberRank(id, orgId);
		JSONArray rankArray = ORGManager.httpAdapter.requestORGRanks(orgId);

		Role role = null;

		for (Object object : rankArray) {
			JSONObject jsobj = (JSONObject) object;
			if (rank == jsobj.getInt("idx")) {
				role = guild.getRolesByName(jsobj.getString("name"), true).get(0);
			}
		}
		if (role == null) {
			role = guild.getRolesByName("Invitado", true).get(0);
		}

		String name = ORGManager.httpAdapter.requestUserName(id);

		if (name.equals("N/A")) {
			event.getMember().modifyNickname("[No registrado] " + event.getMember().getEffectiveName()).queue();
		} else if(role == null){
			event.getMember().modifyNickname("[Invitado] " + name).queue();
		} else {
			event.getMember().modifyNickname(name).queue();
		}
		System.out.print("Se cambi� nombre a " + event.getMember().getEffectiveName() + " - ");
		
		

		event.getGuild().addRoleToMember(id, role).queue();
		System.out.println("Se a�adi� el rol " + role.getName() + " al usuario");
	}

}

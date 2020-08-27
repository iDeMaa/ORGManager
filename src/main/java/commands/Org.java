package commands;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ORGManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

public class Org extends Command{

	public Org() {
		this.name = "Org";
		this.help = "Setea la organizaci\u00F3n a la cual va a pertenecer la ORG";
		this.guildOnly = true;
		this.aliases = new String[] {"org"};
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!event.getChannel().getName().equals("org-manager-temp")) return;
		Guild guild = event.getGuild();
		int id = -1;
		try {
			id = Integer.parseInt(event.getArgs());
		} catch(NumberFormatException e) {
			event.reply("El ID ingresado es incorrecto. Por favor, ingrese \u00FAnicamente el n\u00FAmero de su ORG");
			return;
		}
		
		int rank = ORGManager.httpAdapter.getMemberRank(event.getMember().getId(), id);
		if(rank == 0 || rank == 1) {
			if(ORGManager.dbAdapter.orgExists(id) == true) {
				event.reply("Esta ORG ya est\u00E1 vinculada con un servidor de Discord. Si esto es un error, por favor contactate con `DeMaa#1038`");
				return;
			}
			
			try {
				ORGManager.dbAdapter.addServer(guild.getId(), id, event.getMember().getUser().getAsTag(), event.getMember().getEffectiveName());
				//ORGManager.serverMap.put(guild.getId(), id);
			} catch (SQLException e) {
				event.reply("Hubo un error al intentar vincular la ORG. Contacta a `DeMaa#1038` en caso de que el error persista");
				e.printStackTrace();
				return;
			}
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("\u00A1Listo!");
			eb.setDescription("\u00A1La ORG `" + id + "` fue vinculada con el servidor correctamente!\n\nAhora el bot comenzar\u00E1 a crear los roles autom\u00E1ticamente. Esto puede tardar unos minutos");
			eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
			event.reply(eb.build());
			createRoles(event, id);
		} else {
			event.reply("\u00FAnicamente el lider o sub-lideres de la ORG pueden vincularla con un servidor de Discord");
			return;
		}
	}
	
	private void createRoles(CommandEvent event, int id) {
		Guild guild = event.getGuild();
		JSONArray array = ORGManager.httpAdapter.requestORGRanks(id);
		for(Object object : array) {
			JSONObject jsobj = (JSONObject) object;
			String name = jsobj.getString("name");
			if(guild.getRolesByName(name, false).size() == 0) {
				int idx = jsobj.getInt("idx");
				if(idx>1) {
					guild.createRole().setName(name).queue();
				} else {
					guild.createRole().setName(name).setPermissions(Permission.ADMINISTRATOR).queue();
				}
			}
		}
		if(guild.getRolesByName("Invitado", false).size() == 0) guild.createRole().setName("Invitado").queue();
		guild.getTextChannelsByName("org-manager-temp", true).get(0).delete().complete();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("\u00A1Completado!");
		eb.setDescription("\u00A1La organizaci\u00F3n ya est\u00E1 vinculada al servidor y est\u00E1 todo listo para empezar a funcionar!");
		eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
		guild.getDefaultChannel().sendMessage(eb.build());
		System.out.println("Se vinculo la org " + id + " al servidor de Discord " + guild.getId());
	}

}

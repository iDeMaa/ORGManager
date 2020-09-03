package commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ORGManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class Online extends Command {

	public Online() {
		this.name = "Online";
		this.guildOnly = true;
		this.help = "Muestra a los miembros de la ORG conectados en el juego";
		this.aliases = new String[]{"online"};
	}

	@Override
	protected void execute(CommandEvent event) {
		int orgId = -1;

		try {
			ResultSet rs = ORGManager.dbAdapter.getServer(event.getGuild().getId());
			if (rs.next()) {
				orgId = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
		
		int cont = 0;
		for(Member member : event.getGuild().getMembers()) {
			if(!member.getUser().isBot() && !member.getRoles().get(0).getName().equals("Invitado")) {
				String id = member.getId();
				JSONObject js = ORGManager.httpAdapter.requestUserData(id);
				if(js.getBoolean("online")) {
					eb.appendDescription("[" + member.getEffectiveName() + "](https://foro.unplayer.com/member.php/" + ORGManager.httpAdapter.requestUserId(id, "forum") + ")\n");
					cont++;
				}
			}
		}
		if(cont != 0) {
			eb.setTitle(cont + " miembros de " + ORGManager.httpAdapter.requestORGName(orgId) + " conectados");
		} else {
			eb.setTitle("No hay miembros de " + ORGManager.httpAdapter.requestORGName(orgId) + " conectados");
		}
		
		event.reply(eb.build());
	}
}

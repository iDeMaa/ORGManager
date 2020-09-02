package commands;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import main.ORGManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class Usuario extends Command{

	public Usuario() {
		this.name = "Usuario";
		this.cooldown = 360;
		this.guildOnly = true;
		this.help = "Muestra informaci\u00F3n sobre el usuario taggeado";
		this.aliases = new String[] {"usuario"};
	}
	
	@Override
	protected void execute(CommandEvent event) {
		Member member = event.getMessage().getMentionedMembers().get(0);
		JSONObject js = ORGManager.httpAdapter.requestUserData(member.getId());
		
		if(js == null) return;
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setThumbnail("https://i.imgur.com/x9SxBMU.jpg");
		String name = js.getString("name");
		String forumLink = "https://foro.unplayer.com/member.php/" + ORGManager.httpAdapter.requestUserId(member.getId(), "forum");
		eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
		eb.setAuthor(name + " - Informaci\u00F3n de usuario", forumLink);
		String nivel = js.get("level").toString();
		eb.addField("Nivel:", nivel, true);
		int played_time = js.getInt("played_time");
		String played_time_Str = played_time/24/60 + "d " + played_time/60%24 + "h " + played_time%60 + "m";
		eb.addField("Tiempo jugado: ", played_time_Str, true);
		int certification = js.getInt("certification");
		switch(certification) {
			case 0:
				eb.addField("Certificaci\u00F3n:", "Ninguna", true);
				break;
			case 1:
				eb.addField("Certificaci\u00F3n:", "B\u00E1sica", true);
				break;
			case 2:
				eb.addField("Certificaci\u00F3n:", "Normal", true);
				break;
			case 3:
				eb.addField("Certificaci\u00F3n:", "Completa", true);
				break;
		}
		boolean banned = js.getBoolean("banned");
		eb.addField("Banneado: ", banned ? "Si":"No", true);
		boolean estado = js.getBoolean("online");
		if(estado) {
			eb.addField("Estado:", "En l\u00EDnea", true);
		}else {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date(js.getInt("last_seen") * 1000L);
			eb.addField("\u00DAltima vez:", format.format(date), true);
		}
		
		event.reply(eb.build());
		eb.clearFields();
		
		eb.setAuthor(name + " - Organizaciones", forumLink);
		JSONArray array = js.getJSONArray("orgs");
		for(Object obj : array) {
			JSONObject jsobj = (JSONObject) obj;
			int orgId = jsobj.getInt("org_id");
			String orgName = ORGManager.httpAdapter.requestORGName(orgId);
			int orgRank = jsobj.getInt("idx");
			String orgRankName = ORGManager.httpAdapter.requestORGRankName(orgId, orgRank);
			eb.addField(orgName, orgRankName, false);
		}
		event.reply(eb.build());
		System.out.println("Se realizo busqueda al usuario " + member.getEffectiveName());
	}

}

package commands;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ORGManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Actualizar extends Command{

	public Actualizar() {
		this.name = "Actualizar";
		this.help = "Actualiza los rangos de los usuarios en el Discord";
		this.guildOnly = true;
		this.aliases = new String[] {"actualizar"};
		this.cooldown = 3600;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		List<Member> members = event.getGuild().getMembers();
		for(Member member : members) {
			String discordId = member.getId();
			int orgId = -1;
			
			try {
				orgId = ORGManager.dbAdapter.getServer(discordId).getInt(2);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			JSONArray rankArray = ORGManager.httpAdapter.requestORGRanks(orgId);
			
			int rank = ORGManager.httpAdapter.getMemberRank(discordId, orgId);
			Role userRole = member.getRoles().get(0);
			String rankName = null;
			
			if(rank != -1) {
				rankName = rankArray.getJSONObject(rank).getString("name");
			} else {
				rankName = "Invitado";
			}
			 
			if(!userRole.getName().equals(rankName)) {
				if(userRole.getName().equals("Invitado")) {
					event.getGuild().modifyNickname(member, member.getEffectiveName().split(" ")[0]);
				}
				event.getGuild().removeRoleFromMember(member, userRole);
				event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName(rankName, true).get(0));
				if(rank == -1) {
					event.getGuild().modifyNickname(member, "[Invitado] " + member.getEffectiveName());
				}
			}
		}
	}

}

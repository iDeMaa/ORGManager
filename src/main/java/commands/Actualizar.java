package commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ORGManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class Actualizar extends Command {

	public Actualizar() {
		this.name = "Actualizar";
		this.help = "Actualiza los rangos de los usuarios en el Discord";
		this.guildOnly = true;
		this.aliases = new String[] { "actualizar", "update" };
		this.cooldown = 3600;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR))
			return;
		boolean hasChanges = false;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setFooter("Programado por DeMaa#1038/Thomas_Lawrence", "https://i.imgur.com/x9SxBMU.jpg");
		System.out.println("Inicio de actualización de servidor " + event.getGuild().getId());
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
		JSONArray rankArray = ORGManager.httpAdapter.requestORGRanks(orgId);

		List<Member> members = event.getGuild().getMembers();
		for (Member member : members) {
			System.out.println("Procesando a " + member.getEffectiveName());
			if (!member.getUser().isBot()) {
				String discordId = member.getId();

				eb.setTitle(
						"Actualización de rangos en " + ORGManager.httpAdapter.requestORGName(orgId) + " finalizada");

				int rank = ORGManager.httpAdapter.getMemberRank(discordId, orgId);
				Role userRole = member.getRoles().get(0);
				String rankName = null;
				if (rank != -1) {
					rankName = rankArray.getJSONObject(rank).getString("name");
				} else {
					rankName = "Invitado";
				}
				System.out.println(userRole.getName() + " - " + rankName);
				if (!userRole.getName().equals(rankName)) {
					if (member.getEffectiveName().contains("Invitado")) {
						event.getGuild().modifyNickname(member, member.getEffectiveName().split(" ")[1]).complete();
						System.out.println("Se cambió el nombre al nuevo miembro de la ORG");
					}
					try {
						event.getGuild().removeRoleFromMember(member, userRole).complete();
						event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName(rankName, true).get(0))
								.queue();
						eb.addField(member.getEffectiveName(), userRole.getName() + " -> " + rankName, false);
						hasChanges = true;
					} catch (HierarchyException e) {
						event.reply(
								"El rol de **ORG Manager** debe ser el primero en la lista del servidor. Por favor, cambialo y vuelve a ejecutar el comando**");
						e.printStackTrace();
						return;
					}

					if (rank == -1) {
						event.getGuild().modifyNickname(member, "[Invitado] " + member.getEffectiveName()).queue();
						;
					}
					System.out.println("Se actualizó el rango de " + member.getEffectiveName());
				}
			}

		}
		if (!hasChanges) {
			eb.setDescription("No hay rangos que actualizar");
		}
		event.reply(eb.build());
		System.out.println("Fin de la actualización del servidor " + event.getGuild().getId());
	}

}

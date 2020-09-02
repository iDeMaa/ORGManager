package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import main.ORGManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class ActualizarNombres extends Command{
	
	public ActualizarNombres() {
		this.name = "ActualizarNombres";
		this.help = "Actualiza los nombres de los miembros de la ORG en caso de no coincidir con el actual";
		this.guildOnly = true;
		this.aliases = new String[] {"actualizarnombres", "Actualizarnombres", "ActualizarNombres", "updatenames", "Updatenames", "UpdateNames"};
	}

	@Override
	protected void execute(CommandEvent event) {
		System.out.println("Actualizando nombres del servidor " + event.getGuild().getId());
		for(Member member : event.getGuild().getMembers()) {
			if(!member.getUser().isBot()) {
				String name = ORGManager.httpAdapter.requestUserName(member.getId());
				if(!member.getEffectiveName().equals(name)){
					try {
						if (name.equals("N/A")) {
							event.getMember().modifyNickname("[No registrado] " + event.getMember().getEffectiveName()).queue();
						} else {
							event.getMember().modifyNickname(name).queue();
						}
					} catch (HierarchyException e) {
						event.reply("El rol de **ORG Manager** debe ser el primero en la lista del servidor. Por favor, cambialo y vuelve a ejecutar el comando**");
					}
					System.out.print("Se cambió nombre a " + event.getMember().getEffectiveName() + " - ");
				}
			}
		}
	}

}

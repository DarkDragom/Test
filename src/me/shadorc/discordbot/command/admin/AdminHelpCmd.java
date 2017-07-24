package me.shadorc.discordbot.command.admin;

import java.awt.Color;

import me.shadorc.discordbot.Command;
import me.shadorc.discordbot.Context;
import me.shadorc.discordbot.utility.BotUtils;
import sx.blah.discord.util.EmbedBuilder;

public class AdminHelpCmd extends Command {

	public AdminHelpCmd() {
		super(true, "admin_help");
	}

	@Override
	public void execute(Context context) {
		//EmbedBuilder doc : https://discord4j.readthedocs.io/en/latest/Making-embedded-content-using-EmbedBuilder/
		EmbedBuilder builder = new EmbedBuilder();

		builder.withAuthorName("Shadbot Admin Aide");
		builder.withAuthorIcon(context.getClient().getOurUser().getAvatarURL());
		builder.withColor(new Color(170, 196, 222));
		builder.withDesc("Aide pour les commandes administrateurs. Pour plus d'informations, utilisez /help <commande>.");
		builder.appendField("Commandes :",
				"`/allows_channel <channel | all>`"
						+ "\n`/quit`", false);

		BotUtils.sendEmbed(builder.build(), context.getChannel());		
	}

}
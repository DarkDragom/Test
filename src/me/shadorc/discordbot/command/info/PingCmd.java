package me.shadorc.discordbot.command.info;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.command.AbstractCommand;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.utils.BotUtils;
import me.shadorc.discordbot.utils.Utils;
import sx.blah.discord.util.EmbedBuilder;

public class PingCmd extends AbstractCommand {

	public PingCmd() {
		super(Role.USER, "ping");
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		long ping = Math.abs(ChronoUnit.MILLIS.between(LocalDateTime.now(), context.getMessage().getCreationDate()));
		BotUtils.sendMessage(Emoji.GEAR + " Ping: " + ping + "ms", context.getChannel());
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = Utils.getDefaultEmbed(this)
				.appendDescription("**Show Shadbot's ping.**");
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}

}

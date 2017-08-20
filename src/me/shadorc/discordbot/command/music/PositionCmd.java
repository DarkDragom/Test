package me.shadorc.discordbot.command.music;

import me.shadorc.discordbot.Config;
import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.Shadbot;
import me.shadorc.discordbot.command.AbstractCommand;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.music.GuildMusicManager;
import me.shadorc.discordbot.utils.BotUtils;
import me.shadorc.discordbot.utils.StringUtils;
import sx.blah.discord.util.EmbedBuilder;

public class PositionCmd extends AbstractCommand {

	public PositionCmd() {
		super(Role.USER, "forward", "backward");
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		GuildMusicManager musicManager = GuildMusicManager.getGuildMusicManager(context.getGuild());

		if(musicManager == null || musicManager.getScheduler().isStopped()) {
			BotUtils.sendMessage(Emoji.MUTE + " No currently playing music.", context.getChannel());
			return;
		}

		if(!context.hasArg()) {
			throw new MissingArgumentException();
		}

		String numStr = context.getArg().trim();
		if(!StringUtils.isInteger(numStr)) {
			BotUtils.sendMessage(Emoji.EXCLAMATION + " Invalid number.", context.getChannel());
			return;
		}

		int time = (context.getCommand().equals("backward") ? -1 : 1) * Integer.parseInt(numStr) * 1000;

		try {
			musicManager.getScheduler().skip(time);
		} catch (IllegalArgumentException e) {
			BotUtils.sendMessage(Emoji.EXCLAMATION + " New position is negative or superior to the music duration.", context.getChannel());
			return;
		}

		BotUtils.sendMessage(Emoji.CHECK_MARK + " New position: " + StringUtils.formatDuration(musicManager.getScheduler().getPosition()), context.getChannel());
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = new EmbedBuilder()
				.withAuthorName("Help for " + this.getNames()[0] + " command")
				.withAuthorIcon(Shadbot.getClient().getOurUser().getAvatarURL())
				.withColor(Config.BOT_COLOR)
				.appendDescription("**Fast forward/fast backward the current song a specified amount of time (in seconds).**")
				.appendField("Usage", context.getPrefix() + "forward <sec> or " + context.getPrefix() + "backward <sec>", false);
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}

}

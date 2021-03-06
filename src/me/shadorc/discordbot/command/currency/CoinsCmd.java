package me.shadorc.discordbot.command.currency;

import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.Storage;
import me.shadorc.discordbot.command.AbstractCommand;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.utils.BotUtils;
import me.shadorc.discordbot.utils.Utils;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class CoinsCmd extends AbstractCommand {

	public CoinsCmd() {
		super(Role.USER, "coins", "coin");
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		if(context.getMessage().getMentions().isEmpty()) {
			BotUtils.sendMessage(Emoji.PURSE + " You have **" + context.getPlayer().getCoins() + " coin(s)**.", context.getChannel());
		} else {
			IUser user = context.getMessage().getMentions().get(0);
			int coins = Storage.getPlayer(context.getGuild(), user).getCoins();
			BotUtils.sendMessage(Emoji.PURSE + " " + user.getName() + " has **" + coins + " coin(s)**.", context.getChannel());
		}
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = Utils.getDefaultEmbed(this)
				.appendDescription("**Show how many coins you have.\nYou can also see how much coins have an user by mentioning him.**")
				.appendField("Usage", context.getPrefix() + "coins"
						+ "\n" + context.getPrefix() + "coins <@user>", false);
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}
}
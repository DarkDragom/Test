package me.shadorc.discordbot.command.game;

import java.time.temporal.ChronoUnit;

import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.RateLimiter;
import me.shadorc.discordbot.command.AbstractCommand;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.utils.BotUtils;
import me.shadorc.discordbot.utils.MathUtils;
import me.shadorc.discordbot.utils.StringUtils;
import me.shadorc.discordbot.utils.Utils;
import sx.blah.discord.util.EmbedBuilder;

public class RussianRouletteCmd extends AbstractCommand {

	/*
	 * Expected value: -1/6*(100*bet) + 5/6*(20.6*bet) = 0.5 * bet
	 */

	private static final float WIN_MULTIPLIER = 20.6f;
	private static final float LOSE_MULTIPLIER = 100;

	private final RateLimiter rateLimiter;

	public RussianRouletteCmd() {
		super(Role.USER, "russian_roulette", "russian-roulette", "russianroulette");
		this.rateLimiter = new RateLimiter(5, ChronoUnit.SECONDS);
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		if(!context.hasArg()) {
			throw new MissingArgumentException();
		}

		if(rateLimiter.isLimited(context.getGuild(), context.getAuthor())) {
			if(!rateLimiter.isWarned(context.getGuild(), context.getAuthor())) {
				rateLimiter.warn("You can use the russian roulette only once every " + rateLimiter.getTimeout() + " seconds.", context);
			}
			return;
		}

		String betStr = context.getArg();
		if(!StringUtils.isPositiveInt(betStr)) {
			BotUtils.sendMessage(Emoji.EXCLAMATION + " Invalid bet.", context.getChannel());
			return;
		}

		int bet = Integer.parseInt(betStr);
		if(context.getPlayer().getCoins() < bet) {
			BotUtils.sendMessage(Emoji.BANK + " You don't have enough coins for this.", context.getChannel());
			return;
		}

		StringBuilder strBuilder = new StringBuilder(Emoji.DICE + " You break a sweat, you pull the trigger... ");

		int gains;
		if(MathUtils.rand(6) == 0) {
			gains = (int) -Math.min(bet * LOSE_MULTIPLIER, context.getPlayer().getCoins());
			strBuilder.append("**PAN** ... Sorry, you died. You lose **" + Math.abs(gains) + " coins**.");
		} else {
			gains = (int) (bet * WIN_MULTIPLIER);
			strBuilder.append("**click** ... Phew, you are still alive ! You gets **" + gains + " coins**.");
		}

		context.getPlayer().addCoins(gains);
		BotUtils.sendMessage(strBuilder.toString(), context.getChannel());
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = Utils.getDefaultEmbed(this)
				.appendDescription("**Play russian roulette.**")
				.appendField("Usage", context.getPrefix() + "russian_roulette <bet>", false)
				.appendField("Gains", "You have a **5-in-6** chance to win **" + WIN_MULTIPLIER + " times** your bet and "
						+ "a **1-in-6** chance to lose **" + LOSE_MULTIPLIER + " times** your bet.", false);
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}
}

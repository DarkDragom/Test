package me.shadorc.discordbot.command.music;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.MissingArgumentException;
import me.shadorc.discordbot.RateLimiter;
import me.shadorc.discordbot.command.AbstractCommand;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.music.GuildMusicManager;
import me.shadorc.discordbot.utils.BotUtils;
import me.shadorc.discordbot.utils.StringUtils;
import me.shadorc.discordbot.utils.Utils;
import sx.blah.discord.util.EmbedBuilder;

public class PlaylistCmd extends AbstractCommand {

	private final RateLimiter rateLimiter;

	public PlaylistCmd() {
		super(Role.USER, "playlist");
		this.rateLimiter = new RateLimiter(2, ChronoUnit.SECONDS);
	}

	@Override
	public void execute(Context context) throws MissingArgumentException {
		if(rateLimiter.isLimited(context.getGuild(), context.getAuthor())) {
			if(!rateLimiter.isWarned(context.getGuild(), context.getAuthor())) {
				rateLimiter.warn("Take it easy, don't spam :)", context);
			}
			return;
		}

		GuildMusicManager musicManager = GuildMusicManager.getGuildMusicManager(context.getGuild());

		if(musicManager == null || musicManager.getScheduler().isStopped()) {
			BotUtils.sendMessage(Emoji.MUTE + " No currently playing music.", context.getChannel());
			return;
		}

		EmbedBuilder embed = Utils.getDefaultEmbed()
				.withAuthorName("Playlist")
				.withThumbnail("http://icons.iconarchive.com/icons/dtafalonso/yosemite-flat/512/Music-icon.png")
				.appendDescription(this.formatPlaylist(musicManager.getScheduler().getPlaylist()));
		BotUtils.sendEmbed(embed.build(), context.getChannel());
	}

	@Override
	public void showHelp(Context context) {
		EmbedBuilder builder = Utils.getDefaultEmbed(this)
				.appendDescription("**Show current playlist.**");
		BotUtils.sendEmbed(builder.build(), context.getChannel());
	}

	private String formatPlaylist(BlockingQueue<AudioTrack> queue) {
		if(queue.isEmpty()) {
			return "**The playlist is empty.**";
		}

		StringBuilder playlist = new StringBuilder("**" + queue.size() + " music(s) in the playlist:**\n");

		int count = 1;
		for(AudioTrack track : queue) {
			String name = "\n\t**" + count + ".** " + StringUtils.formatTrackName(track.getInfo());
			if(playlist.length() + name.length() < 1800) {
				playlist.append(name);
				count++;
			} else {
				playlist.append("\n\t...");
				break;
			}
		}
		return playlist.toString();
	}
}
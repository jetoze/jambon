package jetoze.jambon.db;

import static com.google.common.base.Preconditions.checkNotNull;
import static tzeth.preconds.MorePreconditions.checkNotEmpty;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import com.google.common.collect.ImmutableSet;

import jetoze.jambon.player.Player;
import jetoze.jambon.player.PlayerName;
import jetoze.jambon.player.Strengths;
import jetoze.jambon.util.Folder;
import tzeth.collections.ImCollectors;
import tzeth.exhume.XmlBuilder;

public abstract class PlayerDb {
	
	public abstract void addPlayer(Player player);

	public abstract Player getPlayer(String id);
	
	public abstract ImmutableSet<String> listAllPlayerIds();

	public static PlayerDb fileBased(Folder dir) {
		dir.createOnDisk();
		return new FileBasedPlayerDb(dir);
	}
	
	
	private static final class FileBasedPlayerDb extends PlayerDb {
		private static final FileFilter FILE_FILTER = f -> {
			String name = f.getName();
			return name.startsWith("player-") && name.endsWith(".xml");
		};

		private final Folder dir;
		
		public FileBasedPlayerDb(Folder dir) {
			this.dir = checkNotNull(dir);
		}

		@Override
		public void addPlayer(Player player) {
			checkNotNull(player);
			XmlBuilder xmlBuilder = new XmlBuilder();
			PlayerName name = player.getName();
			Strengths strengths = player.getStrengths();
			xmlBuilder.root("player").attribute("id", player.getId()).attribute("dob", player.getBirthDate().toString())
				.child("name").attribute("first", player.getName().getFirstName())
							  .attribute("last", name.getLastName()).close()
				.child("strengths")
					.child("goalie").withValue(Double.toString(strengths.getGoalieStrength().getSavePercentage())).close()
					.child("defender").withValue(Double.toString(strengths.getDefenderStrength().getFactor())).close()
					.child("forward")
						.child("score").withValue(Double.toString(strengths.getForwardStrength().getScoringFactor())).close()
						.child("pass").withValue(Double.toString(strengths.getForwardStrength().getPassingFactor())).close();
			File file = getFile(player.getId());
			try {
				xmlBuilder.writeToFile(file);
			} catch (IOException e) {
				throw new DbException("Failed to write player to disk", e);
			}
		}

		@Override
		public Player getPlayer(String id) {
			checkNotEmpty(id);
			File file = getFile(id);
			if (!file.exists() || !file.canRead()) {
				throw new RuntimeException("No such player: " + id);
			}
			throw new RuntimeException("TODO: Implement me.");
		}

		@Override
		public ImmutableSet<String> listAllPlayerIds() {
			return dir.streamFiles(FILE_FILTER)
					.map(this::idFromFile)
					.collect(ImCollectors.toSet());
		}
		
		private File getFile(String playerId) {
			String name = getFileName(playerId);
			return dir.getFile(name);
		}
		
		private static String getFileName(String playerId) {
			return "player-" + playerId + ".xml";
		}
		
		private String idFromFile(File file) {
			String name = file.getName();
			return name.substring("player-".length(), name.length() - ".xml".length());
		}
	}
	
}

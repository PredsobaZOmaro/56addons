package com.example;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class Addons56SoundTriggers {
	private static final Pattern AMPERSAND_COLOR_CODES = Pattern.compile("(?i)&[0-9A-FK-OR]");
	private static final Pattern SECTION_COLOR_CODES = Pattern.compile("(?i)\u00A7[0-9A-FK-OR]");
	private static final Pattern ANY_FORMAT_CODE = Pattern.compile("(?i)[&\u00A7].");
	private static final Set<String> WINDOWS_XP_TRIGGERS = Set.of(
		"is it a frog? is it a man? well, yes, sorta, it's frog man!!!!!!",
		"a dumpster diver has emerged from the swamp!"
	);
	private static final Comparator<ScoreboardEntry> SIDEBAR_ENTRY_COMPARATOR = Comparator
		.comparingInt(ScoreboardEntry::value)
		.reversed()
		.thenComparing(ScoreboardEntry::owner, String.CASE_INSENSITIVE_ORDER);
	private static final int RECENT_CHAT_WINDOW = 3;
	private static final long CHAT_SOUND_COOLDOWN_MS = 450L;
	private static final long BOSS_SOUND_COOLDOWN_MS = 1500L;
	private static final boolean DEBUG_SPIDERMAN = Boolean.getBoolean("addons56.debug.spiderman");
	private static final Deque<String> recentMessages = new ArrayDeque<>();
	private static final Map<String, Long> lastPlayedAtMsBySound = new HashMap<>();
	private static boolean initialized;
	private static boolean tarantulaBossActiveLastTick;
	private static String lastSpidermanDebugSnapshot = "";

	private Addons56SoundTriggers() {
	}

	public static void initialize() {
		if (initialized) {
			return;
		}
		ClientTickEvents.END_CLIENT_TICK.register(Addons56SoundTriggers::onClientTick);
		initialized = true;
	}

	public static void handleIncomingChatMessage(Text incomingText) {
		if (incomingText == null) {
			return;
		}

		String normalizedMessage = normalizeChatMessage(incomingText.getString());
		if (normalizedMessage.isEmpty()) {
			return;
		}

		recentMessages.addLast(normalizedMessage);
		while (recentMessages.size() > RECENT_CHAT_WINDOW) {
			recentMessages.removeFirst();
		}
		String combinedMessage = String.join(" ", recentMessages);

		if (Addons56SoundSettings.isAngelsoundEnabled() && isPrimordialEyeDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("angelsound", CHAT_SOUND_COOLDOWN_MS);
		}
		if (Addons56SoundSettings.isAngelsoundEnabled() && isTikiMaskOrTitanoboaShedDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("angelsound", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isWowEnabled() && isShriveledWaspDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("wow", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isOoomagaEnabled() && isOoomagaDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("ooomaga", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isSadViolinEnabled() && isEnsnaredSnailDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("sad_violin", CHAT_SOUND_COOLDOWN_MS);
		}
		if (Addons56SoundSettings.isSadViolinEnabled() && isEnchantedClayBlockDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("sad_violin", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isFaaahEnabled() && isOctopusTendrilOrTroubledBubbleDrop(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("faaah", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isJawsEnabled() && isTitanoboaSpawnMessage(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("jaws", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isWikitikiEnabled() && isWikiTikiSpawnMessage(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("wikitiki", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isAmogusEnabled() && isBlueRingedOctopusSpawnMessage(normalizedMessage, combinedMessage)) {
			playSoundWithCooldown("amogus", CHAT_SOUND_COOLDOWN_MS);
		}

		if (Addons56SoundSettings.isWindowsXpEnabled() && WINDOWS_XP_TRIGGERS.contains(normalizedMessage)) {
			playSoundWithCooldown("windows_xp", CHAT_SOUND_COOLDOWN_MS);
		}
	}

	private static void onClientTick(MinecraftClient client) {
		if (!Addons56SoundSettings.isSpidermanEnabled()) {
			tarantulaBossActiveLastTick = false;
			return;
		}
		if (client == null || client.player == null || client.world == null) {
			tarantulaBossActiveLastTick = false;
			return;
		}

		List<String> sidebarLines = getNormalizedSidebarLines(client);
		boolean tarantulaSlayerContext = hasTarantulaSlayerContextInSidebar(sidebarLines);
		boolean slayTheBossVisible = hasSlayTheBossLineInSidebar(sidebarLines);
		boolean tarantulaNearby = isTarantulaNearby(client, 48.0);
		// Only treat the boss as active when the player's own sidebar enters boss phase.
		boolean tarantulaBossActive = tarantulaSlayerContext && slayTheBossVisible;
		boolean transitionTriggered = tarantulaBossActive && !tarantulaBossActiveLastTick;
		boolean cooldownBlocked = false;

		// Play once on transition when sidebar reaches active slayer phase.
		if (transitionTriggered) {
			boolean played = playSoundWithCooldown("spiderman", BOSS_SOUND_COOLDOWN_MS);
			cooldownBlocked = !played;
		}
		debugSpidermanState(sidebarLines, tarantulaSlayerContext, slayTheBossVisible, tarantulaNearby, tarantulaBossActive, transitionTriggered, cooldownBlocked);

		tarantulaBossActiveLastTick = tarantulaBossActive;
	}

	private static boolean hasTarantulaSlayerContextInSidebar(List<String> sidebarLines) {
		for (String line : sidebarLines) {
			if (line.contains("tarantula broodfather") || line.contains("spider slayer")) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasSlayTheBossLineInSidebar(List<String> sidebarLines) {
		for (String line : sidebarLines) {
			if (line.contains("slay the boss!")) {
				return true;
			}
		}
		return false;
	}

	private static List<String> getNormalizedSidebarLines(MinecraftClient client) {
		Scoreboard scoreboard = client.world.getScoreboard();
		ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
		if (sidebar == null) {
			return List.of();
		}

		List<String> normalizedLines = scoreboard.getScoreboardEntries(sidebar).stream()
			.filter(entry -> !entry.hidden())
			.sorted(SIDEBAR_ENTRY_COMPARATOR)
			.limit(15)
			.map(entry -> {
				Team team = scoreboard.getScoreHolderTeam(entry.owner());
				return Team.decorateName(team, entry.name()).getString();
			})
			.map(Addons56SoundTriggers::normalizeChatMessage)
			.filter(line -> !line.isEmpty())
			.toList();

		String title = normalizeChatMessage(sidebar.getDisplayName().getString());
		if (!title.isEmpty()) {
			List<String> withTitle = new ArrayList<>(normalizedLines.size() + 1);
			withTitle.add(title);
			withTitle.addAll(normalizedLines);
			return withTitle;
		}
		return normalizedLines;
	}

	private static void debugSpidermanState(
		List<String> sidebarLines,
		boolean tarantulaSlayerContext,
		boolean slayTheBossVisible,
		boolean tarantulaNearby,
		boolean tarantulaBossActive,
		boolean transitionTriggered,
		boolean cooldownBlocked
	) {
		if (!DEBUG_SPIDERMAN) {
			return;
		}
		String snapshot = "context=" + tarantulaSlayerContext
			+ ", slayLine=" + slayTheBossVisible
			+ ", nearby=" + tarantulaNearby
			+ ", active=" + tarantulaBossActive
			+ ", transition=" + transitionTriggered
			+ ", cooldownBlocked=" + cooldownBlocked
			+ ", sidebar=" + sidebarLines;
		if (!snapshot.equals(lastSpidermanDebugSnapshot)) {
			ExampleMod.LOGGER.info("56addons spiderman debug {}", snapshot);
			lastSpidermanDebugSnapshot = snapshot;
		}
	}

	private static boolean isTarantulaNearby(MinecraftClient client, double maxDistance) {
		for (var entity : client.world.getEntities()) {
			if (!normalizeChatMessage(entity.getName().getString()).contains("tarantula broodfather")) {
				continue;
			}
			if (entity.squaredDistanceTo(client.player) <= maxDistance * maxDistance) {
				return true;
			}
		}
		return false;
	}

	private static String normalizeChatMessage(String raw) {
		if (raw == null) {
			return "";
		}
		// Hypixel sidebar lines often inject non-standard formatting pairs (for uniqueness),
		// so strip any &X / §X token instead of a strict color subset.
		String withoutCodes = ANY_FORMAT_CODE.matcher(raw).replaceAll("");
		withoutCodes = AMPERSAND_COLOR_CODES.matcher(withoutCodes).replaceAll("");
		withoutCodes = SECTION_COLOR_CODES.matcher(withoutCodes).replaceAll("");
		return withoutCodes.replaceAll("\\s+", " ").trim().toLowerCase();
	}

	private static boolean isPrimordialEyeDrop(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "insane drop!", "primordial eye")
			|| containsAll(combinedMessage, "insane drop!", "primordial eye");
	}

	private static boolean isShriveledWaspDrop(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "crazy rare drop!", "shriveled wasp")
			|| containsAll(combinedMessage, "crazy rare drop!", "shriveled wasp");
	}

	private static boolean isOoomagaDrop(String normalizedMessage, String combinedMessage) {
		boolean direct = containsAll(normalizedMessage, "crazy rare drop!", "fly swatter")
			|| containsAll(normalizedMessage, "crazy rare drop!", "digested mosquito")
			|| containsAll(normalizedMessage, "crazy rare drop!", "vial of venom");
		boolean wrapped = containsAll(combinedMessage, "crazy rare drop!", "fly swatter")
			|| containsAll(combinedMessage, "crazy rare drop!", "digested mosquito")
			|| containsAll(combinedMessage, "crazy rare drop!", "vial of venom");
		return direct || wrapped;
	}

	private static boolean isEnsnaredSnailDrop(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "crazy rare drop!", "ensnared snail")
			|| containsAll(combinedMessage, "crazy rare drop!", "ensnared snail");
	}

	private static boolean isTikiMaskOrTitanoboaShedDrop(String normalizedMessage, String combinedMessage) {
		return isRareDropForItem(normalizedMessage, combinedMessage, "tiki mask")
			|| isRareDropForItem(normalizedMessage, combinedMessage, "titanoboa shed");
	}

	private static boolean isOctopusTendrilOrTroubledBubbleDrop(String normalizedMessage, String combinedMessage) {
		return isRareDropForItem(normalizedMessage, combinedMessage, "octopus tendril")
			|| isRareDropForItem(normalizedMessage, combinedMessage, "troubled bubble");
	}

	private static boolean isEnchantedClayBlockDrop(String normalizedMessage, String combinedMessage) {
		return isRareDropForItem(normalizedMessage, combinedMessage, "enchanted clay block");
	}

	private static boolean isRareDropForItem(String normalizedMessage, String combinedMessage, String itemName) {
		return containsAll(normalizedMessage, "rare drop!", itemName)
			|| containsAll(combinedMessage, "rare drop!", itemName);
	}

	private static boolean isTitanoboaSpawnMessage(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "a massive titanoboa surfaces", "stretches as far as the eye can see")
			|| containsAll(combinedMessage, "a massive titanoboa surfaces", "stretches as far as the eye can see");
	}

	private static boolean isWikiTikiSpawnMessage(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "the water bubbles and froths", "disturbed the wiki tiki", "you shall pay the price")
			|| containsAll(combinedMessage, "the water bubbles and froths", "disturbed the wiki tiki", "you shall pay the price");
	}

	private static boolean isBlueRingedOctopusSpawnMessage(String normalizedMessage, String combinedMessage) {
		return containsAll(normalizedMessage, "a garish set of tentacles arise", "blue ringed octopus")
			|| containsAll(combinedMessage, "a garish set of tentacles arise", "blue ringed octopus");
	}

	private static boolean containsAll(String source, String... snippets) {
		for (String snippet : snippets) {
			if (!source.contains(snippet)) {
				return false;
			}
		}
		return true;
	}

	private static void playSound(String soundKey) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null || client.player == null) {
			return;
		}

		Identifier id = Identifier.of("addons56", soundKey);
		SoundEvent soundEvent = SoundEvent.of(id);
		client.getSoundManager().play(PositionedSoundInstance.master(soundEvent, 1.0f, 1.0f));
	}

	private static boolean playSoundWithCooldown(String soundKey, long cooldownMs) {
		long now = System.currentTimeMillis();
		Long lastPlay = lastPlayedAtMsBySound.get(soundKey);
		if (lastPlay != null && now - lastPlay < cooldownMs) {
			return false;
		}
		lastPlayedAtMsBySound.put(soundKey, now);
		playSound(soundKey);
		return true;
	}
}

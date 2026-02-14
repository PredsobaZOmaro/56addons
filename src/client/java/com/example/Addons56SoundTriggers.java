package com.example;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.regex.Pattern;

public final class Addons56SoundTriggers {
	private static final Pattern AMPERSAND_COLOR_CODES = Pattern.compile("(?i)&[0-9A-FK-OR]");
	private static final Pattern SECTION_COLOR_CODES = Pattern.compile("(?i)\u00A7[0-9A-FK-OR]");
	private static final Set<String> WINDOWS_XP_TRIGGERS = Set.of(
		"is it a frog? is it a man? well, yes, sorta, it's frog man!!!!!!",
		"a dumpster diver has emerged from the swamp!"
	);
	private static final int RECENT_CHAT_WINDOW = 3;
	private static final Deque<String> recentMessages = new ArrayDeque<>();
	private static boolean initialized;
	private static boolean tarantulaPresentLastTick;

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
			playSound("angelsound");
		}

		if (Addons56SoundSettings.isWowEnabled() && isShriveledWaspDrop(normalizedMessage, combinedMessage)) {
			playSound("wow");
		}

		if (Addons56SoundSettings.isOoomagaEnabled() && isOoomagaDrop(normalizedMessage, combinedMessage)) {
			playSound("ooomaga");
		}

		if (Addons56SoundSettings.isSadViolinEnabled() && isEnsnaredSnailDrop(normalizedMessage, combinedMessage)) {
			playSound("sad_violin");
		}

		if (Addons56SoundSettings.isWindowsXpEnabled() && WINDOWS_XP_TRIGGERS.contains(normalizedMessage)) {
			playSound("windows_xp");
		}
	}

	private static void onClientTick(MinecraftClient client) {
		if (!Addons56SoundSettings.isSpidermanEnabled()) {
			tarantulaPresentLastTick = false;
			return;
		}
		if (client == null || client.player == null || client.world == null) {
			tarantulaPresentLastTick = false;
			return;
		}

		boolean tarantulaPresent = false;
		for (var entity : client.world.getEntities()) {
			if (normalizeChatMessage(entity.getName().getString()).contains("tarantula broodfather")) {
				tarantulaPresent = true;
				break;
			}
		}

		// Play once on transition from "not present" to "present" to avoid repeating every tick.
		if (tarantulaPresent && !tarantulaPresentLastTick) {
			playSound("spiderman");
		}

		tarantulaPresentLastTick = tarantulaPresent;
	}

	private static String normalizeChatMessage(String raw) {
		if (raw == null) {
			return "";
		}
		String withoutCodes = AMPERSAND_COLOR_CODES.matcher(raw).replaceAll("");
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
}

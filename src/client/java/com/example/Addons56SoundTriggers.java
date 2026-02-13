package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.regex.Pattern;

public final class Addons56SoundTriggers {
	private static final Pattern AMPERSAND_COLOR_CODES = Pattern.compile("(?i)&[0-9A-FK-OR]");
	private static final Pattern SECTION_COLOR_CODES = Pattern.compile("(?i)\u00A7[0-9A-FK-OR]");
	private static final Pattern RARE_DROP_PATTERN = Pattern.compile("^RARE DROP!\\s+(Potato|Carrot)\\b.*$");
	private static final Set<String> WINDOWS_XP_TRIGGERS = Set.of(
		"Is it a frog? Is it a man? Well, yes, sorta, IT'S FROG MAN!!!!!!",
		"A Dumpster Diver has emerged from the swamp!"
	);

	private Addons56SoundTriggers() {
	}

	public static void handleIncomingChatMessage(Text incomingText) {
		if (incomingText == null) {
			return;
		}

		String normalizedMessage = normalizeChatMessage(incomingText.getString());
		if (normalizedMessage.isEmpty()) {
			return;
		}

		if (Addons56SoundSettings.isOoomagaEnabled() && isCarrotOrPotatoRareDrop(normalizedMessage)) {
			playSound("ooomaga");
		}

		if (Addons56SoundSettings.isWindowsXpEnabled() && WINDOWS_XP_TRIGGERS.contains(normalizedMessage)) {
			playSound("windows_xp");
		}
	}

	private static String normalizeChatMessage(String raw) {
		if (raw == null) {
			return "";
		}
		String withoutCodes = AMPERSAND_COLOR_CODES.matcher(raw).replaceAll("");
		withoutCodes = SECTION_COLOR_CODES.matcher(withoutCodes).replaceAll("");
		return withoutCodes.replaceAll("\\s+", " ").trim();
	}

	private static boolean isCarrotOrPotatoRareDrop(String normalizedMessage) {
		// Match the item name only; the trailing Magic Find number is intentionally ignored.
		return RARE_DROP_PATTERN.matcher(normalizedMessage).matches();
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

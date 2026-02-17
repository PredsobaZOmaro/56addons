package com.example.mixin.client;

import com.example.ExampleMod;
import com.example.Addons56SoundTriggers;
import com.example.chatCompact;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudCompactMixin {
	private static final boolean DEBUG_CHAT_COMPACT = true;

	@Shadow
	@Final
	private List<ChatHudLine> messages;

	@Shadow
	protected abstract void refresh();

	@Inject(
		method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void addons56$compactIncoming(Text incomingText, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
		// Sound triggers are independent from chat compaction and should run for every incoming line.
		Addons56SoundTriggers.handleIncomingChatMessage(incomingText);

		if (!chatCompact.isChatCompactEnabled()) {
			return;
		}

		String raw = incomingText.getString();
		String normalized = normalize(raw);
		if (normalized.isEmpty() || shouldIgnoreFromCompaction(normalized)) {
			return;
		}

		int matchIndex = addons56$findRecentMatchingMessageIndex(normalized);
		if (matchIndex < 0) {
			if (DEBUG_CHAT_COMPACT) {
				ExampleMod.LOGGER.info("56addons compact track new: {}", normalized);
			}
			return;
		}

		ChatHudLine matched = messages.get(matchIndex);
		int nextCount = extractCountSuffix(matched.content().getString()) + 1;
		MutableText replacement = incomingText.copy()
			.append(Text.literal(" (" + nextCount + ")").formatted(Formatting.GRAY));

		// Move the compacted line to the newest position (ChatHud stores newest at index 0).
		messages.remove(matchIndex);
		int newestTick = addons56$getLatestMessageTick();
		messages.add(0, new ChatHudLine(newestTick + 1, replacement, signature, indicator));
		refresh();
		if (DEBUG_CHAT_COMPACT) {
			ExampleMod.LOGGER.info("56addons compacted '{}' to ({})", normalized, nextCount);
		}
		ci.cancel();
	}

	private int addons56$getLatestMessageTick() {
		int latestTick = 0;
		for (ChatHudLine line : messages) {
			if (line.creationTick() > latestTick) {
				latestTick = line.creationTick();
			}
		}
		return latestTick;
	}

	private int addons56$findRecentMatchingMessageIndex(String normalizedIncoming) {
		int latestTick = Integer.MIN_VALUE;
		for (int i = 0; i < messages.size(); i++) {
			ChatHudLine line = messages.get(i);
			if (line.creationTick() > latestTick) {
				latestTick = line.creationTick();
			}
		}
		if (latestTick == Integer.MIN_VALUE) {
			return -1;
		}

		int windowTicks = Math.max(1, chatCompact.getChatCompactWindowSeconds()) * 20;
		int oldestAllowedTick = latestTick - windowTicks;

		int bestIndex = -1;
		int bestTick = Integer.MIN_VALUE;
		for (int i = 0; i < messages.size(); i++) {
			ChatHudLine line = messages.get(i);
			int tick = line.creationTick();
			if (tick < oldestAllowedTick) {
				continue;
			}

			String normalizedExisting = normalize(line.content().getString());
			if (normalizedExisting.isEmpty() || shouldIgnoreFromCompaction(normalizedExisting)) {
				continue;
			}

			if (!normalizedIncoming.equals(normalizedExisting)) {
				continue;
			}

			if (tick > bestTick) {
				bestTick = tick;
				bestIndex = i;
			}
		}
		return bestIndex;
	}

	private static boolean shouldIgnoreFromCompaction(String normalized) {
		if (normalized.length() < 10) {
			return false;
		}
		for (int i = 0; i < normalized.length(); i++) {
			char c = normalized.charAt(i);
			if (!Character.isWhitespace(c) && c != '-' && c != '=' && c != '_' && c != '|' && c != '~' && c != '▬') {
				return false;
			}
		}
		return true;
	}

	private static String normalize(String raw) {
		if (raw == null) {
			return "";
		}

		String normalized = raw.trim();
		normalized = normalized.replaceAll("\\s*\\(\\d+\\)\\s*$", "");
		normalized = normalized.replaceFirst("^<[^>]{1,32}>\\s*", "");
		normalized = normalized.replaceFirst("^[^:\\n]{1,32}:\\s+", "");
		normalized = normalized.replaceAll("\\s+", " ");
		return normalized;
	}

	private static int extractCountSuffix(String raw) {
		if (raw == null) {
			return 1;
		}

		java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\((\\d+)\\)\\s*$").matcher(raw.trim());
		if (!matcher.find()) {
			return 1;
		}

		try {
			return Math.max(1, Integer.parseInt(matcher.group(1)));
		} catch (NumberFormatException ignored) {
			return 1;
		}
	}

}


package com.example;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.regex.Pattern;

public final class JerryFeatures {
	private static final Identifier HUD_ID = Identifier.of(ExampleMod.MOD_ID, "jerry_cooldown_hud");
	private static final Pattern ANY_FORMAT_CODE = Pattern.compile("(?i)[&\u00A7].");
	private static final Pattern HIDDEN_JERRY_PATTERN = Pattern.compile("^\\W*you located a hidden (green|blue|purple|golden) jerry!$");
	private static final long JERRY_COOLDOWN_MS = 6L * 60L * 1000L;
	private static long lastJerryFoundAtMs = -1L;

	private JerryFeatures() {
	}

	public static void initialize() {
		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			HUD_ID,
			(context, tickCounter) -> render(context, false)
		);
	}

	public static void handleIncomingChatMessage(Text incomingText, MessageSignatureData signature, MessageIndicator indicator) {
		if (incomingText == null || !isHiddenJerryServerMessage(incomingText, signature)) {
			return;
		}

		long now = System.currentTimeMillis();
		if (JerrySettings.isCooldownHudEnabled()) {
			lastJerryFoundAtMs = now;
		}
		if (JerrySettings.isNotificationEnabled()) {
			showNotification();
		}
	}

	private static boolean isHiddenJerryServerMessage(Text incomingText, MessageSignatureData signature) {
		// Signed messages are player chat; ignore to avoid pasted-message false positives.
		if (signature != null) {
			return false;
		}
		String normalized = normalizeMessage(incomingText.getString());
		return HIDDEN_JERRY_PATTERN.matcher(normalized).matches();
	}

	public static void render(DrawContext context, boolean forceVisible) {
		if (!forceVisible && !JerrySettings.isCooldownHudEnabled()) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null || client.textRenderer == null) {
			return;
		}

		renderAt(
			context,
			client.textRenderer,
			JerrySettings.getHudX(),
			JerrySettings.getHudY(),
			JerrySettings.getHudScale()
		);
	}

	public static int getScaledWidth(TextRenderer textRenderer, float scale) {
		return Math.round((textRenderer.getWidth(getLabelText()) + textRenderer.getWidth(getValueText())) * scale);
	}

	public static int getScaledHeight(TextRenderer textRenderer, float scale) {
		return Math.round(textRenderer.fontHeight * scale);
	}

	private static void renderAt(DrawContext context, TextRenderer textRenderer, int x, int y, float scale) {
		float clampedScale = Math.max(0.5f, Math.min(3.0f, scale));
		Text label = Text.literal(getLabelText()).formatted(Formatting.GOLD);
		boolean ready = isCooldownReady();
		Text value = ready
			? Text.literal("READY").formatted(Formatting.GREEN)
			: Text.literal(getValueText());

		context.getMatrices().pushMatrix();
		context.getMatrices().scale(clampedScale, clampedScale);

		int scaledX = Math.round(x / clampedScale);
		int scaledY = Math.round(y / clampedScale);
		int valueX = scaledX + textRenderer.getWidth(getLabelText());

		context.drawTextWithShadow(textRenderer, label, scaledX, scaledY, 0xFFFFFFFF);
		context.drawTextWithShadow(textRenderer, value, valueX, scaledY, 0xFFFFFFFF);

		context.getMatrices().popMatrix();
	}

	private static String getLabelText() {
		return "Jerry Cooldown: ";
	}

	private static String getValueText() {
		return isCooldownReady() ? "READY" : formatRemainingCooldown();
	}

	private static boolean isCooldownReady() {
		if (lastJerryFoundAtMs < 0L) {
			return true;
		}
		return System.currentTimeMillis() - lastJerryFoundAtMs >= JERRY_COOLDOWN_MS;
	}

	private static String formatRemainingCooldown() {
		if (lastJerryFoundAtMs < 0L) {
			return "0:00";
		}

		long elapsed = System.currentTimeMillis() - lastJerryFoundAtMs;
		long remainingMs = Math.max(0L, JERRY_COOLDOWN_MS - elapsed);
		long totalSeconds = remainingMs / 1000L;
		long minutes = totalSeconds / 60L;
		long seconds = totalSeconds % 60L;
		return String.format("%d:%02d", minutes, seconds);
	}

	private static void showNotification() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null || client.inGameHud == null) {
			return;
		}

		client.inGameHud.setTitle(Text.literal("Jerry!").formatted(Formatting.GREEN));
		client.inGameHud.setTitleTicks(0, 20, 0);
	}

	private static String normalizeMessage(String raw) {
		if (raw == null) {
			return "";
		}
		String withoutCodes = ANY_FORMAT_CODE.matcher(raw).replaceAll("");
		return withoutCodes.replaceAll("\\s+", " ").trim().toLowerCase();
	}
}

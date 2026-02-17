package com.example;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DarkAuctionTimerHud {
	private static final Identifier HUD_ID = Identifier.of(ExampleMod.MOD_ID, "dark_auction_timer");
	private static long lastNotifiedEpochMinute = -1L;

	private DarkAuctionTimerHud() {
	}

	public static void initialize() {
		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			HUD_ID,
			(context, tickCounter) -> render(context, false)
		);
		ClientTickEvents.END_CLIENT_TICK.register(client -> tickNotification(client));
	}

	public static void render(DrawContext context, boolean forceVisible) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null || client.textRenderer == null) {
			return;
		}

		if (!forceVisible && !DarkAuctionTimerSettings.isTimerEnabled()) {
			return;
		}

		renderAt(
			context,
			client.textRenderer,
			DarkAuctionTimerSettings.getX(),
			DarkAuctionTimerSettings.getY(),
			DarkAuctionTimerSettings.getScale()
		);

	}

	public static int getScaledWidth(TextRenderer textRenderer, float scale) {
		return Math.round((textRenderer.getWidth(getLabelText()) + textRenderer.getWidth(getTimerValueText())) * scale);
	}

	public static int getScaledHeight(TextRenderer textRenderer, float scale) {
		return Math.round(textRenderer.fontHeight * scale);
	}

	private static String getLabelText() {
		return "Dark Auction: ";
	}

	private static String getTimerValueText() {
		long totalSeconds = getSecondsUntilNextDarkAuction();
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	private static void renderAt(DrawContext context, TextRenderer textRenderer, int x, int y, float scale) {
		float clampedScale = Math.max(0.5f, Math.min(3.0f, scale));
		String label = getLabelText();
		String timer = getTimerValueText();

		context.getMatrices().pushMatrix();
		context.getMatrices().scale(clampedScale, clampedScale);

		int scaledX = Math.round(x / clampedScale);
		int scaledY = Math.round(y / clampedScale);
		int timerX = scaledX + textRenderer.getWidth(label);

		context.drawTextWithShadow(textRenderer, label, scaledX, scaledY, 0xFFFFAA00);
		context.drawTextWithShadow(textRenderer, timer, timerX, scaledY, 0xFFFFFFFF);

		context.getMatrices().popMatrix();
	}

	private static void tickNotification(MinecraftClient client) {
		if (!DarkAuctionTimerSettings.isOneMinuteNotifyEnabled() || client.player == null || client.inGameHud == null) {
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		if (now.getMinute() != 54 || now.getSecond() != 0) {
			return;
		}

		long epochMinute = System.currentTimeMillis() / 60000L;
		if (lastNotifiedEpochMinute == epochMinute) {
			return;
		}
		lastNotifiedEpochMinute = epochMinute;

		client.inGameHud.setTitle(Text.literal("Dark Auction").formatted(Formatting.GOLD, Formatting.BOLD));
		client.inGameHud.setTitleTicks(5, 60, 5);
	}

	private static long getSecondsUntilNextDarkAuction() {
		LocalDateTime now = LocalDateTime.now();
		if (now.getMinute() == 55 && now.getSecond() == 0) {
			return 0L;
		}

		LocalDateTime nextAuction = now.withMinute(55).withSecond(0).withNano(0);
		if (!now.isBefore(nextAuction)) {
			nextAuction = nextAuction.plusHours(1);
		}
		return Math.max(0L, Duration.between(now, nextAuction).getSeconds());
	}
}

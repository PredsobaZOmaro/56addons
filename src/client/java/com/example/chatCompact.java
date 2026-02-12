package com.example;

import net.fabricmc.api.ClientModInitializer;

public class chatCompact implements ClientModInitializer {
	private static Addons56Config config;

	@Override
	public void onInitializeClient() {
		config = Addons56Config.load();
		ExampleMod.LOGGER.info("56addons chat compact enabled: {}", config.chatCompactEnabled);
		Addons56Commands.register();
		DarkAuctionTimerHud.initialize();
	}

	public static boolean isChatCompactEnabled() {
		return config == null || config.chatCompactEnabled;
	}

	public static void setChatCompactEnabled(boolean enabled) {
		if (config == null) {
			config = new Addons56Config();
		}

		config.chatCompactEnabled = enabled;
		config.save();
	}

	public static int getChatCompactWindowSeconds() {
		if (config == null) {
			return 60;
		}
		return Math.max(1, config.chatCompactWindowSeconds);
	}

	public static void setChatCompactWindowSeconds(int seconds) {
		if (config == null) {
			config = new Addons56Config();
		}

		config.chatCompactWindowSeconds = Math.max(1, seconds);
		config.save();
	}

	public static boolean isDarkAuctionTimerEnabled() {
		return config != null && config.darkAuctionTimerEnabled;
	}

	public static void setDarkAuctionTimerEnabled(boolean enabled) {
		if (config == null) {
			config = new Addons56Config();
		}
		config.darkAuctionTimerEnabled = enabled;
		config.save();
	}

	public static boolean isDarkAuctionNotifyOneMinuteEnabled() {
		return config != null && config.darkAuctionNotifyOneMinute;
	}

	public static void setDarkAuctionNotifyOneMinuteEnabled(boolean enabled) {
		if (config == null) {
			config = new Addons56Config();
		}
		config.darkAuctionNotifyOneMinute = enabled;
		config.save();
	}

	public static int getDarkAuctionTimerX() {
		return config != null ? Math.max(0, config.darkAuctionTimerX) : 10;
	}

	public static int getDarkAuctionTimerY() {
		return config != null ? Math.max(0, config.darkAuctionTimerY) : 10;
	}

	public static float getDarkAuctionTimerScale() {
		if (config == null) {
			return 1.0f;
		}
		float scale = config.darkAuctionTimerScale;
		if (!Float.isFinite(scale)) {
			return 1.0f;
		}
		return Math.max(0.5f, Math.min(3.0f, scale));
	}

	public static void setDarkAuctionTimerPosition(int x, int y) {
		if (config == null) {
			config = new Addons56Config();
		}
		config.darkAuctionTimerX = Math.max(0, x);
		config.darkAuctionTimerY = Math.max(0, y);
		config.save();
	}

	public static void setDarkAuctionTimerScale(float scale) {
		if (config == null) {
			config = new Addons56Config();
		}
		if (!Float.isFinite(scale)) {
			scale = 1.0f;
		}
		config.darkAuctionTimerScale = Math.max(0.5f, Math.min(3.0f, scale));
		config.save();
	}

	public static Addons56Config snapshotConfig() {
		if (config == null) {
			config = Addons56Config.load();
		}

		Addons56Config copy = new Addons56Config();
		copy.chatCompactEnabled = config.chatCompactEnabled;
		copy.chatCompactWindowSeconds = config.chatCompactWindowSeconds;
		copy.darkAuctionTimerEnabled = config.darkAuctionTimerEnabled;
		copy.darkAuctionNotifyOneMinute = config.darkAuctionNotifyOneMinute;
		copy.darkAuctionTimerX = config.darkAuctionTimerX;
		copy.darkAuctionTimerY = config.darkAuctionTimerY;
		copy.darkAuctionTimerScale = config.darkAuctionTimerScale;
		return copy;
	}

	public static void applyConfig(Addons56Config updatedConfig) {
		if (updatedConfig == null) {
			return;
		}
		config = updatedConfig;
		config.save();
	}
}

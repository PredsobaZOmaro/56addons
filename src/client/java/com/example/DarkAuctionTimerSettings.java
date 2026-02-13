package com.example;

public final class DarkAuctionTimerSettings {
	private DarkAuctionTimerSettings() {
	}

	public static boolean isTimerEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().darkAuctionTimerEnabled;
	}

	public static void setTimerEnabled(boolean enabled) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		cfg.darkAuctionTimerEnabled = enabled;
		cfg.save();
	}

	public static boolean isOneMinuteNotifyEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().darkAuctionNotifyOneMinute;
	}

	public static void setOneMinuteNotifyEnabled(boolean enabled) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		cfg.darkAuctionNotifyOneMinute = enabled;
		cfg.save();
	}

	public static int getX() {
		return Math.max(0, Addons56ConfigStore.getOrCreateConfig().darkAuctionTimerX);
	}

	public static int getY() {
		return Math.max(0, Addons56ConfigStore.getOrCreateConfig().darkAuctionTimerY);
	}

	public static float getScale() {
		float scale = Addons56ConfigStore.getOrCreateConfig().darkAuctionTimerScale;
		if (!Float.isFinite(scale)) {
			return 1.0f;
		}
		return Math.max(0.5f, Math.min(3.0f, scale));
	}

	public static void setPosition(int x, int y) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		cfg.darkAuctionTimerX = Math.max(0, x);
		cfg.darkAuctionTimerY = Math.max(0, y);
		cfg.save();
	}

	public static void setScale(float scale) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		if (!Float.isFinite(scale)) {
			scale = 1.0f;
		}
		cfg.darkAuctionTimerScale = Math.max(0.5f, Math.min(3.0f, scale));
		cfg.save();
	}
}

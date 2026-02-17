package com.example;

public final class JerrySettings {
	private JerrySettings() {
	}

	public static boolean isCooldownHudEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().jerryCooldownHudEnabled;
	}

	public static boolean isNotificationEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().jerryNotificationEnabled;
	}

	public static int getHudX() {
		return Math.max(0, Addons56ConfigStore.getOrCreateConfig().jerryCooldownHudX);
	}

	public static int getHudY() {
		return Math.max(0, Addons56ConfigStore.getOrCreateConfig().jerryCooldownHudY);
	}

	public static float getHudScale() {
		float scale = Addons56ConfigStore.getOrCreateConfig().jerryCooldownHudScale;
		if (!Float.isFinite(scale)) {
			return 1.0f;
		}
		return Math.max(0.5f, Math.min(3.0f, scale));
	}

	public static void setHudPosition(int x, int y) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		cfg.jerryCooldownHudX = Math.max(0, x);
		cfg.jerryCooldownHudY = Math.max(0, y);
		cfg.save();
	}

	public static void setHudScale(float scale) {
		Addons56Config cfg = Addons56ConfigStore.getOrCreateConfig();
		if (!Float.isFinite(scale)) {
			scale = 1.0f;
		}
		cfg.jerryCooldownHudScale = Math.max(0.5f, Math.min(3.0f, scale));
		cfg.save();
	}
}

package com.example;

public final class Addons56ConfigStore {
	private static Addons56Config config;

	private Addons56ConfigStore() {
	}

	public static void initialize() {
		config = Addons56Config.load();
	}

	public static Addons56Config snapshot() {
		Addons56Config source = getOrCreateConfig();
		Addons56Config copy = new Addons56Config();
		copy.chatCompactEnabled = source.chatCompactEnabled;
		copy.chatCompactWindowSeconds = source.chatCompactWindowSeconds;
		copy.darkAuctionTimerEnabled = source.darkAuctionTimerEnabled;
		copy.darkAuctionNotifyOneMinute = source.darkAuctionNotifyOneMinute;
		copy.darkAuctionTimerX = source.darkAuctionTimerX;
		copy.darkAuctionTimerY = source.darkAuctionTimerY;
		copy.darkAuctionTimerScale = source.darkAuctionTimerScale;
		copy.ooomagaSoundEnabled = source.ooomagaSoundEnabled;
		copy.windowsXpSoundEnabled = source.windowsXpSoundEnabled;
		copy.spidermanSoundEnabled = source.spidermanSoundEnabled;
		copy.angelsoundSoundEnabled = source.angelsoundSoundEnabled;
		copy.wowSoundEnabled = source.wowSoundEnabled;
		copy.sadViolinSoundEnabled = source.sadViolinSoundEnabled;
		copy.jawsSoundEnabled = source.jawsSoundEnabled;
		copy.wikitikiSoundEnabled = source.wikitikiSoundEnabled;
		copy.amogusSoundEnabled = source.amogusSoundEnabled;
		copy.faaahSoundEnabled = source.faaahSoundEnabled;
		return copy;
	}

	public static void apply(Addons56Config updatedConfig) {
		if (updatedConfig == null) {
			return;
		}
		config = updatedConfig;
		config.save();
	}

	public static boolean isChatCompactEnabled() {
		return getOrCreateConfig().chatCompactEnabled;
	}

	public static void setChatCompactEnabled(boolean enabled) {
		Addons56Config cfg = getOrCreateConfig();
		cfg.chatCompactEnabled = enabled;
		cfg.save();
	}

	public static int getChatCompactWindowSeconds() {
		return Math.max(1, getOrCreateConfig().chatCompactWindowSeconds);
	}

	public static void setChatCompactWindowSeconds(int seconds) {
		Addons56Config cfg = getOrCreateConfig();
		cfg.chatCompactWindowSeconds = Math.max(1, seconds);
		cfg.save();
	}

	static Addons56Config getOrCreateConfig() {
		if (config == null) {
			config = Addons56Config.load();
		}
		return config;
	}
}

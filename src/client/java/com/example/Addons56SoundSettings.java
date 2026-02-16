package com.example;

public final class Addons56SoundSettings {
	private Addons56SoundSettings() {
	}

	public static boolean isOoomagaEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().ooomagaSoundEnabled;
	}

	public static void setOoomagaEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.ooomagaSoundEnabled = enabled;
		config.save();
	}

	public static boolean isWindowsXpEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().windowsXpSoundEnabled;
	}

	public static void setWindowsXpEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.windowsXpSoundEnabled = enabled;
		config.save();
	}

	public static boolean isSpidermanEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().spidermanSoundEnabled;
	}

	public static void setSpidermanEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.spidermanSoundEnabled = enabled;
		config.save();
	}

	public static boolean isAngelsoundEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().angelsoundSoundEnabled;
	}

	public static void setAngelsoundEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.angelsoundSoundEnabled = enabled;
		config.save();
	}

	public static boolean isWowEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().wowSoundEnabled;
	}

	public static void setWowEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.wowSoundEnabled = enabled;
		config.save();
	}

	public static boolean isSadViolinEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().sadViolinSoundEnabled;
	}

	public static void setSadViolinEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.sadViolinSoundEnabled = enabled;
		config.save();
	}

	public static boolean isJawsEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().jawsSoundEnabled;
	}

	public static void setJawsEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.jawsSoundEnabled = enabled;
		config.save();
	}

	public static boolean isWikitikiEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().wikitikiSoundEnabled;
	}

	public static void setWikitikiEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.wikitikiSoundEnabled = enabled;
		config.save();
	}

	public static boolean isAmogusEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().amogusSoundEnabled;
	}

	public static void setAmogusEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.amogusSoundEnabled = enabled;
		config.save();
	}

	public static boolean isFaaahEnabled() {
		return Addons56ConfigStore.getOrCreateConfig().faaahSoundEnabled;
	}

	public static void setFaaahEnabled(boolean enabled) {
		Addons56Config config = Addons56ConfigStore.getOrCreateConfig();
		config.faaahSoundEnabled = enabled;
		config.save();
	}
}

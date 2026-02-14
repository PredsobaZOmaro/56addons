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
}

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
}

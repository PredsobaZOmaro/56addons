package com.example;

import net.fabricmc.api.ClientModInitializer;

public class chatCompact implements ClientModInitializer {
	private static Addons56Config config;

	@Override
	public void onInitializeClient() {
		config = Addons56Config.load();
		ExampleMod.LOGGER.info("56addons chat compact enabled: {}", config.chatCompactEnabled);
		Addons56Commands.register();
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
}

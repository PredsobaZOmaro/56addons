package com.example;

import net.fabricmc.api.ClientModInitializer;

public class chatCompact implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Addons56ConfigStore.initialize();
		ExampleMod.LOGGER.info("56addons chat compact enabled: {}", Addons56ConfigStore.isChatCompactEnabled());
		Addons56Commands.register();
		DarkAuctionTimerHud.initialize();
	}

	public static boolean isChatCompactEnabled() {
		return Addons56ConfigStore.isChatCompactEnabled();
	}

	public static void setChatCompactEnabled(boolean enabled) {
		Addons56ConfigStore.setChatCompactEnabled(enabled);
	}

	public static int getChatCompactWindowSeconds() {
		return Addons56ConfigStore.getChatCompactWindowSeconds();
	}

	public static void setChatCompactWindowSeconds(int seconds) {
		Addons56ConfigStore.setChatCompactWindowSeconds(seconds);
	}

}

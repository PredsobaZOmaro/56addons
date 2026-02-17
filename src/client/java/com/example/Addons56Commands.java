package com.example;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class Addons56Commands {
	private Addons56Commands() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> register(dispatcher));
	}

	private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(
			ClientCommandManager.literal("56")
				.executes(context -> openGui(context.getSource()))
		);

		dispatcher.register(
			ClientCommandManager.literal("56addons")
				.executes(context -> openGui(context.getSource()))
		);
	}

	private static int openGui(FabricClientCommandSource source) {
		MinecraftClient client = source.getClient();
		client.send(() -> client.setScreen(new Addons56ConfigScreen(client.currentScreen)));
		source.sendFeedback(Text.literal("Opened 56addons settings."));
		return 1;
	}
}

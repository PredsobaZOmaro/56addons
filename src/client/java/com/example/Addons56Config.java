package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Addons56Config {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("addons56.json");

	public boolean chatCompactEnabled = true;
	public int chatCompactWindowSeconds = 60;
	public boolean darkAuctionTimerEnabled = false;
	public boolean darkAuctionNotifyOneMinute = false;
	public int darkAuctionTimerX = 10;
	public int darkAuctionTimerY = 10;
	public float darkAuctionTimerScale = 1.0f;
	public boolean ooomagaSoundEnabled = false;
	public boolean windowsXpSoundEnabled = false;

	public static Addons56Config load() {
		if (!Files.exists(CONFIG_PATH)) {
			Addons56Config config = new Addons56Config();
			config.save();
			return config;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			Addons56Config loaded = GSON.fromJson(reader, Addons56Config.class);
			if (loaded == null) {
				return new Addons56Config();
			}
			loaded.sanitize();
			return loaded;
		} catch (IOException | JsonSyntaxException e) {
			ExampleMod.LOGGER.warn("Failed to read config at {}", CONFIG_PATH, e);
			return new Addons56Config();
		}
	}

	public void save() {
		sanitize();
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException e) {
			ExampleMod.LOGGER.warn("Failed to save config at {}", CONFIG_PATH, e);
		}
	}

	private void sanitize() {
		chatCompactWindowSeconds = Math.max(1, chatCompactWindowSeconds);
		if (!Float.isFinite(darkAuctionTimerScale)) {
			darkAuctionTimerScale = 1.0f;
		}
		darkAuctionTimerScale = Math.max(0.5f, Math.min(3.0f, darkAuctionTimerScale));
		darkAuctionTimerX = Math.max(0, darkAuctionTimerX);
		darkAuctionTimerY = Math.max(0, darkAuctionTimerY);
	}
}

package com.example;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class Addons56ConfigScreen extends Screen {
	private final Screen parent;
	private Tab selectedTab = Tab.GENERAL;
	private ButtonWidget generalTabButton;
	private ButtonWidget chatTabButton;
	private ButtonWidget infoTabButton;
	private ButtonWidget chatCompactButton;
	private ButtonWidget chatWindowMinusButton;
	private ButtonWidget chatWindowValueButton;
	private ButtonWidget chatWindowPlusButton;

	public Addons56ConfigScreen(Screen parent) {
		super(Text.literal("56addons"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int centerX = this.width / 2;
		int y = 42;

		generalTabButton = addDrawableChild(createTabButton(Tab.GENERAL, centerX - 155, y));
		chatTabButton = addDrawableChild(createTabButton(Tab.CHAT, centerX - 50, y));
		infoTabButton = addDrawableChild(createTabButton(Tab.INFO, centerX + 55, y));

		this.chatCompactButton = addDrawableChild(ButtonWidget.builder(getChatCompactLabel(), button -> {
			boolean next = !chatCompact.isChatCompactEnabled();
			chatCompact.setChatCompactEnabled(next);
			button.setMessage(getChatCompactLabel());
			updateVisibleControls();
		}).dimensions(centerX - 100, 88, 200, 20).build());

		this.chatWindowMinusButton = addDrawableChild(ButtonWidget.builder(Text.literal("-"), button -> {
			chatCompact.setChatCompactWindowSeconds(chatCompact.getChatCompactWindowSeconds() - 1);
			updateVisibleControls();
		}).dimensions(centerX - 100, 114, 20, 20).build());

		this.chatWindowValueButton = addDrawableChild(ButtonWidget.builder(getChatWindowLabel(), button -> {
		}).dimensions(centerX - 75, 114, 150, 20).build());
		this.chatWindowValueButton.active = false;

		this.chatWindowPlusButton = addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
			chatCompact.setChatCompactWindowSeconds(chatCompact.getChatCompactWindowSeconds() + 1);
			updateVisibleControls();
		}).dimensions(centerX + 80, 114, 20, 20).build());

		addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
			.dimensions(centerX - 100, this.height - 36, 200, 20)
			.build());

		updateVisibleControls();
	}

	private ButtonWidget createTabButton(Tab tab, int x, int y) {
		ButtonWidget button = ButtonWidget.builder(tab.label, b -> {
			selectedTab = tab;
			updateVisibleControls();
		}).dimensions(x, y, 100, 20).build();
		button.active = selectedTab != tab;
		return button;
	}

	private Text getChatCompactLabel() {
		return Text.literal("Chat Compact: " + (chatCompact.isChatCompactEnabled() ? "ON" : "OFF"));
	}

	private Text getChatWindowLabel() {
		return Text.literal("Window: " + chatCompact.getChatCompactWindowSeconds() + "s");
	}

	private void updateVisibleControls() {
		if (chatCompactButton != null) {
			chatCompactButton.visible = selectedTab == Tab.CHAT;
		}
		if (chatWindowMinusButton != null) {
			chatWindowMinusButton.visible = selectedTab == Tab.CHAT;
			chatWindowMinusButton.active = selectedTab == Tab.CHAT && chatCompact.isChatCompactEnabled() && chatCompact.getChatCompactWindowSeconds() > 1;
		}
		if (chatWindowPlusButton != null) {
			chatWindowPlusButton.visible = selectedTab == Tab.CHAT;
			chatWindowPlusButton.active = selectedTab == Tab.CHAT && chatCompact.isChatCompactEnabled();
		}
		if (chatWindowValueButton != null) {
			chatWindowValueButton.visible = selectedTab == Tab.CHAT;
			chatWindowValueButton.setMessage(getChatWindowLabel());
		}

		if (generalTabButton != null) {
			generalTabButton.active = selectedTab != Tab.GENERAL;
		}
		if (chatTabButton != null) {
			chatTabButton.active = selectedTab != Tab.CHAT;
		}
		if (infoTabButton != null) {
			infoTabButton.active = selectedTab != Tab.INFO;
		}
	}

	@Override
	public void close() {
		if (this.client != null) {
			this.client.setScreen(parent);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		// Avoid vanilla blur calls here; some modpacks hook screen rendering and can trigger
		// "Can only blur once per frame" if blur is requested multiple times.
		context.fill(0, 0, this.width, this.height, 0xC0101010);
		super.render(context, mouseX, mouseY, delta);

		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
		context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Section: " + selectedTab.label.getString()), this.width / 2, 66, 0xAAAAAA);

		if (selectedTab == Tab.GENERAL) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("General settings coming soon."), this.width / 2, 100, 0xDDDDDD);
		} else if (selectedTab == Tab.INFO) {
			String version = FabricLoader.getInstance()
				.getModContainer("addons56")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("56addons"), this.width / 2, 96, 0xDDDDDD);
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Version: " + version), this.width / 2, 112, 0xBBBBBB);
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Client-side QoL features for SkyBlock"), this.width / 2, 128, 0xBBBBBB);
		}
	}

	private enum Tab {
		GENERAL(Text.literal("General")),
		CHAT(Text.literal("Chat")),
		INFO(Text.literal("Info"));

		private final Text label;

		Tab(Text label) {
			this.label = label;
		}
	}
}

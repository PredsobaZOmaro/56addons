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
	private ButtonWidget generalEditGuiButton;
	private ButtonWidget generalDaTimerButton;
	private ButtonWidget generalDaNotifyButton;
	private ButtonWidget saveButton;
	private boolean pendingChatCompactEnabled;
	private int pendingChatCompactWindowSeconds;
	private boolean pendingDarkAuctionTimerEnabled;
	private boolean pendingDarkAuctionNotifyOneMinute;
	private boolean hasUnsavedChanges;

	public Addons56ConfigScreen(Screen parent) {
		super(Text.literal("56addons"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		loadPendingValues();

		int centerX = this.width / 2;
		int y = 42;

		generalTabButton = addDrawableChild(createTabButton(Tab.GENERAL, centerX - 155, y));
		chatTabButton = addDrawableChild(createTabButton(Tab.CHAT, centerX - 50, y));
		infoTabButton = addDrawableChild(createTabButton(Tab.INFO, centerX + 55, y));

		this.chatCompactButton = addDrawableChild(ButtonWidget.builder(getChatCompactLabel(), button -> {
			pendingChatCompactEnabled = !pendingChatCompactEnabled;
			markDirty();
		}).dimensions(centerX - 100, 88, 200, 20).build());

		this.chatWindowMinusButton = addDrawableChild(ButtonWidget.builder(Text.literal("-"), button -> {
			pendingChatCompactWindowSeconds = Math.max(1, pendingChatCompactWindowSeconds - 1);
			markDirty();
		}).dimensions(centerX - 100, 114, 20, 20).build());

		this.chatWindowValueButton = addDrawableChild(ButtonWidget.builder(getChatWindowLabel(), button -> {
		}).dimensions(centerX - 75, 114, 150, 20).build());
		this.chatWindowValueButton.active = false;

		this.chatWindowPlusButton = addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
			pendingChatCompactWindowSeconds = pendingChatCompactWindowSeconds + 1;
			markDirty();
		}).dimensions(centerX + 80, 114, 20, 20).build());

		this.generalEditGuiButton = addDrawableChild(ButtonWidget.builder(Text.literal("Edit GUI"), button -> {
			if (this.client != null) {
				this.client.setScreen(new Addons56HudEditorScreen(parent));
			}
		}).dimensions(centerX - 100, 88, 200, 20).build());

		this.generalDaTimerButton = addDrawableChild(ButtonWidget.builder(getDarkAuctionTimerLabel(), button -> {
			pendingDarkAuctionTimerEnabled = !pendingDarkAuctionTimerEnabled;
			markDirty();
		}).dimensions(centerX - 100, 114, 200, 20).build());

		this.generalDaNotifyButton = addDrawableChild(ButtonWidget.builder(getDarkAuctionNotifyLabel(), button -> {
			pendingDarkAuctionNotifyOneMinute = !pendingDarkAuctionNotifyOneMinute;
			markDirty();
		}).dimensions(centerX - 100, 140, 200, 20).build());

		this.saveButton = addDrawableChild(ButtonWidget.builder(Text.literal("Save Changes"), button -> savePendingChanges())
			.dimensions(centerX - 100, this.height - 36, 98, 20)
			.build());

		addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
			.dimensions(centerX + 2, this.height - 36, 98, 20)
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
		return Text.literal("Chat Compact: " + (pendingChatCompactEnabled ? "ON" : "OFF"));
	}

	private Text getChatWindowLabel() {
		return Text.literal("Cooldown: " + pendingChatCompactWindowSeconds + "s");
	}

	private Text getDarkAuctionTimerLabel() {
		return Text.literal("Dark Auction Timer: " + (pendingDarkAuctionTimerEnabled ? "ON" : "OFF"));
	}

	private Text getDarkAuctionNotifyLabel() {
		return Text.literal("Notify 1 Minute Before DA: " + (pendingDarkAuctionNotifyOneMinute ? "ON" : "OFF"));
	}

	private void updateVisibleControls() {
		if (generalEditGuiButton != null) {
			generalEditGuiButton.visible = selectedTab == Tab.GENERAL;
		}
		if (generalDaTimerButton != null) {
			generalDaTimerButton.visible = selectedTab == Tab.GENERAL;
			generalDaTimerButton.setMessage(getDarkAuctionTimerLabel());
		}
		if (generalDaNotifyButton != null) {
			generalDaNotifyButton.visible = selectedTab == Tab.GENERAL;
			generalDaNotifyButton.setMessage(getDarkAuctionNotifyLabel());
		}

		if (chatCompactButton != null) {
			chatCompactButton.visible = selectedTab == Tab.CHAT;
		}
		if (chatWindowMinusButton != null) {
			chatWindowMinusButton.visible = selectedTab == Tab.CHAT;
			chatWindowMinusButton.active = selectedTab == Tab.CHAT && pendingChatCompactEnabled && pendingChatCompactWindowSeconds > 1;
		}
		if (chatWindowPlusButton != null) {
			chatWindowPlusButton.visible = selectedTab == Tab.CHAT;
			chatWindowPlusButton.active = selectedTab == Tab.CHAT && pendingChatCompactEnabled;
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
		if (saveButton != null) {
			saveButton.active = hasUnsavedChanges;
		}
	}

	private void loadPendingValues() {
		Addons56Config current = Addons56ConfigStore.snapshot();
		pendingChatCompactEnabled = current.chatCompactEnabled;
		pendingChatCompactWindowSeconds = Math.max(1, current.chatCompactWindowSeconds);
		pendingDarkAuctionTimerEnabled = current.darkAuctionTimerEnabled;
		pendingDarkAuctionNotifyOneMinute = current.darkAuctionNotifyOneMinute;
		hasUnsavedChanges = false;
	}

	private void markDirty() {
		hasUnsavedChanges = true;
		updateVisibleControls();
	}

	private void savePendingChanges() {
		// Keep HUD position/scale from current config while applying toggles changed in this screen.
		Addons56Config updated = Addons56ConfigStore.snapshot();
		updated.chatCompactEnabled = pendingChatCompactEnabled;
		updated.chatCompactWindowSeconds = Math.max(1, pendingChatCompactWindowSeconds);
		updated.darkAuctionTimerEnabled = pendingDarkAuctionTimerEnabled;
		updated.darkAuctionNotifyOneMinute = pendingDarkAuctionNotifyOneMinute;
		Addons56ConfigStore.apply(updated);
		hasUnsavedChanges = false;
		updateVisibleControls();
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
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("HUD and notification settings"), this.width / 2, 170, 0xDDDDDD);
		} else if (selectedTab == Tab.INFO) {
			String version = FabricLoader.getInstance()
				.getModContainer("addons56")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("56addons"), this.width / 2, 96, 0xDDDDDD);
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Version: " + version), this.width / 2, 112, 0xBBBBBB);
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Client-side QoL features for SkyBlock"), this.width / 2, 128, 0xBBBBBB);
		}
		if (hasUnsavedChanges) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Unsaved changes"), this.width / 2, this.height - 48, 0xFFAA00);
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

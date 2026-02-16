package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class Addons56ConfigScreen extends Screen {
	private final Screen parent;
	private Tab selectedTab = Tab.GENERAL;
	private ButtonWidget generalTabButton;
	private ButtonWidget chatTabButton;
	private ButtonWidget soundsTabButton;
	private ButtonWidget chatCompactButton;
	private ButtonWidget chatWindowMinusButton;
	private ButtonWidget chatWindowValueButton;
	private ButtonWidget chatWindowPlusButton;
	private ButtonWidget generalEditGuiButton;
	private ButtonWidget generalDaTimerButton;
	private ButtonWidget generalDaNotifyButton;
	private ButtonWidget soundsOoomagaButton;
	private ButtonWidget soundsWindowsXpButton;
	private ButtonWidget soundsSpidermanButton;
	private ButtonWidget soundsAngelsoundButton;
	private ButtonWidget soundsWowButton;
	private ButtonWidget soundsSadViolinButton;
	private ButtonWidget soundsJawsButton;
	private ButtonWidget soundsWikitikiButton;
	private ButtonWidget soundsAmogusButton;
	private ButtonWidget soundsFaaahButton;
	private ButtonWidget saveButton;
	private boolean pendingChatCompactEnabled;
	private int pendingChatCompactWindowSeconds;
	private boolean pendingDarkAuctionTimerEnabled;
	private boolean pendingDarkAuctionNotifyOneMinute;
	private boolean pendingOoomagaSoundEnabled;
	private boolean pendingWindowsXpSoundEnabled;
	private boolean pendingSpidermanSoundEnabled;
	private boolean pendingAngelsoundSoundEnabled;
	private boolean pendingWowSoundEnabled;
	private boolean pendingSadViolinSoundEnabled;
	private boolean pendingJawsSoundEnabled;
	private boolean pendingWikitikiSoundEnabled;
	private boolean pendingAmogusSoundEnabled;
	private boolean pendingFaaahSoundEnabled;
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
		soundsTabButton = addDrawableChild(createTabButton(Tab.SOUNDS, centerX + 55, y));

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

		this.soundsOoomagaButton = addDrawableChild(ButtonWidget.builder(getOoomagaLabel(), button -> {
			pendingOoomagaSoundEnabled = !pendingOoomagaSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 88, 200, 20).build());

		this.soundsWindowsXpButton = addDrawableChild(ButtonWidget.builder(getWindowsXpLabel(), button -> {
			pendingWindowsXpSoundEnabled = !pendingWindowsXpSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 114, 200, 20).build());

		this.soundsSpidermanButton = addDrawableChild(ButtonWidget.builder(getSpidermanLabel(), button -> {
			pendingSpidermanSoundEnabled = !pendingSpidermanSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 140, 200, 20).build());

		this.soundsAngelsoundButton = addDrawableChild(ButtonWidget.builder(getAngelsoundLabel(), button -> {
			pendingAngelsoundSoundEnabled = !pendingAngelsoundSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 166, 200, 20).build());

		this.soundsWowButton = addDrawableChild(ButtonWidget.builder(getWowLabel(), button -> {
			pendingWowSoundEnabled = !pendingWowSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 192, 200, 20).build());

		this.soundsSadViolinButton = addDrawableChild(ButtonWidget.builder(getSadViolinLabel(), button -> {
			pendingSadViolinSoundEnabled = !pendingSadViolinSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 218, 200, 20).build());

		this.soundsJawsButton = addDrawableChild(ButtonWidget.builder(getJawsLabel(), button -> {
			pendingJawsSoundEnabled = !pendingJawsSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 244, 200, 20).build());

		this.soundsWikitikiButton = addDrawableChild(ButtonWidget.builder(getWikitikiLabel(), button -> {
			pendingWikitikiSoundEnabled = !pendingWikitikiSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 270, 200, 20).build());

		this.soundsAmogusButton = addDrawableChild(ButtonWidget.builder(getAmogusLabel(), button -> {
			pendingAmogusSoundEnabled = !pendingAmogusSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 296, 200, 20).build());

		this.soundsFaaahButton = addDrawableChild(ButtonWidget.builder(getFaaahLabel(), button -> {
			pendingFaaahSoundEnabled = !pendingFaaahSoundEnabled;
			markDirty();
		}).dimensions(centerX - 100, 322, 200, 20).build());

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

	private Text getOoomagaLabel() {
		return Text.literal("ooomaga: " + (pendingOoomagaSoundEnabled ? "ON" : "OFF"));
	}

	private Text getWindowsXpLabel() {
		return Text.literal("windows_xp: " + (pendingWindowsXpSoundEnabled ? "ON" : "OFF"));
	}

	private Text getSpidermanLabel() {
		return Text.literal("spiderman: " + (pendingSpidermanSoundEnabled ? "ON" : "OFF"));
	}

	private Text getAngelsoundLabel() {
		return Text.literal("angelsound: " + (pendingAngelsoundSoundEnabled ? "ON" : "OFF"));
	}

	private Text getWowLabel() {
		return Text.literal("wow: " + (pendingWowSoundEnabled ? "ON" : "OFF"));
	}

	private Text getSadViolinLabel() {
		return Text.literal("sad_violin: " + (pendingSadViolinSoundEnabled ? "ON" : "OFF"));
	}

	private Text getJawsLabel() {
		return Text.literal("jaws: " + (pendingJawsSoundEnabled ? "ON" : "OFF"));
	}

	private Text getWikitikiLabel() {
		return Text.literal("wikitiki: " + (pendingWikitikiSoundEnabled ? "ON" : "OFF"));
	}

	private Text getAmogusLabel() {
		return Text.literal("amogus: " + (pendingAmogusSoundEnabled ? "ON" : "OFF"));
	}

	private Text getFaaahLabel() {
		return Text.literal("faaah: " + (pendingFaaahSoundEnabled ? "ON" : "OFF"));
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
			chatCompactButton.setMessage(getChatCompactLabel());
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

		if (soundsOoomagaButton != null) {
			soundsOoomagaButton.visible = selectedTab == Tab.SOUNDS;
			soundsOoomagaButton.setMessage(getOoomagaLabel());
		}
		if (soundsWindowsXpButton != null) {
			soundsWindowsXpButton.visible = selectedTab == Tab.SOUNDS;
			soundsWindowsXpButton.setMessage(getWindowsXpLabel());
		}
		if (soundsSpidermanButton != null) {
			soundsSpidermanButton.visible = selectedTab == Tab.SOUNDS;
			soundsSpidermanButton.setMessage(getSpidermanLabel());
		}
		if (soundsAngelsoundButton != null) {
			soundsAngelsoundButton.visible = selectedTab == Tab.SOUNDS;
			soundsAngelsoundButton.setMessage(getAngelsoundLabel());
		}
		if (soundsWowButton != null) {
			soundsWowButton.visible = selectedTab == Tab.SOUNDS;
			soundsWowButton.setMessage(getWowLabel());
		}
		if (soundsSadViolinButton != null) {
			soundsSadViolinButton.visible = selectedTab == Tab.SOUNDS;
			soundsSadViolinButton.setMessage(getSadViolinLabel());
		}
		if (soundsJawsButton != null) {
			soundsJawsButton.visible = selectedTab == Tab.SOUNDS;
			soundsJawsButton.setMessage(getJawsLabel());
		}
		if (soundsWikitikiButton != null) {
			soundsWikitikiButton.visible = selectedTab == Tab.SOUNDS;
			soundsWikitikiButton.setMessage(getWikitikiLabel());
		}
		if (soundsAmogusButton != null) {
			soundsAmogusButton.visible = selectedTab == Tab.SOUNDS;
			soundsAmogusButton.setMessage(getAmogusLabel());
		}
		if (soundsFaaahButton != null) {
			soundsFaaahButton.visible = selectedTab == Tab.SOUNDS;
			soundsFaaahButton.setMessage(getFaaahLabel());
		}

		if (generalTabButton != null) {
			generalTabButton.active = selectedTab != Tab.GENERAL;
		}
		if (chatTabButton != null) {
			chatTabButton.active = selectedTab != Tab.CHAT;
		}
		if (soundsTabButton != null) {
			soundsTabButton.active = selectedTab != Tab.SOUNDS;
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
		pendingOoomagaSoundEnabled = current.ooomagaSoundEnabled;
		pendingWindowsXpSoundEnabled = current.windowsXpSoundEnabled;
		pendingSpidermanSoundEnabled = current.spidermanSoundEnabled;
		pendingAngelsoundSoundEnabled = current.angelsoundSoundEnabled;
		pendingWowSoundEnabled = current.wowSoundEnabled;
		pendingSadViolinSoundEnabled = current.sadViolinSoundEnabled;
		pendingJawsSoundEnabled = current.jawsSoundEnabled;
		pendingWikitikiSoundEnabled = current.wikitikiSoundEnabled;
		pendingAmogusSoundEnabled = current.amogusSoundEnabled;
		pendingFaaahSoundEnabled = current.faaahSoundEnabled;
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
		updated.ooomagaSoundEnabled = pendingOoomagaSoundEnabled;
		updated.windowsXpSoundEnabled = pendingWindowsXpSoundEnabled;
		updated.spidermanSoundEnabled = pendingSpidermanSoundEnabled;
		updated.angelsoundSoundEnabled = pendingAngelsoundSoundEnabled;
		updated.wowSoundEnabled = pendingWowSoundEnabled;
		updated.sadViolinSoundEnabled = pendingSadViolinSoundEnabled;
		updated.jawsSoundEnabled = pendingJawsSoundEnabled;
		updated.wikitikiSoundEnabled = pendingWikitikiSoundEnabled;
		updated.amogusSoundEnabled = pendingAmogusSoundEnabled;
		updated.faaahSoundEnabled = pendingFaaahSoundEnabled;
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
		} else if (selectedTab == Tab.SOUNDS) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Toggle sound files"), this.width / 2, this.height - 62, 0xDDDDDD);
		}
		if (hasUnsavedChanges) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Unsaved changes"), this.width / 2, this.height - 48, 0xFFAA00);
		}
	}

	private enum Tab {
		GENERAL(Text.literal("General")),
		CHAT(Text.literal("Chat")),
		SOUNDS(Text.literal("Sounds"));

		private final Text label;

		Tab(Text label) {
			this.label = label;
		}
	}
}

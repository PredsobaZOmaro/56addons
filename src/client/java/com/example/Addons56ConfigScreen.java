package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Addons56ConfigScreen extends Screen {
	private static final int SOUNDS_BUTTON_HEIGHT = 20;
	private static final int SOUNDS_BUTTON_SPACING = 26;
	private static final int SOUNDS_CONTAINER_WIDTH = 280;
	private static final int SOUNDS_CONTAINER_PADDING = 8;
	private static final int SOUNDS_SCROLLBAR_WIDTH = 6;
	private static final int SOUNDS_SCROLLBAR_GAP = 6;
	private final Screen parent;
	private Tab selectedTab = Tab.GENERAL;
	private final List<ButtonWidget> soundButtons = new ArrayList<>();
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
	private int soundsScrollOffset;
	private int soundsMaxScroll;

	public Addons56ConfigScreen(Screen parent) {
		super(Text.literal("56addons"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		loadPendingValues();

		int centerX = this.width / 2;
		int y = 42;
		int soundsButtonWidth = getSoundsButtonWidth();
		int soundsButtonX = centerX - soundsButtonWidth / 2;

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
		}).dimensions(soundsButtonX, 88, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsOoomagaButton);

		this.soundsWindowsXpButton = addDrawableChild(ButtonWidget.builder(getWindowsXpLabel(), button -> {
			pendingWindowsXpSoundEnabled = !pendingWindowsXpSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 114, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsWindowsXpButton);

		this.soundsSpidermanButton = addDrawableChild(ButtonWidget.builder(getSpidermanLabel(), button -> {
			pendingSpidermanSoundEnabled = !pendingSpidermanSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 140, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsSpidermanButton);

		this.soundsAngelsoundButton = addDrawableChild(ButtonWidget.builder(getAngelsoundLabel(), button -> {
			pendingAngelsoundSoundEnabled = !pendingAngelsoundSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 166, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsAngelsoundButton);

		this.soundsWowButton = addDrawableChild(ButtonWidget.builder(getWowLabel(), button -> {
			pendingWowSoundEnabled = !pendingWowSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 192, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsWowButton);

		this.soundsSadViolinButton = addDrawableChild(ButtonWidget.builder(getSadViolinLabel(), button -> {
			pendingSadViolinSoundEnabled = !pendingSadViolinSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 218, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsSadViolinButton);

		this.soundsJawsButton = addDrawableChild(ButtonWidget.builder(getJawsLabel(), button -> {
			pendingJawsSoundEnabled = !pendingJawsSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 244, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsJawsButton);

		this.soundsWikitikiButton = addDrawableChild(ButtonWidget.builder(getWikitikiLabel(), button -> {
			pendingWikitikiSoundEnabled = !pendingWikitikiSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 270, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsWikitikiButton);

		this.soundsAmogusButton = addDrawableChild(ButtonWidget.builder(getAmogusLabel(), button -> {
			pendingAmogusSoundEnabled = !pendingAmogusSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 296, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsAmogusButton);

		this.soundsFaaahButton = addDrawableChild(ButtonWidget.builder(getFaaahLabel(), button -> {
			pendingFaaahSoundEnabled = !pendingFaaahSoundEnabled;
			markDirty();
		}).dimensions(soundsButtonX, 322, soundsButtonWidth, 20).build());
		soundButtons.add(this.soundsFaaahButton);

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
			soundsOoomagaButton.setMessage(getOoomagaLabel());
		}
		if (soundsWindowsXpButton != null) {
			soundsWindowsXpButton.setMessage(getWindowsXpLabel());
		}
		if (soundsSpidermanButton != null) {
			soundsSpidermanButton.setMessage(getSpidermanLabel());
		}
		if (soundsAngelsoundButton != null) {
			soundsAngelsoundButton.setMessage(getAngelsoundLabel());
		}
		if (soundsWowButton != null) {
			soundsWowButton.setMessage(getWowLabel());
		}
		if (soundsSadViolinButton != null) {
			soundsSadViolinButton.setMessage(getSadViolinLabel());
		}
		if (soundsJawsButton != null) {
			soundsJawsButton.setMessage(getJawsLabel());
		}
		if (soundsWikitikiButton != null) {
			soundsWikitikiButton.setMessage(getWikitikiLabel());
		}
		if (soundsAmogusButton != null) {
			soundsAmogusButton.setMessage(getAmogusLabel());
		}
		if (soundsFaaahButton != null) {
			soundsFaaahButton.setMessage(getFaaahLabel());
		}
		updateSoundsScrollLayout();

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
			int left = getSoundsContainerLeft();
			int right = left + SOUNDS_CONTAINER_WIDTH;
			int top = getSoundsContainerTop();
			int bottom = getSoundsContainerBottom();
			context.fill(left, top, right, bottom, 0x70101010);
			context.fill(left, top, right, top + 1, 0x70202020);
			context.fill(left, bottom - 1, right, bottom, 0x70202020);
			context.fill(left, top, left + 1, bottom, 0x70202020);
			context.fill(right - 1, top, right, bottom, 0x70202020);

			int viewportTop = getSoundsViewportTop();
			int viewportBottom = getSoundsViewportBottom();
			int trackLeft = right - SOUNDS_CONTAINER_PADDING - SOUNDS_SCROLLBAR_WIDTH;
			int trackRight = trackLeft + SOUNDS_SCROLLBAR_WIDTH;
			context.fill(trackLeft, viewportTop, trackRight, viewportBottom, 0x90303030);
			drawSoundsScrollbarThumb(context, trackLeft, trackRight, viewportTop, viewportBottom);
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Toggle sound files"), this.width / 2, this.height - 62, 0xDDDDDD);
		}
		if (hasUnsavedChanges) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Unsaved changes"), this.width / 2, this.height - 48, 0xFFAA00);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (selectedTab == Tab.SOUNDS && isInsideSoundsContainer(mouseX, mouseY)) {
			if (verticalAmount > 0) {
				soundsScrollOffset -= SOUNDS_BUTTON_SPACING;
			} else if (verticalAmount < 0) {
				soundsScrollOffset += SOUNDS_BUTTON_SPACING;
			}
			updateSoundsScrollLayout();
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	private int getSoundsContainerLeft() {
		return this.width / 2 - SOUNDS_CONTAINER_WIDTH / 2;
	}

	private int getSoundsContainerTop() {
		return 84;
	}

	private int getSoundsContainerBottom() {
		return this.height - 76;
	}

	private int getSoundsViewportTop() {
		return getSoundsContainerTop() + SOUNDS_CONTAINER_PADDING;
	}

	private int getSoundsViewportBottom() {
		return getSoundsContainerBottom() - SOUNDS_CONTAINER_PADDING;
	}

	private int getSoundsButtonWidth() {
		return SOUNDS_CONTAINER_WIDTH - SOUNDS_CONTAINER_PADDING * 2 - SOUNDS_SCROLLBAR_WIDTH - SOUNDS_SCROLLBAR_GAP;
	}

	private boolean isInsideSoundsContainer(double mouseX, double mouseY) {
		return mouseX >= getSoundsContainerLeft()
			&& mouseX <= getSoundsContainerLeft() + SOUNDS_CONTAINER_WIDTH
			&& mouseY >= getSoundsContainerTop()
			&& mouseY <= getSoundsContainerBottom();
	}

	private void updateSoundsScrollLayout() {
		if (soundButtons.isEmpty()) {
			return;
		}

		int contentHeight = soundButtons.size() * SOUNDS_BUTTON_SPACING;
		int viewportHeight = Math.max(0, getSoundsContainerBottom() - getSoundsContainerTop() - SOUNDS_CONTAINER_PADDING * 2);
		soundsMaxScroll = Math.max(0, contentHeight - viewportHeight);
		soundsScrollOffset = Math.max(0, Math.min(soundsScrollOffset, soundsMaxScroll));

		int buttonX = this.width / 2 - getSoundsButtonWidth() / 2;
		int viewportTop = getSoundsViewportTop();
		int viewportBottom = getSoundsViewportBottom();

		for (int i = 0; i < soundButtons.size(); i++) {
			ButtonWidget button = soundButtons.get(i);
			int buttonY = viewportTop + i * SOUNDS_BUTTON_SPACING - soundsScrollOffset;
			button.setPosition(buttonX, buttonY);
			boolean fullyInsideViewport = buttonY >= viewportTop && buttonY + SOUNDS_BUTTON_HEIGHT <= viewportBottom;
			button.visible = selectedTab == Tab.SOUNDS && fullyInsideViewport;
			button.active = button.visible;
		}
	}

	private void drawSoundsScrollbarThumb(DrawContext context, int trackLeft, int trackRight, int viewportTop, int viewportBottom) {
		int viewportHeight = Math.max(1, viewportBottom - viewportTop);
		int contentHeight = Math.max(1, soundButtons.size() * SOUNDS_BUTTON_SPACING);

		if (soundsMaxScroll <= 0) {
			context.fill(trackLeft, viewportTop, trackRight, viewportBottom, 0x70484848);
			return;
		}

		int thumbHeight = Math.max(16, (int) ((viewportHeight * (double) viewportHeight) / contentHeight));
		int thumbRange = Math.max(1, viewportHeight - thumbHeight);
		int thumbTop = viewportTop + (int) ((soundsScrollOffset / (double) soundsMaxScroll) * thumbRange);
		int thumbBottom = Math.min(viewportBottom, thumbTop + thumbHeight);
		context.fill(trackLeft, thumbTop, trackRight, thumbBottom, 0xB0A8A8A8);
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

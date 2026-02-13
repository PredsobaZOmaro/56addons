package com.example;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class Addons56HudEditorScreen extends Screen {
	private final Screen parent;
	private boolean draggingTimer;
	private int dragOffsetX;
	private int dragOffsetY;

	public Addons56HudEditorScreen(Screen parent) {
		super(Text.literal("56addons HUD Editor"));
		this.parent = parent;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, this.width, this.height, 0x90101010);
		super.render(context, mouseX, mouseY, delta);

		TextRenderer tr = this.textRenderer;
		context.drawCenteredTextWithShadow(tr, Text.literal("HUD Editor"), this.width / 2, 14, 0xFFFFFF);
		context.drawCenteredTextWithShadow(
			tr,
			Text.literal("Drag timer with LMB, scroll to resize, press ESC to save and exit"),
			this.width / 2,
			30,
			0xDDDDDD
		);

		if (!DarkAuctionTimerSettings.isTimerEnabled()) {
			draggingTimer = false;
			context.drawCenteredTextWithShadow(
				tr,
				Text.literal("Dark Auction Timer is OFF in settings"),
				this.width / 2,
				54,
				0xFFAA00
			);
			return;
		}

		int x = DarkAuctionTimerSettings.getX();
		int y = DarkAuctionTimerSettings.getY();
		float scale = DarkAuctionTimerSettings.getScale();
		int boxWidth = DarkAuctionTimerHud.getScaledWidth(tr, scale);
		int boxHeight = DarkAuctionTimerHud.getScaledHeight(tr, scale);
		boolean hovered = isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight);
		int borderColor = hovered || draggingTimer ? 0xFF66CCFF : 0x88FFFFFF;

		context.fill(x - 2, y - 2, x + boxWidth + 2, y + boxHeight + 2, 0x50000000);
		drawBorder(context, x - 2, y - 2, boxWidth + 4, boxHeight + 4, borderColor);
		DarkAuctionTimerHud.render(context, true);
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		if (!DarkAuctionTimerSettings.isTimerEnabled()) {
			return super.mouseClicked(click, doubled);
		}

		if (click.button() != 0) {
			return super.mouseClicked(click, doubled);
		}

		double mouseX = click.x();
		double mouseY = click.y();
		int x = DarkAuctionTimerSettings.getX();
		int y = DarkAuctionTimerSettings.getY();
		float scale = DarkAuctionTimerSettings.getScale();
		int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
		int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
		if (!isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight)) {
			return super.mouseClicked(click, doubled);
		}

		draggingTimer = true;
		dragOffsetX = (int) Math.round(mouseX) - x;
		dragOffsetY = (int) Math.round(mouseY) - y;
		return true;
	}

	@Override
	public boolean mouseReleased(Click click) {
		if (click.button() == 0) {
			draggingTimer = false;
		}
		return super.mouseReleased(click);
	}

	@Override
	public boolean mouseDragged(Click click, double deltaX, double deltaY) {
		if (!DarkAuctionTimerSettings.isTimerEnabled()) {
			draggingTimer = false;
			return super.mouseDragged(click, deltaX, deltaY);
		}

		if (!draggingTimer || click.button() != 0) {
			return super.mouseDragged(click, deltaX, deltaY);
		}

		double mouseX = click.x();
		double mouseY = click.y();
		float scale = DarkAuctionTimerSettings.getScale();
		int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
		int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
		int targetX = (int) Math.round(mouseX) - dragOffsetX;
		int targetY = (int) Math.round(mouseY) - dragOffsetY;
		int clampedX = Math.max(0, Math.min(targetX, this.width - boxWidth));
		int clampedY = Math.max(0, Math.min(targetY, this.height - boxHeight));
		DarkAuctionTimerSettings.setPosition(clampedX, clampedY);
		return true;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (!DarkAuctionTimerSettings.isTimerEnabled()) {
			return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}

		int x = DarkAuctionTimerSettings.getX();
		int y = DarkAuctionTimerSettings.getY();
		float scale = DarkAuctionTimerSettings.getScale();
		int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
		int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
		if (!isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight)) {
			return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}

		float updatedScale = scale + (float) verticalAmount * 0.1f;
		updatedScale = Math.max(0.5f, Math.min(3.0f, updatedScale));
		DarkAuctionTimerSettings.setScale(updatedScale);

		int newWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, updatedScale);
		int newHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, updatedScale);
		int clampedX = Math.max(0, Math.min(x, this.width - newWidth));
		int clampedY = Math.max(0, Math.min(y, this.height - newHeight));
		DarkAuctionTimerSettings.setPosition(clampedX, clampedY);
		return true;
	}

	@Override
	public void close() {
		if (this.client != null) {
			this.client.setScreen(parent);
		}
	}

	private static boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	private static void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
		context.fill(x, y, x + width, y + 1, color);
		context.fill(x, y + height - 1, x + width, y + height, color);
		context.fill(x, y, x + 1, y + height, color);
		context.fill(x + width - 1, y, x + width, y + height, color);
	}
}

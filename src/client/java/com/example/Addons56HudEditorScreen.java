package com.example;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class Addons56HudEditorScreen extends Screen {
	private final Screen parent;
	private DragTarget dragTarget = DragTarget.NONE;
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
			Text.literal("Drag HUD with LMB, scroll to resize, press ESC to save and exit"),
			this.width / 2,
			30,
			0xDDDDDD
		);

		boolean hasDarkAuctionHud = DarkAuctionTimerSettings.isTimerEnabled();
		boolean hasJerryHud = JerrySettings.isCooldownHudEnabled();
		if (!hasDarkAuctionHud && !hasJerryHud) {
			dragTarget = DragTarget.NONE;
			context.drawCenteredTextWithShadow(tr, Text.literal("Dark Auction Timer is OFF in settings"), this.width / 2, 54, 0xFFAA00);
			context.drawCenteredTextWithShadow(tr, Text.literal("Jerry Cooldown HUD is OFF in settings"), this.width / 2, 66, 0xFFAA00);
			return;
		}

		if (hasDarkAuctionHud) {
			int x = DarkAuctionTimerSettings.getX();
			int y = DarkAuctionTimerSettings.getY();
			float scale = DarkAuctionTimerSettings.getScale();
			int boxWidth = DarkAuctionTimerHud.getScaledWidth(tr, scale);
			int boxHeight = DarkAuctionTimerHud.getScaledHeight(tr, scale);
			boolean hovered = isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight);
			int borderColor = hovered || dragTarget == DragTarget.DARK_AUCTION ? 0xFF66CCFF : 0x88FFFFFF;

			context.fill(x - 2, y - 2, x + boxWidth + 2, y + boxHeight + 2, 0x50000000);
			drawBorder(context, x - 2, y - 2, boxWidth + 4, boxHeight + 4, borderColor);
			DarkAuctionTimerHud.render(context, true);
		}

		if (hasJerryHud) {
			int x = JerrySettings.getHudX();
			int y = JerrySettings.getHudY();
			float scale = JerrySettings.getHudScale();
			int boxWidth = JerryFeatures.getScaledWidth(tr, scale);
			int boxHeight = JerryFeatures.getScaledHeight(tr, scale);
			boolean hovered = isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight);
			int borderColor = hovered || dragTarget == DragTarget.JERRY ? 0xFF66CCFF : 0x88FFFFFF;

			context.fill(x - 2, y - 2, x + boxWidth + 2, y + boxHeight + 2, 0x50000000);
			drawBorder(context, x - 2, y - 2, boxWidth + 4, boxHeight + 4, borderColor);
			JerryFeatures.render(context, true);
		}
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		if (click.button() != 0) {
			return super.mouseClicked(click, doubled);
		}

		double mouseX = click.x();
		double mouseY = click.y();
		if (JerrySettings.isCooldownHudEnabled()) {
			int jx = JerrySettings.getHudX();
			int jy = JerrySettings.getHudY();
			float jScale = JerrySettings.getHudScale();
			int jWidth = JerryFeatures.getScaledWidth(this.textRenderer, jScale);
			int jHeight = JerryFeatures.getScaledHeight(this.textRenderer, jScale);
			if (isHovered(mouseX, mouseY, jx, jy, jWidth, jHeight)) {
				dragTarget = DragTarget.JERRY;
				dragOffsetX = (int) Math.round(mouseX) - jx;
				dragOffsetY = (int) Math.round(mouseY) - jy;
				return true;
			}
		}

		if (DarkAuctionTimerSettings.isTimerEnabled()) {
			int x = DarkAuctionTimerSettings.getX();
			int y = DarkAuctionTimerSettings.getY();
			float scale = DarkAuctionTimerSettings.getScale();
			int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
			int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
			if (isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight)) {
				dragTarget = DragTarget.DARK_AUCTION;
				dragOffsetX = (int) Math.round(mouseX) - x;
				dragOffsetY = (int) Math.round(mouseY) - y;
				return true;
			}
		}

		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(Click click) {
		if (click.button() == 0) {
			dragTarget = DragTarget.NONE;
		}
		return super.mouseReleased(click);
	}

	@Override
	public boolean mouseDragged(Click click, double deltaX, double deltaY) {
		if (dragTarget == DragTarget.NONE || click.button() != 0) {
			return super.mouseDragged(click, deltaX, deltaY);
		}

		double mouseX = click.x();
		double mouseY = click.y();
		int targetX = (int) Math.round(mouseX) - dragOffsetX;
		int targetY = (int) Math.round(mouseY) - dragOffsetY;
		if (dragTarget == DragTarget.DARK_AUCTION) {
			float scale = DarkAuctionTimerSettings.getScale();
			int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
			int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
			int clampedX = Math.max(0, Math.min(targetX, this.width - boxWidth));
			int clampedY = Math.max(0, Math.min(targetY, this.height - boxHeight));
			DarkAuctionTimerSettings.setPosition(clampedX, clampedY);
			return true;
		}
		if (dragTarget == DragTarget.JERRY) {
			float scale = JerrySettings.getHudScale();
			int boxWidth = JerryFeatures.getScaledWidth(this.textRenderer, scale);
			int boxHeight = JerryFeatures.getScaledHeight(this.textRenderer, scale);
			int clampedX = Math.max(0, Math.min(targetX, this.width - boxWidth));
			int clampedY = Math.max(0, Math.min(targetY, this.height - boxHeight));
			JerrySettings.setHudPosition(clampedX, clampedY);
			return true;
		}
		return super.mouseDragged(click, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (JerrySettings.isCooldownHudEnabled()) {
			int jx = JerrySettings.getHudX();
			int jy = JerrySettings.getHudY();
			float jScale = JerrySettings.getHudScale();
			int jWidth = JerryFeatures.getScaledWidth(this.textRenderer, jScale);
			int jHeight = JerryFeatures.getScaledHeight(this.textRenderer, jScale);
			if (isHovered(mouseX, mouseY, jx, jy, jWidth, jHeight)) {
				float updatedScale = Math.max(0.5f, Math.min(3.0f, jScale + (float) verticalAmount * 0.1f));
				JerrySettings.setHudScale(updatedScale);
				int newWidth = JerryFeatures.getScaledWidth(this.textRenderer, updatedScale);
				int newHeight = JerryFeatures.getScaledHeight(this.textRenderer, updatedScale);
				int clampedX = Math.max(0, Math.min(jx, this.width - newWidth));
				int clampedY = Math.max(0, Math.min(jy, this.height - newHeight));
				JerrySettings.setHudPosition(clampedX, clampedY);
				return true;
			}
		}

		if (DarkAuctionTimerSettings.isTimerEnabled()) {
			int x = DarkAuctionTimerSettings.getX();
			int y = DarkAuctionTimerSettings.getY();
			float scale = DarkAuctionTimerSettings.getScale();
			int boxWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, scale);
			int boxHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, scale);
			if (isHovered(mouseX, mouseY, x, y, boxWidth, boxHeight)) {
				float updatedScale = Math.max(0.5f, Math.min(3.0f, scale + (float) verticalAmount * 0.1f));
				DarkAuctionTimerSettings.setScale(updatedScale);

				int newWidth = DarkAuctionTimerHud.getScaledWidth(this.textRenderer, updatedScale);
				int newHeight = DarkAuctionTimerHud.getScaledHeight(this.textRenderer, updatedScale);
				int clampedX = Math.max(0, Math.min(x, this.width - newWidth));
				int clampedY = Math.max(0, Math.min(y, this.height - newHeight));
				DarkAuctionTimerSettings.setPosition(clampedX, clampedY);
				return true;
			}
		}

		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
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

	private enum DragTarget {
		NONE,
		DARK_AUCTION,
		JERRY
	}
}

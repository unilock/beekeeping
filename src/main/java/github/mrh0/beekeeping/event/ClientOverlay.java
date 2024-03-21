package github.mrh0.beekeeping.event;

import com.mojang.blaze3d.systems.RenderSystem;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ClientOverlay {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");
    static int lx = 8, ly = 8;

    public static void renderOverlay(GuiGraphics guiGraphics, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        var player = Minecraft.getInstance().player;
        if (player == null || player.level() == null || !player.getInventory().contains(new ItemStack(ModItems.THERMOMETER)))
            return;

        var temp = BiomeTemperature.of(player.level().getBiome(Minecraft.getInstance().player.getOnPos()).value().getBaseTemperature());

        //drawTextShadowed(stack, new TextComponent("Test"), 10, 10, 1f);
        drawListItem(guiGraphics, temp.getComponent(), 0, 6 + temp.ordinal());
    }

    public static void drawTextShadowed(GuiGraphics guiGraphics, Component text, int x, int y, float scale) {
        drawText(guiGraphics, text.plainCopy(), x + 1, y + 1, scale, 4210752);
        drawText(guiGraphics, text, x, y, scale, 16777215);
    }

    public static void drawText(GuiGraphics guiGraphics, Component text, int x, int y, float scale) {
        drawText(guiGraphics, text, x, y, scale, 4210752);
    }

    public static void drawText(GuiGraphics guiGraphics, Component text, int x, int y, float scale, int color) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(Minecraft.getInstance().font, text, (int) (x / scale), (int) (y / scale), color);
        guiGraphics.pose().popPose();
    }

    private static void drawListItem(GuiGraphics guiGraphics, Component text, int index, int image) {
        drawTextShadowed(guiGraphics, text, lx + 12, ly + 14*index, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(guiGraphics, lx, ly + 14*index, 176, 8*image, 8, 8);
    }

    public static void blit(GuiGraphics guiGraphics, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        guiGraphics.blit(TEXTURE, x, y, 0, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
    }
}

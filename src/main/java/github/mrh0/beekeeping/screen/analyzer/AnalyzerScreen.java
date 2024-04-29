package github.mrh0.beekeeping.screen.analyzer;

import com.mojang.blaze3d.systems.RenderSystem;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Species;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.bee.genes.LightToleranceGene;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.genes.TemperatureToleranceGene;
import github.mrh0.beekeeping.bee.genes.WeatherToleranceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AnalyzerScreen extends BeeScreen<AnalyzerMenu, AnalyzerBlockEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");

    int lx = 14, ly = 45;
    public AnalyzerScreen(AnalyzerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageHeight = 211;
        inventoryLabelY = 121;
    }

    private Bounds getListBounds(int index) {
        return new Bounds(lx, ly + 14*index - 2, imageWidth - lx, 12);
    }

    private void drawListItem(GuiGraphics guiGraphics, Component text, int index, int image) {
        drawText(guiGraphics, text, lx + 12, ly + 14*index, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, lx + getXOffset(), ly + 14*index + getYOffset(), imageWidth, 8*image, 8, 8);
    }

    private Bounds lifetimeBounds = getListBounds(0);
    private Bounds weatherBounds = getListBounds(1);
    private Bounds temperatureBounds = getListBounds(2);
    private Bounds lightBounds = getListBounds(3);
    private Bounds produceBounds = getListBounds(4);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if(getAnalyzed() != null && getSpecies() != null) {
            drawText(guiGraphics, Component.translatable("item.beekeeping.species." + getSpecies().name), 29, 14, 1.75f, getSpecies().color);
        }
        else
            drawText(guiGraphics, Component.translatable("title.beekeeping.analyzer.insert"), 29, 14, 1.75f);
        /*float temp = getLevel().getBiomeManager().getBiome(getBlockPos()).value().getBaseTemperature();
        drawText(poseStack, Component.translatable("title.beekeeping.analyzer.temp").append(": ").append(temp+""), 14, 44, 1f);
        float rain = getLevel().getBiomeManager().getBiome(getBlockPos()).value().getDownfall();
        drawText(poseStack, Component.translatable("title.beekeeping.analyzer.rain").append(": ").append(rain+""), 14, 60, 1f);*/

        if(getSpecies() == null)
            return;

        LightToleranceGene lightTolerance = LightToleranceGene.of(LightToleranceGene.get(getAnalyzed()));
        int lightToleranceImage = lightTolerance == LightToleranceGene.ANY ? 2 : (getSpecies().nocturnal ? 1 : 0);

        int temperatureImage = getSpecies().preferredTemperature.ordinal();

        RareProduceGene rareProduceGene = RareProduceGene.of(RareProduceGene.get(getAnalyzed()));
        int rareProduceBonus = (int)((rareProduceGene.getChance()-1d)*100d);

        int line = 0;
        drawListItem(guiGraphics, LifetimeGene.of(LifetimeGene.get(getAnalyzed())).getComponent(), line++, 4);
        drawListItem(guiGraphics, WeatherToleranceGene.of(WeatherToleranceGene.get(getAnalyzed())).getComponent(), line++, 3);
        drawListItem(guiGraphics, TemperatureToleranceGene.of(TemperatureToleranceGene.get(getAnalyzed())).getComponent()
                .append(" ").append(getSpecies().preferredTemperature.getComponent()), line++, 6 + temperatureImage);
        drawListItem(guiGraphics,
                getSpecies().nocturnal ?
                lightTolerance.getComponent().append(" ").append(Component.translatable("text.beekeeping.nocturnal").withStyle(ChatFormatting.DARK_BLUE)) : lightTolerance.getComponent(),
                line++, lightToleranceImage);
        drawListItem(guiGraphics,
                getSpecies().produce.isRare() ? rareProduceGene.getComponent().append(" ").append((rareProduceBonus >= 0 ? "+" : "") + rareProduceBonus + "%")
                 : Component.literal("").append(rareProduceGene.getComponent().withStyle(ChatFormatting.STRIKETHROUGH))
                                        .append(" ").append(Component.translatable("text.beekeeping.none").withStyle(ChatFormatting.RED)),
                line++, 5);
    }

    private static List<Component> lifetimeDescription = List.of(Component.translatable("tooltip.beekeeping.gene.lifetime"));
    private static List<Component> weatherDescription = List.of(Component.translatable("tooltip.beekeeping.gene.weather"));
    private static List<Component> temperatureDescription = List.of(Component.translatable("tooltip.beekeeping.gene.temperature"));
    private static List<Component> lightDescription = List.of(Component.translatable("tooltip.beekeeping.gene.light"));
    private static List<Component> produceDescription = List.of(Component.translatable("tooltip.beekeeping.gene.produce"));

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        if(getSpecies() == null) return;
        if(lifetimeBounds.in(mouseX, mouseY))
            guiGraphics.renderComponentTooltip(this.font, lifetimeDescription, mouseX, mouseY);
        else if(weatherBounds.in(mouseX, mouseY))
            guiGraphics.renderComponentTooltip(this.font, weatherDescription, mouseX, mouseY);
        else if(temperatureBounds.in(mouseX, mouseY))
            guiGraphics.renderComponentTooltip(this.font, temperatureDescription, mouseX, mouseY);
        else if(lightBounds.in(mouseX, mouseY))
            guiGraphics.renderComponentTooltip(this.font, lightDescription, mouseX, mouseY);
        else if(produceBounds.in(mouseX, mouseY))
            guiGraphics.renderComponentTooltip(this.font, produceDescription, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public ItemStack getAnalyzed() {
        return getBlockEntity().getAnalyzed();
    }

    public Species getSpecies() {
        return BeeItem.getSpecies(getAnalyzed());
    }
}

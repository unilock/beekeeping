package github.mrh0.beekeeping.compat.jei;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.registry.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BeeBreedingRecipeCategory implements IRecipeCategory<BeeBreedingRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(Beekeeping.MODID, "bee_breeding");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/bee_breeding.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BeeBreedingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 112, 32);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.APIARY));
    }

    @Override
    public RecipeType<BeeBreedingRecipe> getRecipeType() {
        return new RecipeType<>(UID, BeeBreedingRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.beekeeping.recipe.bee_breeding");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BeeBreedingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 8).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 8).addIngredients(recipe.getIngredients().get(1));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 8).addItemStack(recipe.getResultItem(null));
    }
}

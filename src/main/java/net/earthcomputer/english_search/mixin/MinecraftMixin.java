package net.earthcomputer.english_search.mixin;

import net.earthcomputer.english_search.EnglishSearch;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "method_1485(Lnet/minecraft/world/item/ItemStack;)Ljava/util/stream/Stream;", at = @At("RETURN"), cancellable = true)
    private static void modifyCreativeNameLines(ItemStack itemStack, CallbackInfoReturnable<Stream<String>> cir) {
        cir.setReturnValue(Stream.concat(
            cir.getReturnValue(),
            itemStack.getTooltipLines(null, TooltipFlag.NORMAL.asCreative()).stream()
                .map(EnglishSearch::toEnglish)
                .map(c -> ChatFormatting.stripFormatting(c.getString()).trim())
                .filter(str -> !str.isEmpty())
        ));
    }

    @Inject(method = "method_43761(Lnet/minecraft/client/gui/screens/recipebook/RecipeCollection;)Ljava/util/stream/Stream;", at = @At("RETURN"), cancellable = true)
    private static void modifyRecipeLines(RecipeCollection recipeCollection, CallbackInfoReturnable<Stream<String>> cir) {
        cir.setReturnValue(Stream.concat(
            cir.getReturnValue(),
            recipeCollection.getRecipes().stream()
                .flatMap(recipe -> recipe.getResultItem(recipeCollection.registryAccess()).getTooltipLines(null, TooltipFlag.NORMAL.asCreative()).stream())
                .map(EnglishSearch::toEnglish)
                .map(c -> ChatFormatting.stripFormatting(c.getString()).trim())
                .filter(str -> !str.isEmpty())
        ));
    }
}

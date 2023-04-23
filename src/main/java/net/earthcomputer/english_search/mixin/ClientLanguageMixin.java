package net.earthcomputer.english_search.mixin;

import net.earthcomputer.english_search.EnglishSearch;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @Inject(method = "appendFrom", at = @At("RETURN"))
    private static void captureEnglishTranslations(String language, List<Resource> resources, Map<String, String> translations, CallbackInfo ci) {
        if ("en_us".equals(language)) {
            EnglishSearch.englishTranslations = new HashMap<>(translations)::getOrDefault;
        }
    }
}

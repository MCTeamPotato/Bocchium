package me.kall.bocchium.mixin;

import me.kall.bocchium.Bocchium;
import net.minecraft.network.chat.Component;
import org.embeddedt.embeddium.api.options.control.TickBoxControl;
import org.embeddedt.embeddium.api.options.structure.*;
import org.embeddedt.embeddium.impl.gui.EmbeddiumGameOptionPages;
import org.embeddedt.embeddium.impl.gui.EmbeddiumOptions;
import org.embeddedt.embeddium.impl.gui.options.storage.EmbeddiumOptionsStorage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = EmbeddiumGameOptionPages.class, remap = false)
public abstract class SodiumGameOptionPagesMixin {
    @Shadow @Final private static EmbeddiumOptionsStorage sodiumOpts;

    @Inject(method = "performance", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void insertSetting(CallbackInfoReturnable<OptionPage> cir, @NotNull List<OptionGroup> groups) {
        OptionImpl<EmbeddiumOptions, Boolean> enableBocchium = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("bocchium.enable"))
                .setTooltip(Component.translatable("bocchium.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (option, value) -> Bocchium.enableBocchium.set(value),
                        option -> Bocchium.enableBocchium.get()
                )
                .setImpact(OptionImpact.LOW)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        OptionImpl<EmbeddiumOptions, Boolean> shouldCullTop = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("bocchium.cull_top"))
                .setTooltip(Component.translatable("bocchium.cull_top.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (option, value) -> Bocchium.shouldCullTopBedrock.set(value),
                        option -> Bocchium.shouldCullTopBedrock.get()
                )
                .setImpact(OptionImpact.LOW)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        OptionImpl<EmbeddiumOptions, Boolean> shouldCullBottom = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("bocchium.cull_bottom"))
                .setTooltip(Component.translatable("bocchium.cull_bottom.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (option, value) -> Bocchium.shouldCullBottomBedrock.set(value),
                        option -> Bocchium.shouldCullBottomBedrock.get()
                )
                .setImpact(OptionImpact.LOW)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        groups.add(OptionGroup.createBuilder().add(enableBocchium).add(shouldCullTop).add(shouldCullBottom).build());
    }
}
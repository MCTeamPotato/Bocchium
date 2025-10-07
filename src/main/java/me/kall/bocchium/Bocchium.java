package me.kall.bocchium;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod(Bocchium.MOD_ID)
public class Bocchium {
    public static final String MOD_ID = "bocchium";
    public static final ModConfigSpec config;
    public static final ModConfigSpec.BooleanValue shouldCullTopBedrock, shouldCullBottomBedrock, enableBocchium;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("BocchiTheBedrock");
        enableBocchium = builder.define("enableBocchium", true);
        shouldCullTopBedrock = builder.define("shouldCullTopBedrock", true);
        shouldCullBottomBedrock = builder.define("shouldCullBottomBedrock", true);
        builder.pop();
        config = builder.build();
    }

    public static boolean canCullBottom(Direction facing, int height) {
        if (!shouldCullBottomBedrock.get()) return false;
        ClientLevel clientWorld = Minecraft.getInstance().level;
        if (clientWorld == null) return false;
        return facing == Direction.DOWN && height == clientWorld.getMinBuildHeight();
    }

    public static boolean canCullTop(Direction facing, int height) {
        if (!shouldCullTopBedrock.get()) return false;
        ClientLevel clientWorld = Minecraft.getInstance().level;
        if (clientWorld == null) return false;
        return facing == Direction.UP && height == clientWorld.getMaxBuildHeight() - 1 && clientWorld.dimensionType().hasCeiling();
    }

    public Bocchium(IEventBus modBus, Dist dist, ModContainer container) {
        if (!dist.isClient()) return;
        container.registerConfig(ModConfig.Type.CLIENT, config);
    }

    public static boolean shouldCull(Direction facing, int height) {
        return (canCullBottom(facing, height) || canCullTop(facing, height)) && enableBocchium.get();
    }
}
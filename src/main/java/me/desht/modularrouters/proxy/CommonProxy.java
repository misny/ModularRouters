package me.desht.modularrouters.proxy;

import me.desht.modularrouters.EnchantBreakerModuleRecipe;
import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.block.ModBlocks;
import me.desht.modularrouters.block.tile.TileEntityItemRouter;
import me.desht.modularrouters.config.Config;
import me.desht.modularrouters.integration.IntegrationHandler;
import me.desht.modularrouters.item.ModItems;
import me.desht.modularrouters.item.module.ItemModule;
import me.desht.modularrouters.item.upgrade.ItemUpgrade;
import me.desht.modularrouters.network.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public static SimpleNetworkWrapper network;

    public void registerItemRenderer(Item item, int meta, String id) {
        // STUB
    }

    public void preInit() {
        Config.preInit();
        ModItems.init();
        ModBlocks.init();

        int d = 0;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModularRouters.modId);
        network.registerMessage(ModuleSettingsMessage.Handler.class, ModuleSettingsMessage.class, d++, Side.SERVER);
        network.registerMessage(RouterSettingsMessage.Handler.class, RouterSettingsMessage.class, d++, Side.SERVER);
        network.registerMessage(ParticleMessage.Handler.class, ParticleMessage.class, d++, Side.CLIENT);
        network.registerMessage(ModuleConfigMessage.Handler.class, ModuleConfigMessage.class, d++, Side.SERVER);
        network.registerMessage(ReopenRouterMessage.Handler.class, ReopenRouterMessage.class, d++, Side.SERVER);

        GameRegistry.registerTileEntity(TileEntityItemRouter.class, "item_router");
    }

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModularRouters.instance, new GuiProxy());
        setupRecipes();
        IntegrationHandler.registerWaila();
        IntegrationHandler.registerTOP();
    }

    public void postInit() {

    }

    private void setupRecipes() {
        GameRegistry.addRecipe(new ItemStack(ModBlocks.itemRouter, 4),
                "ibi", "brb", "ibi",
                'b', Blocks.IRON_BARS, 'r', Items.REDSTONE, 'i', Items.IRON_INGOT);

        GameRegistry.addRecipe(new ItemStack(ModItems.blankModule, 3),
                " r ", "ppp", "nnn",
                'r', Items.REDSTONE, 'p', Items.PAPER, 'n', Items.GOLD_NUGGET);

        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.BREAKER),
                ModItems.blankModule, Items.IRON_PICKAXE);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.DROPPER),
                ModItems.blankModule, Blocks.DROPPER);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.PLACER),
                ModItems.blankModule, Blocks.DISPENSER, Blocks.DIRT);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.SORTER),
                ModItems.blankModule, Items.COMPARATOR, Items.SPIDER_EYE);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.PULLER),
                ModItems.blankModule, Blocks.STICKY_PISTON);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.SENDER1),
                ModItems.blankModule, Items.BOW, Items.ARROW);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.SENDER2),
                ItemModule.makeItemStack(ItemModule.ModuleType.SENDER1), Items.ENDER_EYE);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.SENDER3),
                ItemModule.makeItemStack(ItemModule.ModuleType.SENDER2), Blocks.END_STONE, Blocks.ENDER_CHEST);
        GameRegistry.addShapelessRecipe(ItemModule.makeItemStack(ItemModule.ModuleType.VACUUM),
                ModItems.blankModule, Blocks.HOPPER, Items.ENDER_EYE);

        GameRegistry.addRecipe(new ItemStack(ModItems.blankUpgrade, 4),
                "ppn", "pdn", " pn",
                'p', Items.PAPER, 'd', Items.DIAMOND, 'n', Items.GOLD_NUGGET);
        GameRegistry.addShapelessRecipe(ItemUpgrade.makeItemStack(ItemUpgrade.UpgradeType.SPEED),
                ModItems.blankUpgrade, Items.BLAZE_POWDER, Items.SUGAR, Items.GUNPOWDER, Items.REDSTONE);
        GameRegistry.addShapelessRecipe(ItemUpgrade.makeItemStack(ItemUpgrade.UpgradeType.STACK),
                ModItems.blankUpgrade, Blocks.BRICK_BLOCK, Blocks.STONEBRICK);
        GameRegistry.addShapelessRecipe(ItemUpgrade.makeItemStack(ItemUpgrade.UpgradeType.RANGE),
                ModItems.blankUpgrade, Items.PRISMARINE_SHARD);

        GameRegistry.addRecipe(new EnchantBreakerModuleRecipe());
    }
}

package xaeroplus;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XaeroPlus.MODID, name = XaeroPlus.NAME, version = XaeroPlus.VERSION)
public class XaeroPlus {
    public static final String MODID = "xaeroplus";
    public static final String NAME = "XaeroPlus";
    public static final String VERSION = "1.12.2";
    // keeping this at current 3 levels for now.
    // needs testing around different levels
    // cache sizes get increasingly large and time intensive to generate as levels increase
    public static final int MAX_LEVEL = 3; // 5;

    @Mod.Instance
    public static XaeroPlus INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) { }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) { }
}

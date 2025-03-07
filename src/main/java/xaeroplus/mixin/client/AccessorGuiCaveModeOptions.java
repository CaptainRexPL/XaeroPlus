package xaeroplus.mixin.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import xaero.map.gui.GuiCaveModeOptions;
import xaero.map.world.MapDimension;

@Mixin(value = GuiCaveModeOptions.class, remap = false)
public interface AccessorGuiCaveModeOptions {

    @Accessor
    void setDimension(MapDimension dimension);

    @Accessor
    GuiButton getCaveModeStartSlider();

    @Accessor
    GuiTextField getCaveModeStartField();

    @Invoker("getCaveModeTypeButtonMessage")
    ITextComponent invokeCaveModeTypeButtonMessage();

}

package org.cneko.justarod;


import net.fabricmc.api.ModInitializer;
import org.cneko.justarod.api.NetWorkingRodData;
import org.cneko.justarod.block.JRBlocks;
import org.cneko.justarod.command.JRCommands;
import org.cneko.justarod.effect.JREffects;
import org.cneko.justarod.entity.JREntities;
import org.cneko.justarod.event.*;
import org.cneko.justarod.item.JRItems;
import org.cneko.justarod.packet.JRPackets;
import org.cneko.justarod.quirks.JRQuirks;


public class Justarod implements ModInitializer {
    public static final String MODID = "justarod";

    @Override
    public void onInitialize() {
        NetWorkingRodData.Companion.init();
        JRItems.Companion.init();
        JRBlocks.init();
        JREffects.Companion.init();
        JRAttributes.Companion.init();
        JRQuirks.Companion.init();
        EntityAttackEvent.init();
        MessagingEvent.Companion.init();
        JREntities.init();
        TickEvent.Companion.init();
        JRCommands.init();
        JRPackets.init();
        JRNetWorkingEvents.init();
        JRCriteria.init();

        EntityDeathEvent.init();
        EntityRespawnEvent.init();

    }
}

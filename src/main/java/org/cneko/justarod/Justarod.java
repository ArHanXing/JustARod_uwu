package org.cneko.justarod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.cneko.justarod.api.NetWorkingRodData;
import org.cneko.justarod.block.JRBlocks;
import org.cneko.justarod.command.JRCommands;
import org.cneko.justarod.effect.JREffects;
import org.cneko.justarod.entity.JREntities;
import org.cneko.justarod.event.*;
import org.cneko.justarod.item.JRItems;
import org.cneko.justarod.packet.JRPackets;
import org.cneko.justarod.quirks.JRQuirks;

/* TODO
    2. 将欲望水晶的注册移植到mod本体中来？
    5. 实现玩家检测器的获取末地烛速度
    4. 实现物品化的猫娘及其压榨问题（物品nbt问题在这里处理
    6. 单独写一个NeoForge模组实现猫娘处理（兼容性，唉唉/机器逻辑在那里处理）
 */

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

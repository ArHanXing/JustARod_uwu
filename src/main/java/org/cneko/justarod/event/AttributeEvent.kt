package org.cneko.justarod.event

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.cneko.justarod.entity.PersistentAttributeManager

object AttributeEvent {
    fun register() {
        // 玩家重生时复制属性
        ServerPlayerEvents.COPY_FROM.register { oldPlayer, newPlayer, alive ->
            if (!alive) { // 玩家死亡后重生
                // 保存旧玩家数据
                PersistentAttributeManager.savePlayerData(oldPlayer)

                // 加载并应用到新玩家
                PersistentAttributeManager.loadPlayerData(newPlayer)
            }
        }

        // 玩家加入时确保属性存在
        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            PersistentAttributeManager.loadPlayerData(handler.player)
        }

        // 玩家退出时保存数据
        ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
            PersistentAttributeManager.savePlayerData(handler.player)
        }
    }
}
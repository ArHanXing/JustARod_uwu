package org.cneko.justarod

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.cneko.justarod.Justarod.MODID
import org.cneko.toneko.common.mod.entities.INeko


// awa
class JRUtil {
    companion object {
        fun World.getNekoInRange(entity: Entity, radius: Float): List<INeko> {
            val box = Box(
                entity.x - radius.toDouble(),
                entity.y - radius.toDouble(),
                entity.z - radius.toDouble(),
                entity.x + radius.toDouble(),
                entity.y + radius.toDouble(),
                entity.z + radius.toDouble()
            )
            val entities = this.getNonSpectatingEntities(LivingEntity::class.java, box)
            return entities.filter { it is INeko  && it != entity } as List<INeko>
        }

        fun World.getPlayerInRange(entity: Entity, radius: Float): List<PlayerEntity> {
            val box = Box(
                entity.x - radius.toDouble(),
                entity.y - radius.toDouble(),
                entity.z - radius.toDouble(),
                entity.x + radius.toDouble(),
                entity.y + radius.toDouble(),
                entity.z + radius.toDouble()
            )
            val entities = this.getNonSpectatingEntities(PlayerEntity::class.java, box)
            return entities.filter {it != entity}
        }
        fun rodId(path:String): Identifier{
            return Identifier.of(MODID, path)
        }

        fun ItemStack.containsEnchantment(enchantment: RegistryKey<Enchantment>): Boolean{
            return this.hasEnchantments() && this.enchantments.enchantments.any { e ->
                if(e.key.isPresent){
                    return e.key.get().value.equals(enchantment.value)
                }
                return false
            }
        }
        fun ItemStack.getEnchantmentLevel(world : World,enchantment: RegistryKey<Enchantment>): Int {
            val rm = world.registryManager
            val level = this.enchantments.getLevel(rm?.get(RegistryKeys.ENCHANTMENT)?.entryOf(enchantment))

            return 0
        }
        // ds最新最热大作，掉掉物
        // 首先获取Item对象
        fun getItemById(itemId: String): Item {
            val identifier = Identifier.of(itemId)
            return Registries.ITEM.get(identifier) // 从物品注册表获取[citation:10]
        }

        // 然后创建ItemStack
        fun createItemStack(itemId: String, count: Int): ItemStack {
            val item = getItemById(itemId)
            // 检查物品是否存在且有效
            return if (item !== Items.AIR) {
                // Item类通常实现了ItemConvertible接口
                ItemStack(item, count)
            } else {
                // 处理物品不存在的情况，例如记录日志或返回默认物品
                //println("物品不存在: " + itemId)
                ItemStack(Items.AIR, count) // 或者返回 null
            }
        }
    }

}

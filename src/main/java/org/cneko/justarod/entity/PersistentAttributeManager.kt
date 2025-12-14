package org.cneko.justarod.entity

import net.minecraft.entity.attribute.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object PersistentAttributeManager {
    private val playerData = mutableMapOf<String, PersistentModifierData>()

    // 给玩家添加永久属性修饰符
    fun addPersistentModifier(
        player: ServerPlayerEntity,
        attribute: RegistryEntry<EntityAttribute>, // 修改这里
        modifierId: Identifier,
        amount: Double,
        operation: EntityAttributeModifier.Operation
    ) {
        // 先添加到玩家属性
        val modifier = EntityAttributeModifier(
            modifierId,
            amount,
            operation
        )

        player.getAttributeInstance(attribute)?.addPersistentModifier(modifier) // 这里直接使用RegistryEntry

        // 保存到数据
        val uuid = player.uuidAsString
        val data = playerData.getOrPut(uuid) { PersistentModifierData() }

        // 保存注册表ID
        val attributeId = Registries.ATTRIBUTE.getId(attribute.value())
        attributeId?.let {
            data.addModifier(
                PersistentModifier(
                    it,
                    modifierId,
                    amount,
                    operation.id
                )
            )
        }

        // 保存到玩家NBT
        savePlayerData(player)
    }

    // 从EntityAttribute转换为RegistryEntry的帮助函数
    fun getAttributeRegistryEntry(attribute: EntityAttribute): RegistryEntry<EntityAttribute> {
        return Registries.ATTRIBUTE.getEntry(attribute)
    }

    // 从Identifier获取RegistryEntry的帮助函数
    fun getAttributeRegistryEntry(attributeId: Identifier): RegistryEntry<EntityAttribute> {
        return Registries.ATTRIBUTE.getEntry(attributeId).orElse(null)
            ?: throw IllegalArgumentException("Attribute not found: $attributeId")
    }

    // 方便的重载函数，接受EntityAttribute参数
    fun addPersistentModifier(
        player: ServerPlayerEntity,
        attribute: EntityAttribute, // 保留这个重载
        modifierId: Identifier,
        amount: Double,
        operation: EntityAttributeModifier.Operation
    ) {
        val registryEntry = getAttributeRegistryEntry(attribute)
        addPersistentModifier(player, registryEntry, modifierId, amount, operation)
    }

    // 从玩家NBT加载数据
    fun loadPlayerData(player: ServerPlayerEntity) {
        val nbt = player.writeNbt(NbtCompound())

        if (nbt.contains("PersistentAttributes")) {
            val dataNbt = nbt.getCompound("PersistentAttributes")
            val data = PersistentModifierData.fromNbt(dataNbt)

            playerData[player.uuidAsString] = data

            // 重新应用所有修饰符
            applyModifiers(player, data)
        }
    }

    // 保存玩家数据到NBT
    fun savePlayerData(player: ServerPlayerEntity) {
        val uuid = player.uuidAsString
        val data = playerData[uuid] ?: return

        val nbt = player.writeNbt(NbtCompound())
        nbt.put("PersistentAttributes", data.toNbt())
        player.readNbt(nbt)
    }

    // 应用所有保存的修饰符
    private fun applyModifiers(player: ServerPlayerEntity, data: PersistentModifierData) {
        data.getModifiers().forEach { modifier ->
            try {
                val attributeEntry = getAttributeRegistryEntry(modifier.attributeId)
                val operation = idToOperation(modifier.operation)
                val attributeInstance = player.getAttributeInstance(attributeEntry)
                if (attributeInstance != null) {
                    val existing = attributeInstance.getModifier(modifier.modifierId)
                    if (existing == null) {
                        val newModifier = EntityAttributeModifier(
                            modifier.modifierId,
                            modifier.amount,
                            operation
                        )
                        attributeInstance.addPersistentModifier(newModifier)
                    }
                }
            } catch (e: IllegalArgumentException) {
                // 属性未找到，跳过
                println("Warning: Attribute ${modifier.attributeId} not found, skipping modifier.")
            }
        }
    }

    // 清除玩家数据
    fun clearPlayerData(player: ServerPlayerEntity) {
        playerData.remove(player.uuidAsString)
    }

    // 移除指定修饰符
    fun removePersistentModifier(
        player: ServerPlayerEntity,
        attribute: RegistryEntry<EntityAttribute>,
        modifierId: Identifier
    ) {
        player.getAttributeInstance(attribute)?.removeModifier(modifierId)

        val uuid = player.uuidAsString
        val data = playerData[uuid]
        if (data != null) {
            // 从数据中移除
            data.removeModifier(modifierId)
            savePlayerData(player)
        }
    }

    // 重载版本
    fun removePersistentModifier(
        player: ServerPlayerEntity,
        attribute: EntityAttribute,
        modifierId: Identifier
    ) {
        val registryEntry = getAttributeRegistryEntry(attribute)
        removePersistentModifier(player, registryEntry, modifierId)
    }

    fun idToOperation(id: Int): EntityAttributeModifier.Operation {
        return when (id) {
            0 -> EntityAttributeModifier.Operation.ADD_VALUE
            1 -> EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            2 -> EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            else -> EntityAttributeModifier.Operation.ADD_VALUE // 默认
        }
    }
}
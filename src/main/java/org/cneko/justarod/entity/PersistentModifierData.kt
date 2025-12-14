package org.cneko.justarod.entity

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.util.Identifier

data class PersistentModifier(
    val attributeId: Identifier,
    val modifierId: Identifier,
    val amount: Double,
    val operation: Int
)

class PersistentModifierData {
    private val modifiers = mutableListOf<PersistentModifier>()

    fun addModifier(modifier: PersistentModifier) {
        modifiers.add(modifier)
    }

    fun removeModifier(modifierId: Identifier) {
        modifiers.removeIf { it.modifierId == modifierId }
    }

    fun getModifiers(): List<PersistentModifier> = modifiers

    fun clear() {
        modifiers.clear()
    }

    fun toNbt(): NbtCompound {
        val nbt = NbtCompound()
        val list = NbtList()

        modifiers.forEach { modifier ->
            val compound = NbtCompound()
            compound.putString("attribute", modifier.attributeId.toString())
            compound.putString("modifier", modifier.modifierId.toString())
            compound.putDouble("amount", modifier.amount)
            compound.putInt("operation", modifier.operation)
            list.add(compound)
        }

        nbt.put("modifiers", list)
        return nbt
    }

    companion object {
        fun fromNbt(nbt: NbtCompound): PersistentModifierData {
            val data = PersistentModifierData()

            if (nbt.contains("modifiers", NbtElement.LIST_TYPE.toInt())) {
                val list = nbt.getList("modifiers", NbtElement.COMPOUND_TYPE.toInt())

                for (i in 0 until list.size) {
                    val compound = list.getCompound(i)
                    val attributeId = Identifier.of(compound.getString("attribute"))
                    val modifierId = Identifier.of(compound.getString("modifier"))
                    val amount = compound.getDouble("amount")
                    val operation = compound.getInt("operation")

                    data.addModifier(PersistentModifier(attributeId, modifierId, amount, operation))
                }
            }

            return data
        }
    }
}
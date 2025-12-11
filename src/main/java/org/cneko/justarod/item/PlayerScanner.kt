package org.cneko.justarod.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.cneko.justarod.JRAttributes

/*
PlayerScanner

输出玩家的各项指标
 */
class PlayerScannerItem : Item(Settings().maxCount(1)) {
    fun __speak(str: String?,user: PlayerEntity?){
        val _x = Text.of(str)
        user?.sendMessage(_x)
    }
    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user ?: return super.use(world, user, hand)
        val handStack = user.offHandStack

        if (world?.isClient == true) {
            val _dev_rate = user.getAttributeValue(JRAttributes.PLAYER_DEVELOP_RATE)
            val _lubr = user.getAttributeValue(JRAttributes.PLAYER_LUBRICATING)
            val _xpower = user.power

            //改平衡的时候记得把这里也改掉
            __speak("§b================",user)
            __speak("§b目前你的§d开发度§b： §e$_dev_rate",user)
            __speak("§b目前你的§f润滑值§b： §e$_lubr",user)
            __speak("§b目前你的§6体力§b： §e$_xpower",user)
            __speak("§b================",user)
            __speak("§b目前你的§6末地烛速度倍率§b： §e"+(_dev_rate*_lubr),user)
            __speak("§b这个倍率将会在你使用末地烛时，与末地烛的基础速度乘算",user)
            __speak("§b你还可以承受§e"+(_xpower/(0.0025*100)).toInt()+"次 速度为100的末地烛使用",user)
            __speak("§b你还可以承受§e"+(_xpower/(0.0025*500).toInt())+"次 速度为500的末地烛使用",user)
            __speak("§b================",user)

            val _speed = (handStack.item as EndRodItem).getRodSpeed(handStack)
        }


        return TypedActionResult.pass(handStack)
    }
}
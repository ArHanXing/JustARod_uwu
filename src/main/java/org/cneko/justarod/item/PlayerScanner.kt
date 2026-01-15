package org.cneko.justarod.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.cneko.justarod.JRAttributes
import kotlin.math.floor
import kotlin.math.round

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
        if (hand == Hand.OFF_HAND) {
            if (world?.isClient == true) {
                __speak("§c扫描器只能在主手使用！", user)
            }
            return TypedActionResult.fail(user.getStackInHand(hand))
        }
        if (world?.isClient == true) {
            val _dev_rate = user.getAttributeValue(JRAttributes.PLAYER_DEVELOP_RATE)
            val _lubr = user.getAttributeValue(JRAttributes.PLAYER_LUBRICATING)
            val _xpower = user.power

            //改平衡的时候记得把这里也改掉
            __speak("§b================",user)
            __speak("§b目前你的§d开发度§b： §e"+round((_dev_rate)*100)*0.01,user)
            __speak("§b目前你的§f润滑值§b： §e"+round((_lubr)*100)*0.01,user)
            __speak("§b目前你的§6体力§b： §e"+round((_xpower)*100)*0.01,user)
            __speak("§b================",user)
            __speak("§b目前你的§6末地烛速度倍率§b： §e"+round(((_dev_rate+1)*_lubr)*100)*0.01,user)
            __speak("§b你还可以承受§e"+ floor(_xpower/(0.0025*1000)) +"次 速度为1000的末地烛使用",user)
            __speak("§b你还可以承受§e"+ floor(_xpower/(0.0025*5000)) +"次 速度为5000的末地烛使用",user)
            __speak("§b================",user)

            if(handStack.item is EndRodItem) {
                val _speed = (handStack.item as EndRodItem).getRodSpeed(handStack)
                __speak("§b副手末地烛的§e速度§b： §e$_speed",user)
                __speak("§b副手末地烛的§e实际速度§b： §e"+round(((_dev_rate+1)*_lubr*_speed)*100)*0.01,user)
                __speak("§b你还可以承受§e"+ floor(_xpower/(0.0025*_speed*_dev_rate)) +"次 这根末地烛的使用",user)
                __speak("§b================",user)

            }
            else
            {
                __speak("§b开发度会在使用末地烛后更新！",user)
            }
        }

        return TypedActionResult.fail(user.getStackInHand(hand))
        //这里我万策尽了
        //如果传 pass 扫描器直接就没了
        //抽象
    }
}
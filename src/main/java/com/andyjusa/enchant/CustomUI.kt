package com.andyjusa.enchant

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class CustomUI(private val size: Int, private val title: String) {
    private val inventory: Inventory = Bukkit.createInventory(null, size, title)

    fun addItem(vararg items: ItemStack?) {
        for (item in items) {
            inventory.addItem(item)
        }
    }

    fun setItem(slot: Int, item: ItemStack) {
        inventory.setItem(slot, item)
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    companion object {
        fun create(size: Int, title: String, vararg items: ItemStack): CustomUI {
            val inventory = CustomUI(size, title)
            inventory.addItem(*items)
            return inventory
        }

        fun create(size: Int, title: String, filler: Material): CustomUI {
            val inventory = CustomUI(size, title)
            for (i in 0 until size) {
                inventory.setItem(i, ItemStack(filler))
            }
            return inventory
        }
    }
}

package com.andyjusa.enchant

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.math.max
import kotlin.random.Random

class Event : Listener {
    private val enchantUIName = "강화창"

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.view.title == enchantUIName) {
            event.player.inventory.addItem(event.inventory.getItem(6))
            event.player.inventory.addItem(event.inventory.getItem(7))
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventoryView = event.view
        val clickedItem = event.currentItem
        val inventoryName = inventoryView.title
        val player = event.whoClicked as Player
        if (inventoryName == enchantUIName) {
            event.isCancelled = true
            when (clickedItem?.type) {

                Material.BOOK -> {

                }

                Material.ENCHANTED_BOOK -> {
                    if (event.clickedInventory?.size == 9) {
                        player.openInventory.setItem(
                            event.slot,
                            ItemStack(Material.BARRIER).also { it.addUnsafeEnchantments(clickedItem.enchantments) })
                    }
                }

                Material.BARRIER -> {
                    if (event.clickedInventory?.size == 9) {
                        player.openInventory.setItem(
                            event.slot,
                            ItemStack(Material.ENCHANTED_BOOK).also { it.addUnsafeEnchantments(clickedItem.enchantments) })
                    }
                }

                Material.EXPERIENCE_BOTTLE -> {
                    val item = player.openInventory.getItem(6)
                    player.openInventory.setItem(6, ItemStack(Material.AIR))
                    player.inventory.addItem(item)
                }

                Material.IRON_INGOT, Material.DIAMOND -> {
                    if (event.clickedInventory?.size != 9) {
                        player.openInventory.setItem(7, clickedItem)
                        player.inventory.setItem(event.slot, ItemStack(Material.AIR))
                    } else {
                        player.inventory.addItem(player.openInventory.getItem(7))
                        player.openInventory.setItem(7, ItemStack(Material.AIR))
                    }
                }

                Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_SWORD, Material.IRON_AXE, Material.IRON_AXE, Material.IRON_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_HOE, Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_SWORD, Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_HOE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_BOOTS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HOE, Material.NETHERITE_HELMET, Material.NETHERITE_LEGGINGS, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD -> {
                    if (event.clickedInventory?.size != 9) {
                        player.inventory.setItem(event.slot, ItemStack(Material.AIR))
                        player.openInventory.setItem(6, clickedItem)
                        val enchantmentsList = ArrayList<Enchantment>()
                        clickedItem?.enchantments?.forEach { enchantment ->
                            enchantmentsList.add(enchantment.key)
                        }
                        for (i in 0 until 5) {
                            val item: ItemStack
                            if (i < enchantmentsList.size) {
                                item = ItemStack(Material.ENCHANTED_BOOK)
                                item.addUnsafeEnchantment(
                                    enchantmentsList[i],
                                    clickedItem.getEnchantmentLevel(enchantmentsList[i])
                                )

                            } else {
                                item = ItemStack(Material.BOOK)
                            }
                            player.openInventory.setItem(i, item)
                        }
                    } else {
                        var e = clickedItem?.enchantments

                        val item = clickedItem
                        val enchantList = arrayListOf(*Enchantment.values())
                        val needEnchant = arrayListOf<Enchantment>()
                        var count = 0
                        var noMoney = false
                        var disenchantList = arrayListOf<Enchantment>()

                        enchantList.remove(Enchantment.BINDING_CURSE)
                        enchantList.remove(Enchantment.VANISHING_CURSE)
                        for (i in 0 until item.enchantments.size) {
                            var enchant: Enchantment = Enchantment.BINDING_CURSE
                            player.openInventory.getItem(i)?.enchantments?.forEach {
                                enchant = it.key
                            }

                            if (player.openInventory.getItem(i)?.type == Material.BARRIER) {
                                needEnchant.add(enchant)
                            } else {
//                                item.removeEnchantment(enchant)
                                disenchantList.add(enchant)
                                count++
                            }
                        }

                        player.openInventory.getItem(7).let {
                            if (it?.type == Material.DIAMOND && it.amount >= needEnchant.size) {
                                player.sendMessage("ass")
                                player.openInventory.setItem(
                                    7,
                                    it.also {
                                        if (it.amount - max(needEnchant.size, 1) > 0) {
                                            it.amount -= max(needEnchant.size, 1)
                                        } else {
                                            it.type = Material.AIR
                                        }
                                    })
                            } else if (it?.type == Material.IRON_INGOT && it.amount > 0 && needEnchant.isEmpty()) {
                                player.openInventory.setItem(7, it.also {
                                    if (it.type != Material.AIR) {
                                        it.amount -= 1
                                    } else {
                                        it.type = Material.AIR
                                    }
                                })
                            } else {
                                noMoney = true
                            }
                        }

                        if (noMoney) {
                            player.sendMessage("자원이 딸림")
                        } else {
                            disenchantList.forEach { item.removeEnchantment(it) }
                            needEnchant.forEach { enchantList.remove(it) }



                            for (i in 1..count) {
                                val enchant = enchantList.random()
                                enchantList.remove(enchant)
                                val level = Random.nextInt(1, enchant.maxLevel + 1)
                                item.addUnsafeEnchantment(enchant, level)
                            }
                            if (item.enchantments.isEmpty() || (item.enchantments.size < 5 && Random.nextInt(
                                    0,
                                    50
                                ) == 35)
                            ) {
                                val enchant = enchantList.random()
                                enchantList.remove(enchant)
                                val level = Random.nextInt(1, enchant.maxLevel + 1)
                                item.addUnsafeEnchantment(enchant, level)
                            }
                            player.openInventory?.setItem(6, item)


                            val enchantmentsList = ArrayList<Enchantment>()
                            clickedItem?.enchantments?.forEach { enchantment ->
                                enchantmentsList.add(enchantment.key)
                            }
                            player.sendMessage(enchantmentsList[0].toString())

                            for (i in 0 until enchantmentsList.size) {
                                val item =
                                    ItemStack(if (needEnchant.size > i) Material.BARRIER else Material.ENCHANTED_BOOK)
                                item.addUnsafeEnchantment(
                                    enchantmentsList[i],
                                    clickedItem.getEnchantmentLevel(enchantmentsList[i])
                                )
                                player.openInventory.setItem(i, item)
                            }
                        }
                    }


                }

                else -> {
                    event.isCancelled = false
                }
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val clickedBlock: Block? = event.clickedBlock
        when (clickedBlock?.type) {
            Material.ENCHANTING_TABLE -> {
                when (event.action) {
                    Action.RIGHT_CLICK_BLOCK -> {
                        event.isCancelled = true
                        val customUI = CustomUI.create(9, enchantUIName)
                        customUI.setItem(0, ItemStack(Material.BOOK))
                        customUI.setItem(1, ItemStack(Material.BOOK))
                        customUI.setItem(2, ItemStack(Material.BOOK))
                        customUI.setItem(3, ItemStack(Material.BOOK))
                        customUI.setItem(4, ItemStack(Material.BOOK))
                        customUI.setItem(8, ItemStack(Material.EXPERIENCE_BOTTLE))
                        customUI.open(player)
                    }

                    else -> {

                    }
                }
            }

            else -> {

            }
        }
    }
}

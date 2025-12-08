package com.thesatyric.overly_complicated_garbage;

import com.thesatyric.overly_complicated_garbage.items.AshDustItem;
import com.thesatyric.overly_complicated_garbage.items.HeldPlasticBagItem;
import com.thesatyric.overly_complicated_garbage.items.PlasticBagItem;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.injection.At;

import javax.xml.crypto.Data;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class OCGarbageItems {

    public static final Item ASH_DUST = register("ash_dust", AshDustItem::new, new Item.Settings().fireproof());
//    public  static final Item ASH_BLOCK = register("ash_block", OCGarbageBlocks.ASH_BLOCK, new BlockItem.Settings().fireproof());
    public static final Item CACTUS_PRICKLES = register("cactus_prickles", OCGarbageBlocks.CACTUS_PLACED_PRICKLES, new BlockItem.Settings()
        .component(OCGarbageComponents.PRICKLY_COMPONENT, Boolean.TRUE));
    public static final Item BIOMASS_PROCESSOR = register("biomass_processor", OCGarbageBlocks.BIOMASS_PROCESSOR, new BlockItem.Settings());
    public static final Item ECO_FRIENDLY_PLASTIC = register("eco_friendly_plastic", Item::new, new Item.Settings());
    public static final Item ECO_FRIENDLY_PLASTIC_BLOCK = register("eco_friendly_plastic_block", OCGarbageBlocks.ECO_FRIENDLY_PLASTIC_BLOCK, new BlockItem.Settings());
    public static final TagKey<Item> REPAIRS_BAG = TagKey.of(Registries.ITEM.getKey(), Identifier.of(OverlyComplicatedGarbage.MOD_ID, "repairs_bag"));
    public static final RegistryKey<EquipmentAsset> PLASTIC_BAG_KEY =
            RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(OverlyComplicatedGarbage.MOD_ID, "plastic_bag"));
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            -1,
            Map.of(
                    EquipmentType.HELMET, 0,
                    EquipmentType.CHESTPLATE, 0,
                    EquipmentType.LEGGINGS, 0,
                    EquipmentType.BOOTS, 0
            ),
            0,
            RegistryEntry.of(SoundEvents.ITEM_BUNDLE_INSERT),
            0.0F,
            0.0F,
            REPAIRS_BAG,
            PLASTIC_BAG_KEY
    );
    public static final Item PLASTIC_BAG = register("plastic_bag", PlasticBagItem::new, new PlasticBagItem.Settings()
            .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.HEAD).model(PLASTIC_BAG_KEY).swappable(false).build())
            .maxCount(1));
    public static final Item BROKEN_PLASTIC_BAG = register("broken_plastic_bag", Item::new, new Item.Settings().maxCount(1));
    public static final Item HELD_PLASTIC_BAG = register("held_plastic_bag", HeldPlasticBagItem::new, new Item.Settings()
            .maxCount(1)
            .component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)
            .attributeModifiers(AttributeModifiersComponent.builder().add(EntityAttributes.MOVEMENT_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("movement_speed"), -0.05f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY).build()));
    public static final Item TRASH_CAN = register("trash_can", OCGarbageBlocks.TRASH_CAN, new BlockItem.Settings());
    public static final Item GARBAGE_BLOCK = register("garbage_block", OCGarbageBlocks.GARBAGE_BLOCK, new BlockItem.Settings());
    public static final Item SUSPICIOUS_GARBAGE_BLOCK = register("suspicious_garbage_block", OCGarbageBlocks.SUSPICIOUS_GARBAGE_BLOCK, new BlockItem.Settings());
    public static void initialize()
    {

    }

    public static Item register(String name, Block block, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));
        BlockItem blockItem = new BlockItem(block, settings.registryKey(itemKey));

        return register(itemKey, blockItem);
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OverlyComplicatedGarbage.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));


        return register(itemKey, item);
    }

    public static Item register(RegistryKey<Item> itemKey, Item item)
    {
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

}

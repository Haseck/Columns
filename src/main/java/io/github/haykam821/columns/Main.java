package io.github.haykam821.columns;

import io.github.haykam821.columns.block.ColumnBlock;
import io.github.haykam821.columns.block.ColumnTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {
	private static final String MOD_ID = "columns";

	private static final Identifier COLUMNS_ID = Main.id("columns");

	public static final TagKey<Block> COLUMNS_BLOCK_TAG = TagKey.of(RegistryKeys.BLOCK, COLUMNS_ID);
	public static final TagKey<Item> COLUMNS_ITEM_TAG = TagKey.of(RegistryKeys.ITEM, COLUMNS_ID);

	private static final Identifier COLUMN_ID = Main.id("column");

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK_TYPE, COLUMN_ID, ColumnBlock.CODEC);

		ColumnTypes.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
package io.github.haykam821.columns.data.provider;

import java.util.concurrent.CompletableFuture;

import io.github.haykam821.columns.block.ColumnTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

public class ColumnsBlockLootTableProvider extends FabricBlockLootTableProvider {
	public ColumnsBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
		super(dataOutput, registries);
	}

	@Override
	public void generate() {
		for (ColumnTypes columnType : ColumnTypes.values()) {
			this.addDrop(columnType.block);
		}

		this.lootTables.forEach((id, lootTable) -> {
			lootTable.randomSequenceId(id.getValue());
		});
	}
}

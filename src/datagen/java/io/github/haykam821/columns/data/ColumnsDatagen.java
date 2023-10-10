package io.github.haykam821.columns.data;

import io.github.haykam821.columns.data.provider.ColumnsBlockLootTableProvider;
import io.github.haykam821.columns.data.provider.ColumnsBlockTagProvider;
import io.github.haykam821.columns.data.provider.ColumnsItemTagProvider;
import io.github.haykam821.columns.data.provider.ColumnsModelProvider;
import io.github.haykam821.columns.data.provider.ColumnsRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class ColumnsDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();

		pack.addProvider(ColumnsBlockLootTableProvider::new);

		FabricTagProvider.BlockTagProvider blockTags = pack.addProvider(ColumnsBlockTagProvider::new);

		pack.addProvider((dataOutput, registries) -> new ColumnsItemTagProvider(dataOutput, registries, blockTags));

		pack.addProvider(ColumnsModelProvider::new);

		pack.addProvider(ColumnsRecipeProvider::new);
	}
}

package io.github.haykam821.columns.data.provider;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.github.haykam821.columns.block.ColumnTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements.CriterionMerger;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class ColumnsRecipeProvider extends FabricRecipeProvider {
	public ColumnsRecipeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
		super(dataOutput, registries);
	}

	@Override
	public void generate(RecipeExporter exporter) {
		for (ColumnTypes columnType : ColumnTypes.values()) {
			ColumnsRecipeProvider.offerColumnRecipe(exporter, columnType.block, columnType.base);
			ColumnsRecipeProvider.offerColumnStonecuttingRecipe(exporter, columnType.block, columnType.base);
		}

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.DEEPSLATE_BRICK.block, Blocks.COBBLED_DEEPSLATE);
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.DEEPSLATE_BRICK.block, Blocks.POLISHED_DEEPSLATE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.DEEPSLATE_TILE.block, Blocks.COBBLED_DEEPSLATE);
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.DEEPSLATE_TILE.block, Blocks.DEEPSLATE_BRICKS);
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.DEEPSLATE_TILE.block, Blocks.POLISHED_DEEPSLATE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.END_STONE_BRICK.block, Blocks.END_STONE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.POLISHED_BLACKSTONE_BRICK.block, Blocks.BLACKSTONE);
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.POLISHED_BLACKSTONE_BRICK.block, Blocks.POLISHED_BLACKSTONE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.POLISHED_BLACKSTONE.block, Blocks.BLACKSTONE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.POLISHED_DEEPSLATE.block, Blocks.COBBLED_DEEPSLATE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.POLISHED_TUFF.block, Blocks.TUFF);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.STONE_BRICK.block, Blocks.STONE);

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.TUFF_BRICK.block, Blocks.TUFF);
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, ColumnTypes.TUFF_BRICK.block, Blocks.POLISHED_TUFF);
	}

	public static void offerColumnRecipe(RecipeExporter exporter, Block block, Block base) {
		ColumnsRecipeProvider.offerCraftingTo(exporter, ColumnsRecipeProvider.getColumnRecipe(block, Ingredient.ofItems(base))
			.criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base)));
	}

	public static ShapedRecipeJsonBuilder getColumnRecipe(ItemConvertible output, Ingredient input) {
		return ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, output, 6)
			.input('#', input)
			.pattern("###")
			.pattern(" # ")
			.pattern("###");
	}

	public static void offerColumnStonecuttingRecipe(RecipeExporter exporter, Block block, Block base) {
		Identifier blockId = Registries.ITEM.getId(block.asItem());
		Identifier recipeId = blockId.withSuffixedPath("_from_stonecutting");

		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, recipeId, block, base);
	}

	public static void offerCustomColumnStonecuttingRecipe(RecipeExporter exporter, Block block, Block base) {
		Identifier baseId = Registries.ITEM.getId(base.asItem());
		Identifier blockId = Registries.ITEM.getId(block.asItem());

		Identifier recipeId = blockId.withPath(path -> path + "_from_" + baseId.getPath() + "_stonecutting");
		ColumnsRecipeProvider.offerCustomColumnStonecuttingRecipe(exporter, recipeId, block, base);
	}

	private static void offerCustomColumnStonecuttingRecipe(RecipeExporter exporter, Identifier recipeId, Block block, Block base) {
		ColumnsRecipeProvider.offerSingleItemTo(exporter, recipeId, StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(base), RecipeCategory.DECORATIONS, block, 1)
			.criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base)));
	}

	private static void offerCraftingTo(RecipeExporter exporter, ShapedRecipeJsonBuilder factory) {
		Identifier recipeId = CraftingRecipeJsonBuilder.getItemId(factory.getOutputItem());
		ColumnsRecipeProvider.offerShapedTo(exporter, recipeId, factory);
	}

	private static void offerShapedTo(RecipeExporter exporter, Identifier recipeId, ShapedRecipeJsonBuilder factory) {
		RawShapedRecipe rawRecipe = factory.validate(recipeId);

		Identifier advancementId = ColumnsRecipeProvider.getAdvancementId(recipeId);
		Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(CriterionMerger.OR);

		factory.criteria.forEach(advancementBuilder::criterion);

		AdvancementEntry advancement = advancementBuilder.build(advancementId);

		String group = Objects.requireNonNullElse(factory.group, "");
		CraftingRecipeCategory category = CraftingRecipeJsonBuilder.toCraftingCategory(factory.category);
		ItemStack output = new ItemStack(factory.getOutputItem(), factory.count);

		ShapedRecipe recipe = new ShapedRecipe(group, category, rawRecipe, output, factory.showNotification);
		exporter.accept(recipeId, recipe, advancement);
	}

	private static void offerSingleItemTo(RecipeExporter exporter, Identifier recipeId, StonecuttingRecipeJsonBuilder factory) {
		factory.validate(recipeId);

		Identifier advancementId = ColumnsRecipeProvider.getAdvancementId(recipeId);
		Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(CriterionMerger.OR);

		factory.criteria.forEach(advancementBuilder::criterion);

		AdvancementEntry advancement = advancementBuilder.build(advancementId);

		String group = Objects.requireNonNullElse(factory.group, "");
		ItemStack output = new ItemStack(factory.getOutputItem(), factory.count);

		CuttingRecipe recipe = factory.recipeFactory.create(group, factory.input, output);
		exporter.accept(recipeId, recipe, advancement);
	}

	private static Identifier getAdvancementId(Identifier recipeId) {
		return recipeId.withPrefixedPath("recipes/");
	}
}

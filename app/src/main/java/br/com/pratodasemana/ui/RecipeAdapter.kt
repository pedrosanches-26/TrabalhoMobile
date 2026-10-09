package br.com.pratodasemana.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pratodasemana.R
import br.com.pratodasemana.data.Recipe
import br.com.pratodasemana.data.RecipeListItem
import br.com.pratodasemana.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit,
) : ListAdapter<RecipeListItem, RecipeAdapter.ViewHolder>(RecipeDiff) {

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val recipe = item.recipe
        val context = holder.itemView.context

        with(holder.binding) {
            recipeImage.setImageResource(recipe.imageRes)
            recipeImage.contentDescription = context.getString(R.string.recipe_image_description, recipe.name)
            recipeName.text = recipe.name

            // Valores opcionais: `null` vira um texto padrão.
            val area = recipe.area ?: context.getString(R.string.area_unknown)
            val time = recipe.prepTimeMinutes?.let { context.getString(R.string.prep_time_format, it) }
                ?: context.getString(R.string.prep_time_unknown)
            recipeMeta.text = context.getString(R.string.meta_format, recipe.category, area, time)

            plannedBadge.isVisible = item.planned
            root.setOnClickListener { onRecipeClick(recipe) }
        }
    }
}

private object RecipeDiff : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean =
        oldItem.recipe.id == newItem.recipe.id

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean =
        oldItem == newItem
}

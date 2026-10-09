package br.com.pratodasemana.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pratodasemana.R
import br.com.pratodasemana.data.MockRecipes
import br.com.pratodasemana.data.Recipe
import br.com.pratodasemana.data.RecipeListItem
import br.com.pratodasemana.databinding.ActivityMainBinding

/** Tela 1: lista de receitas (RecyclerView). Ao tocar em um item, abre a Tela 2 com uma Intent explícita. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = RecipeAdapter { recipe -> openDetail(recipe) }

    // Estado da tela (restaurado após rotação em onSaveInstanceState)
    private var plannedIds: Set<Int> = emptySet()
    private var onlyPlanned: Boolean = false

    /** Recebe o resultado devolvido pela Tela 2 (receita adicionada/removida do plano). */
    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode != RESULT_OK || data == null) return@registerForActivityResult

            val id = data.getIntExtra(DetailActivity.EXTRA_RECIPE_ID, NO_ID)
            if (id == NO_ID) return@registerForActivityResult

            val planned = data.getBooleanExtra(DetailActivity.EXTRA_PLANNED, false)
            plannedIds = if (planned) plannedIds + id else plannedIds - id
            render()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        plannedIds = savedInstanceState?.getIntArray(STATE_PLANNED_IDS)?.toSet() ?: emptySet()
        onlyPlanned = savedInstanceState?.getBoolean(STATE_ONLY_PLANNED) ?: false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets()

        binding.recipeList.layoutManager = LinearLayoutManager(this)
        binding.recipeList.adapter = adapter

        binding.chipOnlyPlanned.isChecked = onlyPlanned
        binding.chipOnlyPlanned.setOnCheckedChangeListener { _, isChecked ->
            onlyPlanned = isChecked
            render()
        }

        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(STATE_PLANNED_IDS, plannedIds.toIntArray())
        outState.putBoolean(STATE_ONLY_PLANNED, onlyPlanned)
    }

    private fun openDetail(recipe: Recipe) {
        val intent = DetailActivity.newIntent(
            context = this,
            recipe = recipe,
            planned = recipe.id in plannedIds,
        )
        detailLauncher.launch(intent)
    }

    private fun render() {
        val items = MockRecipes.all.map { RecipeListItem(recipe = it, planned = it.id in plannedIds) }
        val visible = if (onlyPlanned) items.filter { it.planned } else items

        adapter.submitList(visible)
        binding.emptyText.isVisible = visible.isEmpty()
        binding.summaryText.text =
            getString(R.string.summary_format, MockRecipes.all.size, plannedIds.size)
    }

    private fun applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(left = bars.left, top = bars.top, right = bars.right, bottom = bars.bottom)
            insets
        }
    }

    private companion object {
        const val STATE_PLANNED_IDS = "state_planned_ids"
        const val STATE_ONLY_PLANNED = "state_only_planned"
        const val NO_ID = -1
    }
}

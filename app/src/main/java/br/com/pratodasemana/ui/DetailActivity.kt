package br.com.pratodasemana.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import br.com.pratodasemana.R
import br.com.pratodasemana.data.Recipe
import br.com.pratodasemana.databinding.ActivityDetailBinding

/** Tela 2: detalhes da receita recebida via Intent explícita. Devolve à Tela 1 se ela entrou no plano. */
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var planned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Dado recebido pela Intent (pode ser nulo -> encerra a tela com segurança).
        val recipe = IntentCompat.getParcelableExtra(intent, EXTRA_RECIPE, Recipe::class.java)
        if (recipe == null) {
            finish()
            return
        }

        planned = savedInstanceState?.getBoolean(STATE_PLANNED)
            ?: intent.getBooleanExtra(EXTRA_PLANNED, false)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets()

        bindRecipe(recipe)
        renderPlanState()

        // Na primeira criação adiciona o Fragment; após rotação o FragmentManager o recria sozinho.
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.ingredientsFragmentContainer, IngredientsFragment.newInstance(recipe.ingredients))
            }
        }
        publishResult(recipe)

        binding.planButton.setOnClickListener {
            planned = !planned
            renderPlanState()
            publishResult(recipe)
        }
        binding.backButton.setOnClickListener { finish() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_PLANNED, planned)
    }

    private fun bindRecipe(recipe: Recipe) {
        binding.detailImage.setImageResource(recipe.imageRes)
        binding.detailImage.contentDescription = getString(R.string.recipe_image_description, recipe.name)
        binding.detailName.text = recipe.name

        val area = recipe.area ?: getString(R.string.area_unknown)
        val time = recipe.prepTimeMinutes?.let { getString(R.string.prep_time_format, it) }
            ?: getString(R.string.prep_time_unknown)
        binding.detailMeta.text = getString(R.string.meta_format, recipe.category, area, time)
        binding.instructionsText.text = recipe.instructions
    }

    /** Interação principal: atualiza o status e o texto do botão conforme a receita esteja no plano. */
    private fun renderPlanState() {
        if (planned) {
            binding.planStatus.setText(R.string.status_planned)
            binding.planButton.setText(R.string.action_remove_from_plan)
        } else {
            binding.planStatus.setText(R.string.status_not_planned)
            binding.planButton.setText(R.string.action_add_to_plan)
        }
    }

    /** Devolve o resultado à Tela 1 (setResult fica válido inclusive depois de uma rotação). */
    private fun publishResult(recipe: Recipe) {
        val data = Intent()
            .putExtra(EXTRA_RECIPE_ID, recipe.id)
            .putExtra(EXTRA_PLANNED, planned)
        setResult(RESULT_OK, data)
    }

    private fun applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.detailRoot) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(left = bars.left, top = bars.top, right = bars.right, bottom = bars.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_RECIPE = "extra_recipe"
        const val EXTRA_RECIPE_ID = "extra_recipe_id"
        const val EXTRA_PLANNED = "extra_planned"
        private const val STATE_PLANNED = "state_planned"

        /** Intent explícita (classe de destino definida) com os dados da receita. */
        fun newIntent(context: Context, recipe: Recipe, planned: Boolean): Intent =
            Intent(context, DetailActivity::class.java)
                .putExtra(EXTRA_RECIPE, recipe)
                .putExtra(EXTRA_PLANNED, planned)
    }
}

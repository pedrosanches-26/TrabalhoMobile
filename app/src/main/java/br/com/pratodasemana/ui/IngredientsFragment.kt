package br.com.pratodasemana.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.pratodasemana.R
import br.com.pratodasemana.data.Ingredient
import br.com.pratodasemana.databinding.FragmentIngredientsBinding
import br.com.pratodasemana.databinding.ItemIngredientBinding

/**
 * Fragment com a lista de ingredientes da receita (checklist "o que já tenho em casa").
 * Usa ViewBinding respeitando o ciclo de vida: o binding só existe entre onCreateView e onDestroyView.
 */
class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private var ingredients: List<Ingredient> = emptyList()
    private var checkedIngredients: Set<Int> = emptySet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Argumentos recebidos da Activity (lista vazia se ausentes).
        ingredients = arguments
            ?.let { BundleCompat.getParcelableArrayList(it, ARG_INGREDIENTS, Ingredient::class.java) }
            .orEmpty()
        checkedIngredients = savedInstanceState?.getIntArray(STATE_CHECKED)?.toSet() ?: emptySet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Componente XML reutilizável: um item_ingredient.xml inflado para cada ingrediente.
        binding.ingredientsContainer.removeAllViews()
        ingredients.forEachIndexed { index, ingredient ->
            val item = ItemIngredientBinding.inflate(layoutInflater, binding.ingredientsContainer, false)
            item.ingredientCheck.text = ingredient.name
            item.ingredientMeasure.text = ingredient.measure ?: getString(R.string.measure_unknown)

            item.ingredientCheck.isChecked = index in checkedIngredients
            applyStrikeThrough(item.ingredientCheck, item.ingredientCheck.isChecked)
            item.ingredientCheck.setOnCheckedChangeListener { _, isChecked ->
                checkedIngredients = if (isChecked) checkedIngredients + index else checkedIngredients - index
                applyStrikeThrough(item.ingredientCheck, isChecked)
                renderProgress()
            }
            binding.ingredientsContainer.addView(item.root)
        }
        renderProgress()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(STATE_CHECKED, checkedIngredients.toIntArray())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Evita vazamento da View: o Fragment pode viver mais que a sua View.
        _binding = null
    }

    /** Atualiza o contador conforme o usuário marca os ingredientes. */
    private fun renderProgress() {
        binding.ingredientsProgress.text =
            getString(R.string.ingredients_progress, checkedIngredients.size, ingredients.size)
    }

    private fun applyStrikeThrough(checkBox: CheckBox, strike: Boolean) {
        checkBox.paintFlags = if (strike) {
            checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    companion object {
        private const val ARG_INGREDIENTS = "arg_ingredients"
        private const val STATE_CHECKED = "state_checked"

        fun newInstance(ingredients: List<Ingredient>): IngredientsFragment =
            IngredientsFragment().apply {
                arguments = bundleOf(ARG_INGREDIENTS to ArrayList(ingredients))
            }
    }
}

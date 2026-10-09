package br.com.pratodasemana.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

/** Ingrediente. `measure` é opcional (null = "a gosto"). */
@Parcelize
data class Ingredient(
    val name: String,
    val measure: String?,
) : Parcelable

/**
 * Receita imutável (data class). Os campos opcionais são nullable e a UI trata o `null`.
 * É Parcelable para poder viajar dentro da Intent entre as duas telas.
 */
@Parcelize
data class Recipe(
    val id: Int,
    val name: String,
    val category: String,
    val area: String?,
    val prepTimeMinutes: Int?,
    val ingredients: List<Ingredient>,
    val instructions: String,
    @DrawableRes val imageRes: Int,
) : Parcelable

/** Item exibido na lista: a receita + se ela já está no plano (estado da UI, também imutável). */
data class RecipeListItem(
    val recipe: Recipe,
    val planned: Boolean,
)

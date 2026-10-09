package br.com.pratodasemana.data

import br.com.pratodasemana.R

/** Dados simulados (mocks): nesta etapa não há API nem banco de dados. */
object MockRecipes {

    val all: List<Recipe> = listOf(
        Recipe(
            id = 1,
            name = "Frango grelhado com legumes",
            category = "Frango",
            area = "Brasil",
            prepTimeMinutes = 35,
            ingredients = listOf(
                Ingredient("Peito de frango", "500 g"),
                Ingredient("Abobrinha", "1 unidade"),
                Ingredient("Cenoura", "2 unidades"),
                Ingredient("Azeite", "2 colheres de sopa"),
                Ingredient("Sal e pimenta", null),
            ),
            instructions = "Tempere o frango e deixe descansar por 10 minutos. Grelhe até dourar dos dois lados. " +
                "Salteie os legumes em fatias com azeite e sirva junto.",
            imageRes = R.drawable.img_prato_verde,
        ),
        Recipe(
            id = 2,
            name = "Macarrão ao molho de tomate",
            category = "Massas",
            area = "Itália",
            prepTimeMinutes = 25,
            ingredients = listOf(
                Ingredient("Macarrão espaguete", "300 g"),
                Ingredient("Tomates maduros", "4 unidades"),
                Ingredient("Alho", "2 dentes"),
                Ingredient("Manjericão", "a gosto"),
                Ingredient("Azeite", null),
            ),
            instructions = "Cozinhe a massa em água salgada. Refogue o alho, adicione os tomates picados e deixe " +
                "apurar por 10 minutos. Misture à massa e finalize com manjericão.",
            imageRes = R.drawable.img_prato_vermelho,
        ),
        Recipe(
            id = 3,
            name = "Feijão tropeiro",
            category = "Tradicional",
            area = null, // origem desconhecida: exemplo de valor opcional ausente
            prepTimeMinutes = 50,
            ingredients = listOf(
                Ingredient("Feijão cozido", "3 xícaras"),
                Ingredient("Farinha de mandioca", "1 xícara"),
                Ingredient("Bacon", "150 g"),
                Ingredient("Ovos", "2 unidades"),
                Ingredient("Couve", "1 maço"),
            ),
            instructions = "Frite o bacon, junte os ovos mexidos, o feijão e a couve. " +
                "Por último, acrescente a farinha aos poucos, mexendo sempre.",
            imageRes = R.drawable.img_prato_laranja,
        ),
        Recipe(
            id = 4,
            name = "Omelete de queijo",
            category = "Café da manhã",
            area = "França",
            prepTimeMinutes = null, // tempo opcional ausente
            ingredients = listOf(
                Ingredient("Ovos", "3 unidades"),
                Ingredient("Queijo minas", "50 g"),
                Ingredient("Manteiga", "1 colher de chá"),
                Ingredient("Sal", null),
            ),
            instructions = "Bata os ovos com sal, despeje na frigideira untada e adicione o queijo. " +
                "Dobre ao meio quando firmar e sirva quente.",
            imageRes = R.drawable.img_prato_amarelo,
        ),
        Recipe(
            id = 5,
            name = "Sopa de abóbora",
            category = "Sopas",
            area = "Brasil",
            prepTimeMinutes = 40,
            ingredients = listOf(
                Ingredient("Abóbora cabotiá", "600 g"),
                Ingredient("Cebola", "1 unidade"),
                Ingredient("Caldo de legumes", "1 litro"),
                Ingredient("Gengibre ralado", "1 colher de chá"),
            ),
            instructions = "Refogue a cebola, junte a abóbora em cubos e o caldo. Cozinhe até amolecer, " +
                "bata no liquidificador e acerte o sal.",
            imageRes = R.drawable.img_prato_laranja,
        ),
        Recipe(
            id = 6,
            name = "Salada caprese",
            category = "Saladas",
            area = "Itália",
            prepTimeMinutes = 10,
            ingredients = listOf(
                Ingredient("Tomate", "3 unidades"),
                Ingredient("Muçarela de búfala", "200 g"),
                Ingredient("Folhas de manjericão", null),
                Ingredient("Azeite extravirgem", "a gosto"),
            ),
            instructions = "Fatie o tomate e a muçarela, intercale em um prato, espalhe as folhas de manjericão " +
                "e regue com azeite.",
            imageRes = R.drawable.img_prato_verde,
        ),
    )
}

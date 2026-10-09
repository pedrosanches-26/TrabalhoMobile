# 🍽️ Prato da Semana

Aplicativo Android para **planejar as refeições da semana**. O usuário navega por uma lista de receitas,
abre os detalhes de cada uma (ingredientes e modo de preparo), marca os ingredientes que já tem em casa e
adiciona ou remove a receita do seu plano da semana.

> Entrega **Parcial**: Android Views (XML) + navegação com `Intent` explícita, usando dados simulados (mocks).

---

## 🎯 Objetivo do aplicativo
Ajudar quem quer se organizar na cozinha: **ver receitas → conferir os ingredientes → escolher o que entra no plano da semana**.

Fluxo:
1. **Tela 1 – Lista de receitas** (`MainActivity`): `RecyclerView` com as receitas mockadas, selo "No plano",
   resumo (*X receitas • Y no plano*) e filtro **Somente planejadas** (com mensagem de lista vazia).
2. Ao tocar em uma receita, a **Tela 2 – Detalhes** (`DetailActivity`) é aberta por **Intent explícita**, recebendo a receita.
3. Na Tela 2 o usuário marca os ingredientes que já tem (checklist em um **Fragment**) e toca em
   **Adicionar ao plano da semana** / **Remover do plano**.
4. Ao voltar, a Tela 2 devolve o resultado para a Tela 1, que atualiza o selo e o resumo.

---

## ▶️ Como rodar localmente

**Pré-requisitos**
- Android Studio recente (Narwhal 2025.1.3 ou superior), com **JDK 17+** (o JDK embutido do Android Studio serve).
- **Android SDK Platform 36** (o Android Studio oferece a instalação ao sincronizar).
- Emulador ou aparelho com **Android 8.0 (API 26) ou superior**.
- Internet na primeira execução (para baixar o Gradle e as dependências).

**Pelo Android Studio**
1. Clone o repositório:
   ```bash
   git clone https://github.com/pedrosanches-26/TrabalhoMobile.git
   ```
2. No Android Studio: *File ▸ Open* e selecione a **pasta raiz** do repositório.
3. Aguarde o *Gradle Sync* (o Gradle 8.13 é baixado automaticamente pelo wrapper).
4. Selecione a configuração **app**, escolha o emulador/aparelho e clique em ▶ **Run**.

**Pela linha de comando** (com o Android SDK configurado em `ANDROID_HOME` ou em `local.properties` → `sdk.dir=...`):
```bash
./gradlew assembleDebug      # gera o APK em app/build/outputs/apk/debug/
./gradlew installDebug       # instala no emulador/aparelho conectado
```
No Windows use `gradlew.bat` no lugar de `./gradlew`.

### 🔑 Chaves e segredos
O projeto **não usa nenhuma chave, senha ou API** nesta etapa — todos os dados são mockados em `data/MockRecipes.kt`.
Não é necessário nenhum arquivo extra para rodar.

---

## 📚 Bibliotecas externas

| Biblioteca | Para que é usada |
|---|---|
| **AndroidX Core KTX** | Extensões Kotlin e utilitários de compatibilidade (`IntentCompat`, `BundleCompat`, `WindowInsets`). |
| **AndroidX AppCompat** | `AppCompatActivity`, base das duas telas. |
| **AndroidX Activity KTX** | *Edge-to-edge* e `registerForActivityResult` (receber o resultado da Tela 2). |
| **AndroidX Fragment KTX** | `IngredientsFragment` e `FragmentContainerView`, com a extensão `commit { }`. |
| **Material Components** | Tema Material 3, `MaterialCardView`, `MaterialButton` e `Chip` de filtro. |
| **AndroidX RecyclerView** | Lista de receitas (`ListAdapter` + `DiffUtil`). |
| **Kotlin Parcelize** (plugin) | Gera o `Parcelable` dos modelos para enviá-los dentro da `Intent`. |

---

## 🗂️ Estrutura
```
app/src/main/
├─ java/br/com/pratodasemana/
│  ├─ data/Models.kt            # data classes imutáveis (Recipe, Ingredient, RecipeListItem)
│  ├─ data/MockRecipes.kt       # dados simulados
│  └─ ui/
│     ├─ MainActivity.kt        # Tela 1 (lista)
│     ├─ RecipeAdapter.kt       # adapter do RecyclerView
│     ├─ DetailActivity.kt      # Tela 2 (detalhes)
│     └─ IngredientsFragment.kt # Fragment com o checklist de ingredientes
└─ res/layout/
   ├─ activity_main.xml, activity_detail.xml
   ├─ item_recipe.xml           # item da lista
   ├─ item_ingredient.xml       # componente reutilizável (inflado por ingrediente)
   └─ fragment_ingredients.xml
```

## ✅ Onde cada requisito está atendido

| Requisito | Onde |
|---|---|
| Duas telas em Views/XML com Views (`TextView`, `ImageView`, `Button`, `CheckBox`, `Space`) e ViewGroups (`LinearLayout`, `FrameLayout`, `ScrollView`, `RecyclerView`) | `activity_main.xml`, `activity_detail.xml`, `item_recipe.xml` |
| Navegação por **Intent explícita** com passagem de dados | `DetailActivity.newIntent(...)` (envia a `Recipe` *Parcelable* e se ela está no plano); o retorno volta via `registerForActivityResult` em `MainActivity` |
| Views conectadas ao Kotlin (**ViewBinding**) + interação que atualiza a UI | Botão *Adicionar/Remover do plano*, filtro *Somente planejadas*, checkboxes dos ingredientes com contador |
| Modelos imutáveis (`data class` com `val`) e opcionais tratados | `data/Models.kt` — `area`, `prepTimeMinutes` e `measure` são *nullable* e tratados com `?:` / `?.let` |
| Dados simulados (mocks) | `data/MockRecipes.kt` |
| *Opcional:* apenas ViewBinding (nenhum `findViewById`) | Todas as telas, o adapter e o fragment |
| *Opcional:* componente XML reutilizável inflado em código, com eventos que atualizam a UI | `item_ingredient.xml`, inflado para cada ingrediente em `IngredientsFragment` |
| *Opcional:* Fragment com ciclo de vida + ViewBinding | `IngredientsFragment` (binding criado em `onCreateView`, liberado em `onDestroyView`; estado salvo em `onSaveInstanceState`) |

Extras: o estado das telas (receitas no plano, filtro, ingredientes marcados) é preservado após rotação;
suporte a tema escuro; descrições de conteúdo, títulos marcados como *heading* e áreas de toque ≥ 48dp.

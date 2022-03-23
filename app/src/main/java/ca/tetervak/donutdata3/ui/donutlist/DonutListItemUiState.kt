package ca.tetervak.donutdata3.ui.donutlist

import ca.tetervak.donutdata3.model.Donut

data class DonutListItemUiState(
    val donut: Donut,
    var onEdit: () -> Unit,
    val onDelete: (Donut) -> Unit
    )
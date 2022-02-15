package ca.tetervak.donutdata3.ui.selectimage

data class ImageListItemUiState(
    val fileName: String,
    val isSelected: Boolean,
    val onSelect: () -> Unit
)

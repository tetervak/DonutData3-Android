package ca.tetervak.donutdata3.domain

enum class SortBy {
    SORT_BY_NAME,
    SORT_BY_DATE,
    SORT_BY_RATING,
    SORT_BY_ID
}

fun sortById(list: List<Donut>): List<Donut>{
    return list.sortedBy { it.id }
}

fun sortByName(list: List<Donut>): List<Donut>{
    return list.sortedBy { it.name }
}

fun sortByDate(list: List<Donut>): List<Donut>{
    return list.sortedByDescending { it.date }
}

fun sortByRating(list: List<Donut>): List<Donut>{
    return list.sortedByDescending { it.rating }
}

fun sort(list: List<Donut>, sortBy: SortBy = SortBy.SORT_BY_ID): List<Donut> {
    return when (sortBy) {
        SortBy.SORT_BY_NAME -> sortByName(list)
        SortBy.SORT_BY_DATE -> sortByDate(list)
        SortBy.SORT_BY_RATING -> sortByRating(list)
        else -> sortById(list)
    }
}
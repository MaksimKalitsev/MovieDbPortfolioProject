package ua.zp.gardendirectory.ui.details_screen

import ua.zp.gardendirectory.data.models.MovieData
import ua.zp.gardendirectory.ui.plantsList_screen.RequestState

data class PlantsListState(
    val requestState: RequestState,
    val data: List<MovieData>? = null
)
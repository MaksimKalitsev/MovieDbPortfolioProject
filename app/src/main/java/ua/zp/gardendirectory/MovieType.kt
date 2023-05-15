package ua.zp.gardendirectory

import androidx.annotation.IdRes

enum class MovieType(@IdRes val menuId: Int, val endpoint: String) {
    TOP_RATED(R.id.topRated, "top_rated"),
    POPULAR(R.id.popular, "popular"),
    NOW_PLAYING(R.id.nowPlaying, "now_playing"),
    UPCOMING(R.id.upcoming, "upcoming");

    companion object {
        fun getTypeByMenuId(@IdRes menuId: Int): MovieType? {
            MovieType.values().forEach {
                if (it.menuId == menuId) return it
            }
            return null
        }
    }
}
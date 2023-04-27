package ua.zp.gardendirectory.ui.view_custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ua.zp.gardendirectory.R

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_search, this, true )
    }
}
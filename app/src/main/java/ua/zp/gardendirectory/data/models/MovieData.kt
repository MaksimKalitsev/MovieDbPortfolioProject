package ua.zp.gardendirectory.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.zp.gardendirectory.Confiq.BASE_URL_IMAGES

@Parcelize
data class MovieData(
    val id: Int,
    val title: String?,
    val photo: String?,
    val description: String?
): Parcelable {
    val preview: String?
        get() = photo?.let {
            "$BASE_URL_IMAGES/w500/$photo"
        }
    val originalSize: String?
        get() = photo?.let {
            "$BASE_URL_IMAGES/original/$photo"
        }
}

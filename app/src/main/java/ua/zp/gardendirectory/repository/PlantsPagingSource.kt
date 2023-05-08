package ua.zp.gardendirectory.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ua.zp.gardendirectory.data.models.PlantData
import ua.zp.gardendirectory.data.network.Api
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class PlantsPagingSource(
    private val apiPlant: Api,
    private val query: String = ""
) : PagingSource<Int, PlantData>() {

    override fun getRefreshKey(state: PagingState<Int, PlantData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        val refreshKey = page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
        println(refreshKey)
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlantData> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = if (query.isEmpty()) {
                apiPlant.getPlants(
                    pageIndex = pageIndex
                )
            }else {
                apiPlant.searchPlant(
                    search = query
                )
            }
            val plants = response.data.map { it.toPlantData() }
            val nextKey =
                if (plants.isEmpty() || plants.size % 20 != 0) {
                    null
                } else {
                    pageIndex + 1
                }
            LoadResult.Page(
                data = plants,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }catch (exception: NullPointerException) {
            return LoadResult.Error(exception)
        }
    }
}
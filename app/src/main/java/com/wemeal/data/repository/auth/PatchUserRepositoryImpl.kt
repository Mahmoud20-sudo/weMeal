package com.wemeal.data.repository.auth

import com.google.gson.JsonParseException
import com.wemeal.data.model.Resource
import com.wemeal.data.model.SuccessModel
import com.wemeal.data.repository.datasource.onboarding.PatchUserDataSource
import com.wemeal.domain.repository.auth.PatchUserRepository
import org.json.JSONObject
import retrofit2.Response

class PatchUserRepositoryImpl(private val patchUserDataSource: PatchUserDataSource) :
    PatchUserRepository {
    override suspend fun updateUser(): Resource<SuccessModel> {
        return responseToResource(
            patchUserDataSource.updateUser()
        )
    }

    private fun responseToResource(response: Response<SuccessModel>): Resource<SuccessModel> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(parseErrorMessage(response.errorBody()?.string()))
    }

    private fun parseErrorMessage(error: String?) : String?{
        return try {
            val json = JSONObject(error ?: "")
            json.getString("type")
        }catch (e: JsonParseException){
            e.message
        }
    }
}

//class GetCitiesRepositoryImpl constructor(
//    private val getCitiesDataSource: GetCitiesDataSource?
//) : PagingSource<Int, Result>(), GetCitiesRepository {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
//        return try {
//
//            Log.e("AAa", params.toString())
//            val nextPage = params.key ?: 1
//            val movieListResponse = getCitiesDataSource?.getCities(nextPage)
//
//            LoadResult.Page(
//                data = movieListResponse?.body()?.result!!,
//                prevKey = if (nextPage == 1) null else nextPage - 1,
//                nextKey = if(nextPage == 1) 2 else null
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
//        return null
//    }
//
//    override suspend fun getCities(params: LoadParams<Int>): LoadResult<Int, Result>{
//        TODO("Not yet implemented")
//    }
//}

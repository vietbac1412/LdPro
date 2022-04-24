package tamhoang.ldpro4.data.remote

import io.reactivex.Observable
import retrofit2.http.GET

interface CustomService {
    @GET("appversion.json")
    fun getNewestVersion(): Observable<HashMap<String, String>>
}
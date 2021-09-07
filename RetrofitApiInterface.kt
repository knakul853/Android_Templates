import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterfaceClass {

    @GET("endpoins")
    suspend fun getMethod():ResponseDtoClass

    @GET("/endpoint/{id}") // for paramterized query
    suspend fun getCoinById(@Path("id") id:String): ResponseDtoClass

}

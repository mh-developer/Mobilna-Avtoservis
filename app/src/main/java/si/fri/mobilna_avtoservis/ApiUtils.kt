package si.fri.mobilna_avtoservis

object ApiUtils {

    val BASE_URL = "http://localhost:8000/api/"

    val soService: SOService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(SOService::class.java)
}
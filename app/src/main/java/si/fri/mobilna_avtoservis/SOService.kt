package si.fri.mobilna_avtoservis

import io.reactivex.Observable
import retrofit2.http.GET
import si.fri.mobilna_avtoservis.Models.Reservation

interface SOService {
    @GET("reservations")
    fun getResevarions(): Observable<List<Reservation>>

}
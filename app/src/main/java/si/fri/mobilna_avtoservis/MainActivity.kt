package si.fri.mobilna_avtoservis

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import si.fri.mobilna_avtoservis.Models.Reservation


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var requestQueue: RequestQueue? = null
    val BASE_URL = "http://10.0.2.2:8000/api"

    private var list: ListView? = null
    private var adapter: ListViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        requestQueue = Volley.newRequestQueue(applicationContext)
        list = findViewById<ListView>(R.id.listview)


        // load API reservation data
        loadList()


        // Pass results to ListViewAdapter Class
        adapter = ListViewAdapter(this)
        list!!.adapter = adapter

        list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this@MainActivity,
                "Številka rezervacije: " + reservationsArrayList.get(position).id_rezervacije,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val jsonArrayListener = Response.Listener<JSONArray> { response ->

        for (i in 0 until response.length()) {
            try {
                val `object` = response.getJSONObject(i)
                val id = `object`.getString("id_rezervacije")
                var dodatniOpis: String? = `object`.getString("dodatni_opis")
                var termin: String? = `object`.getString("termin")

                dodatniOpis = if (dodatniOpis == null || dodatniOpis == "null") "" else dodatniOpis
                termin = if (termin == null || termin == "null") "" else termin

                reservationsArrayList.add(
                    Reservation(
                        id,
                        dodatniOpis,
                        termin
                    )
                )

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            adapter = ListViewAdapter(this@MainActivity)
            list = findViewById(R.id.listview)
            list!!.setAdapter(adapter)
        }
    }

    private val errorListener = Response.ErrorListener { error ->
        Log.d("REST error", error.message)
    }

    fun loadList() {

        reservationsArrayList = ArrayList<Reservation>()

        if (!isNetworkAvailable()) {
            val noNetworkToast =
                Toast.makeText(applicationContext, "Ni povezave", Toast.LENGTH_LONG)
            noNetworkToast.show()
        }

        // Potrebno nastaviti link do APIja
        try {
            val url = "${BASE_URL}/reservations"
            val request = JsonArrayRequest(url, jsonArrayListener, errorListener)
            requestQueue?.add(request)
        } catch (e: Exception) {
            val noNetworkToast =
                Toast.makeText(applicationContext, "Ni povezave do API-ja", Toast.LENGTH_LONG)
            noNetworkToast.show()
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    override fun onQueryTextSubmit(query: String): Boolean {

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter!!.filter(newText)
        return false
    }

    companion object {
        var reservationsArrayList = ArrayList<Reservation>()
    }
}

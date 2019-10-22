package si.fri.mobilna_avtoservis

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import si.fri.mobilna_avtoservis.Models.Reservation


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var mService: SOService? = null
    val BASE_URL = "http://localhost:8000/api/"


    private var myAdapter: ListViewAdapter? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var myRetroCryptoArrayList: ArrayList<Reservation>? = null
    private var list: ListView? = null
    private var adapter: ListViewAdapter? = null
    private var editsearch: SearchView? = null
    private var moviewList: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }


        // Generate sample data

        moviewList = arrayOf(
            "Xmen",
            "Titanic",
            "Captain America",
            "Iron man",
            "Rocky",
            "Transporter",
            "Lord of the rings",
            "The jungle book",
            "Tarzan",
            "Cars",
            "Shreck"
        )

        loadData()

        // Locate the ListView in listview_main.xml
        list = findViewById(R.id.listview) as ListView

        movieNamesArrayList = ArrayList()

//        for (i in myRetroCryptoArrayList!!.indices) {
//            val movieNames = Reservation(moviewList!![i])
//            // Binds all strings into an array
//            movieNamesArrayList.add(movieNames)
//        }

        println(myRetroCryptoArrayList)

        // Pass results to ListViewAdapter Class
        adapter = ListViewAdapter(this)

        // Binds the Adapter to the ListView
        list!!.adapter = adapter

        // Locate the EditText in listview_main.xml
//        editsearch = findViewById<SearchView>(R.id.search)
//        editsearch!!.setOnQueryTextListener(this)

        list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this@MainActivity,
                movieNamesArrayList[position].dodatni_opis,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadData() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)

//Specify the converter factory to use for serialization and deserialization//

            .addConverterFactory(GsonConverterFactory.create())

//Add a call adapter factory to support RxJava return types//

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

//Build the Retrofit instance//

            .build().create(SOService::class.java)

//Add all RxJava disposables to a CompositeDisposable//

        myCompositeDisposable?.add(
            requestInterface.getResevarions()

//Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

//Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )

    }

    private fun handleResponse(reservationList: List<Reservation>) {

        myRetroCryptoArrayList = ArrayList(reservationList)

//Set the adapter//

//        cryptocurrency_list.adapter = myAdapter

    }

//    override fun onItemClick(reservation: Reservation) {
//
////If the user clicks on an item, then display a Toast//
//
//        Toast.makeText(this, "Izbrali ste rezervacijo ${reservation.id_rezervacije}", Toast.LENGTH_LONG).show()
//
//    }


    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.clear()

    }

        override fun onQueryTextSubmit(query: String): Boolean {

            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            adapter!!.filter(newText)
            return false
        }

        companion object {
            var movieNamesArrayList = ArrayList<Reservation>()
        }
    }

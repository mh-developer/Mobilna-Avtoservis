package si.fri.mobilna_avtoservis

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import si.fri.mobilna_avtoservis.Models.Reservation
import java.util.*
import kotlin.collections.ArrayList

class ListViewAdapter(
    internal var mContext: Context

) : BaseAdapter() {

    internal var inflater: LayoutInflater
    private val arraylist: ArrayList<Reservation>

    init {
        inflater = LayoutInflater.from(mContext)
        this.arraylist = ArrayList()
        this.arraylist.addAll(MainActivity.reservationsArrayList)
    }

    inner class ViewHolder {
        internal var name: TextView? = null
        internal var subtitle: TextView? = null
        internal var inv: TextView? = null
    }

    override fun getCount(): Int {
        return MainActivity.reservationsArrayList.size
    }

    override fun getItem(position: Int): Reservation {
        return MainActivity.reservationsArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.list_item, null)
            // Locate the TextViews in listview_item.xml
            holder.name = view!!.findViewById(R.id.name) as TextView
            holder.subtitle = view.findViewById(R.id.subtitle) as TextView
            holder.inv = view.findViewById(R.id.inv) as TextView
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        // Set the results into TextViews
        holder.name!!.setText("Številka rezervacije: ${MainActivity.reservationsArrayList[position].id_rezervacije}")
        holder.subtitle!!.setText(MainActivity.reservationsArrayList[position].dodatni_opis.substring(0, 20) + "...")

//        val d = MainActivity.reservationsArrayList[position].termin.split("T")
//        val da = d[0].split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//        holder.inv!!.setText(String.format("%s. %s. %s", da[2], da[1], da[0]))
        holder.inv!!.setText(MainActivity.reservationsArrayList[position].termin)

        return view
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        MainActivity.reservationsArrayList.clear()
        if (charText.length == 0) {
            MainActivity.reservationsArrayList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.dodatni_opis.toLowerCase(Locale.getDefault()).contains(charText)) {
                    MainActivity.reservationsArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

}
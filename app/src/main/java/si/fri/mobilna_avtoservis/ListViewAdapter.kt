package si.fri.mobilna_avtoservis

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
    // Declare Variables

    internal var mContext: Context
) : BaseAdapter() {
    internal var inflater: LayoutInflater
    private val arraylist: ArrayList<Reservation>

    init {
        inflater = LayoutInflater.from(mContext)
        this.arraylist = ArrayList()
        this.arraylist.addAll(MainActivity.movieNamesArrayList)
    }

    inner class ViewHolder {
        internal var name: TextView? = null
    }

    override fun getCount(): Int {
        return MainActivity.movieNamesArrayList.size
    }

    override fun getItem(position: Int): Reservation {
        return MainActivity.movieNamesArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.list_item, null)
            // Locate the TextViews in listview_item.xml
            holder.name = view!!.findViewById(R.id.name) as TextView
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        // Set the results into TextViews
        holder.name!!.setText(MainActivity.movieNamesArrayList[position].dodatni_opis)
        return view
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        MainActivity.movieNamesArrayList.clear()
        if (charText.length == 0) {
            MainActivity.movieNamesArrayList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.dodatni_opis.toLowerCase(Locale.getDefault()).contains(charText)) {
                    MainActivity.movieNamesArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

}
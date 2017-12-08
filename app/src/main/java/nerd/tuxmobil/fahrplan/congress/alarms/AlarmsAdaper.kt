package nerd.tuxmobil.fahrplan.congress.alarms

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.models.Alarm

class AlarmsAdapter(

        var list: List<Alarm>,
        private val onDelete: (alarm: Alarm) -> Unit

) :

        RecyclerView.Adapter<AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.alarm_list_item, parent, false)
        return AlarmViewHolder(itemView, onDelete)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = list[position]
        holder.bind(alarm)
    }

    override fun getItemCount() = list.size

}

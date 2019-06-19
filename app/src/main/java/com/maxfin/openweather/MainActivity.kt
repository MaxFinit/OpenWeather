package com.maxfin.openweather

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var townListAdapter: TownListAdapter? = null
    private var list: List<Town> = ArrayList()
    private var townManager = TownManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        town_recycler_view.layoutManager = LinearLayoutManager(this)
        town_recycler_view.setHasFixedSize(true)



        add_town_fab.setOnClickListener {
            intent = Intent(this, AddTownActivity::class.java)
            startActivity(intent)
        }


    }


    fun updateUi() {
        if (townListAdapter == null) {
            townListAdapter = TownListAdapter(list)
            town_recycler_view.adapter = townListAdapter
        } else {
             townListAdapter!!.setContacts(list)
             townListAdapter!!.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        SomeTask().execute()

    }


    inner class SomeTask : AsyncTask<Void, Void, Long>() {
        override fun doInBackground(vararg params: Void?): Long {

            runOnUiThread{
                progressBar.visibility = View.VISIBLE
            }


            list = townManager.getAllTown()
            val weathermanager = WeatherManager()


            for (item in list) {
                item.weather = weathermanager.loadCurrentWeather(item.name)
            }



            return 0
        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            progressBar.visibility = View.GONE
            updateUi()
        }
    }


    class TownListAdapter(private var townList: List<Town>) : RecyclerView.Adapter<TownListAdapter.TownListHolder>() {


        class TownListHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_view_town, parent, false)) {


            private lateinit var town: Town
            private var townName: TextView = itemView.findViewById(R.id.town_name_text_view)
            private var townTemperature: TextView = itemView.findViewById(R.id.town_temperature_text_view)
            private var windSpeed: TextView = itemView.findViewById(R.id.town_wind_text_view)
            private var windDirection: ImageView = itemView.findViewById(R.id.town_wind_direction_image_view)
            private var deleteTown: ImageButton = itemView.findViewById(R.id.town_delete)


            fun bind(town: Town) {
                this.town = town
                townName.text = town.name
                townTemperature.text = town.weather.temperature
                windSpeed.text = town.weather.windSpeed
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TownListHolder {

            val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
            return TownListHolder(layoutInflater, parent)

        }


        override fun onBindViewHolder(townListHolder: TownListHolder, position: Int) {
            val town = this.townList[position]
            townListHolder.bind(town)
        }


        override fun getItemCount(): Int {
            return townList.size
        }

        fun setContacts(list: List<Town>) {
            this.townList = list

        }

    }


}
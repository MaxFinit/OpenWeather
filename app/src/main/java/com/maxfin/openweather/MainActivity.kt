package com.maxfin.openweather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var townListAdapter: TownListAdapter? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        town_recycler_view.setHasFixedSize(true)


       updateUi()


    }


    fun updateUi(){

        val list: MutableList<Town> = ArrayList()
        val weatherManager : WeatherManager = WeatherManager()
       // list.add(weatherManager.loadCurrentWeather())
weatherManager.loadCurrentWeather()


        if (townListAdapter == null) {
            townListAdapter =  TownListAdapter(list)
            town_recycler_view.adapter = townListAdapter
        } else {
           // townListAdapter.setContacts(mBlockContacts);
           // townListAdapter.notifyDataSetChanged();
        }





    }






    class TownListAdapter(private val townList: List<Town>) : RecyclerView.Adapter<TownListAdapter.TownListHolder>() {


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
                townTemperature.text = town.temperature
                windSpeed.text = town.windSpeed
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

    }


}
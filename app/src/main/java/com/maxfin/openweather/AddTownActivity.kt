package com.maxfin.openweather

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_town.*
import org.json.JSONException

class AddTownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_town)



        addTownButton.setOnClickListener {
            SomeTask().execute()

        }

    }

    inner class SomeTask : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {

            try {
                val townManager = TownManager()
                val weatherManager = WeatherManager()
                val townName = townNameEditText.text.toString()
                townManager.addTown(townName, weatherManager.loadCurrentWeather(townName))


            }catch (e: JSONException){
                return false
            }




            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!){
                Toast.makeText(this@AddTownActivity, "Город успешно добавлен", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this@AddTownActivity, "Ошибка", Toast.LENGTH_SHORT).show()

        }
    }


}

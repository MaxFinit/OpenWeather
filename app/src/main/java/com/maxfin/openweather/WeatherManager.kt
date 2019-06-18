package com.maxfin.openweather

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class WeatherManager {

    private val TAG = "MainActivity"
    private val apiKey = "aff9d742074cb0f517e3a2d83c4c1926"
    private val url = "http://api.openweathermap.org/data/2.5/weather?q=Moscow&appid=$apiKey"

    var okHttpClient: OkHttpClient = OkHttpClient()

    fun loadCurrentWeather(){

        val request : Request = Request.Builder().url(url).build()
        var currentTown : Town? = null
        okHttpClient.newCall(request).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string()
                val temp = (JSONObject(json).getJSONObject("main").get("temp")).toString()
                val name = "Moscow"
                val windSpeed = (JSONObject(json).getJSONObject("wind").get("speed")).toString()
                val windDeg = (JSONObject(json).getJSONObject("wind").get("deg")).toString()


                currentTown  = Town(name,temp,windSpeed,windDeg)




            }
        })



    }






}
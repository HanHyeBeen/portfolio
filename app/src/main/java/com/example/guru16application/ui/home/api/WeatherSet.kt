package com.example.guru16application.ui.home.api

class WeatherSet {

    fun getBaseTime(h : String, m : String) : String {

        var result = ""
        if(m.toInt() < 45) {
            if(h == "00")
                result = "2330"
            else
            {
                var resultH = h.toInt() - 1

                if(resultH < 10)
                    result = "0" + resultH + "30"
                else
                    result = resultH.toString() + "30"
            }
        }
        else
        {
            result = h + "30"
        }
        return result
    }
}
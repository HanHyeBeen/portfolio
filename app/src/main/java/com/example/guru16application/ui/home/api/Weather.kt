package com.example.guru16application.ui.home.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class Weather(val response : Response) {

    data class Response(
        val header: Header,
        val body : Body
    )
    data class Header(
        val resultCode : Int,
        val resultMsa : String
    )

    data class Body(
        val dataType : String,
        val items : Items,
        val totalCount : Int
    )
    data class Items(
        val item : List<Item>
    )
    data class Item(
        val baseDate : String,
        val baseTime : String,
        val category : String,
        val fcstDate : String,
        val fcstTime : String,
        val fcstValue : String,
        val nx : Int,
        val ny : Int
    )
}

data class ModelWeather (
    @SerializedName("rainType") var rainType : String = "",
    @SerializedName("sky") var sky : String = "",
    @SerializedName("temp") var temp : String = "",
    //@SerializedName("fcstTime") var fcstTime : String = "",
)

/*
* < baseDate : 발표 날짜 >
*
* < baseTime (발표 시각) : 기온>
* 0200 / 0500 / 0800 / 1100 / 1400 / 1700 / 2000 / 2300
* 0210 / 0510 / 0810 / 1110 / 1410 / 1710 / 2010 / 2310
* API 제공 시간 ↑
*
* < category 값 참고 >
* 기온 : T3H
* 강수확률 : POP
* 강수형태 : PTY -> 없음0 비1 진눈개비2 눈3 소나기4 빗방울5 빗눈날림6 눈날림7
* 6시간 강수량 : R06 -> 범주나 문자열 표시, R06=6이면 5~9mm
*   ㄴ 0/1미만/1-4/5-9/10-19/20-39/40-69/70이상
* 하늘 상태 : SKY -> 맑음1 구름많음3 흐림4
* 습도 : REH
* 아침 최저 : TMN
* 낮 최고 : TMX
*
* < fcstDate : 예보 날짜 >
* < fcstTime : 예보 시간 >
* < fcstValue : 예보 값 >
* < nx : 예보 지점 x좌표 > : 공릉1동 61 공릉2동 62
* < ny : 예보 지점 y좌표 > : 128
*
* */
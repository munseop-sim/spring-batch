package spring.ms2709.batch.external.dto.datagokr

import java.time.LocalDate

/**
 * @class WeatherDay
 * @author 심문섭, ms2709@a2dcorp.co.kr
 * @version 1.0
 * @since 2024-01-23 오후 5:04
 * @modified
 */

data class WeatherDayParam(
    val targetDate: LocalDate,
    var wcCode:Int
)


data class WeatherDayResult (
    var stnId:String,         //지점 번호
    var stnNm:String,         //지점명
    var tm :String,           //시간
    var avgTa:String,         //평균 기온
    var minTa:String,         //최저 기온
    var minTaHrmt:String,         //최저 기온 시각
    var maxTa:String,         //최고 기온
    var maxTaHrmt:String,         //최고 기온 시각
    var sumRnDur:String,          //강수 계속시간
    var mi10MaxRn:String,         //10분 최다강수량
    var mi10MaxRnHrmt:String,         //10분 최다강수량 시각
    var hr1MaxRn:String,          //1시간 최다강수량
    var hr1MaxRnHrmt:String,          //1시간 최다 강수량 시각
    var sumRn:String,         //일강수량
    var maxInsWs:String,          //최대 순간풍속
    var maxInsWsWd:String,        //최대 순간 풍속 풍향
    var maxInsWsHrmt:String,          //최대 순간풍속 시각
    var maxWs:String,         //최대 풍속
    var maxWsWd:String,           //최대 풍속 풍향
    var maxWsHrmt:String,         //최대 풍속 시각
    var avgWs:String,         //평균 풍속
    var hr24SumRws:String,        //풍정합
    var maxWd:String,         //최다 풍향
    var avgTd	:String,          //평균 이슬점온도
    var minRhm:String,        //최소 상대습도
    var minRhmHrmt:String,        //평균 상대습도 시각
    var avgRhm:String,        //평균 상대습도
    var avgPv:String,         //평균 증기압
    var avgPa:String,         //평균 현지기압
    var maxPs:String,         //최고 해면 기압
    var maxPsHrmt:String,         //최고 해면기압 시각
    var minPs:String,         //최저 해면기압
    var minPsHrmt:String,         //최저 해면기압 시각
    var avgPs:String,         //평균 해면기압
    var ssDur:String,         //가조시간
    var sumSsHr:String,           //합계 일조 시간
    var hr1MaxIcsrHrmt:String,        //1시간 최다 일사 시각
    var hr1MaxIcsr:String,        //1시간 최다 일사량
    var sumGsr:String,        //합계 일사량
    var ddMefs:String,        //일 최심신적설
    var ddMefsHrmt:String,        //일 최심신적설 시각
    var ddMes:String,         //일 최심적설
    var ddMesHrmt:String,         //일 최심적설 시각
    var sumDpthFhsc:String,           //합계 3시간 신적설
    var avgTca:String,        //평균 전운량
    var avgLmac:String,           //평균 중하층운량
    var avgTs:String,         //평균 지면온도
    var minTg:String,         //최저 초상온도
    var avgCm5Te:String,          //평균 5cm 지중온도
    var avgCm10Te:String,         //평균10cm 지중온도
    var avgCm20Te:String,         //평균 20cm 지중온도
    var avgCm30Te:String,         //평균 30cm 지중온도
    var avgM05Te:String,          //0.5m 지중온도
    var avgM10Te:String,          //1.0m 지중온도
    var avgM15Te:String,          //1.5m 지중온도
    var avgM30Te:String,          //3.0m 지중온도
    var avgM50Te:String,          //5.0m 지중온도
    var sumLrgEv:String,          //합계 대형증발량
    var sumSmlEv:String,          //합계 소형증발량
    var n99Rn:String,         //9-9강수
    var iscs:String,          //일기현상
    var sumFogDur:String         //안개 계속 시간
)
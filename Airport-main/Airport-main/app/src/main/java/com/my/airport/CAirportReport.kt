package com.my.airport

class CAirportReport {
    var FlyType: String = "" //Todo: A-入境航班 / D-出境航班
    var AirPortID: String = "" //Todo: 入境/出境機場IATA國際代碼 [RMQ:台中清泉崗機場, TPE:桃園國際機場, KHH: 高雄小港國際機場]，其餘可參考http://www.ting.com.tw/agent/air-code.htm
    var Airline: String = "" //Todo: 航空公司名稱
        var FlightNumber: String = "" //Todo: 航機班號
        var DepartureAirportID: String = "" //Todo:起點機場IATA國際代碼
        var DepartureAirport: String = "" //Todo:起點機場名稱
        var ArrivalAirportID: String = "" //Todo:目的地機場IATA國際代碼
        var ArrivalAirport: String = "" //Todo:目的地機場名稱
    var ScheduleTime: String = "" //Todo:表訂出發/到達時間
        var ActualTime: String = "" //Todo:實際出發/到達時間
        var EstimatedTime: String = "" //Todo:預估出發/到達時間
        var Remark: String = "" //Todo:航班狀態描述
        var Terminal: String = "" //Todo:航廈
        var Gate: String = "" //Todo:登機門
    var UpdateTime: String = "" //Todo:資料更新時間


}
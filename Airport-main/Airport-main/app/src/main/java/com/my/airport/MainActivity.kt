package com.my.airport

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import javax.security.auth.callback.Callback


class MainActivity : AppCompatActivity() {
    var myAirportReport : ArrayList<CAirportReport> = ArrayList<CAirportReport>()
    lateinit var btnFly : Button
    lateinit var btnRate : Button
    lateinit var mRecyclerView : RecyclerView
    lateinit var mLayoutManager : RecyclerView.LayoutManager
    lateinit var mAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            //需要进行的操作
//            super.handleMessage(msg);
//        }
//    };
    public var result : String = ""
    private lateinit var mService: MyServiceDownload
    private var mBound: Boolean = false



    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MyServiceDownload.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, MyServiceDownload::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    fun onButtonClick(v: View) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            //val num: Int = mService.
            //Toast.makeText(this, "number: $num", Toast.LENGTH_SHORT).show()
        }
    }



    public fun UIReport() {

        val jsonArray = JSONArray(result)
        var len = jsonArray.length()
        myAirportReport.clear()
        for (i in 0..len - 1) {
            val item : JSONObject = jsonArray.getJSONObject(i)
            val myTemp : CAirportReport = CAirportReport()
            myTemp.FlyType = item.getString("FlyType")
            when (myTemp.FlyType) {
               "A" -> myTemp.FlyType = "入境航班"
               "D" -> myTemp.FlyType = "出境航班"
            }
            myTemp.AirPortID = "航空公司IATA國際代碼" + item.getString("AirlineID")
            myTemp.Airline = "航空公司名稱" + item.getString("Airline")
            myTemp.FlightNumber = "航機班號" + item.getString("FlightNumber")
            myTemp.DepartureAirportID = "起點機場IATA國際代碼" + item.getString("DepartureAirportID")
            myTemp.DepartureAirport = "起點機場名稱" + item.getString("DepartureAirport")
            myTemp.ArrivalAirportID = "目的地機場IATA國際代碼" + item.getString("ArrivalAirportID")
            myTemp.ArrivalAirport =  "目的地機場名稱" + item.getString("ArrivalAirport")
            myTemp.ScheduleTime = "表訂出發/到達時間" + item.getString("ScheduleTime")
            myTemp.ActualTime = "實際出發/到達時間" + item.getString("ActualTime")
            myTemp.EstimatedTime = "預估出發/到達時間" +item.getString("EstimatedTime")
            myTemp.Remark = "航班狀態描述" + item.getString("Remark")
            myTemp.Terminal = "航廈" + item.getString("Terminal")
            myTemp.Gate = "登機門" + item.getString("Gate")
            myTemp.UpdateTime = "資料更新時間" + item.getString("UpdateTime")
            myAirportReport.add(myTemp)
        }

        mRecyclerView.adapter = MyAdapter(myAirportReport)

    }
    public fun getNetData()  {
        try{
            result = ""
            val url_a = "http://e-traffic.taichung.gov.tw/DataAPI/api/AirPortFlyAPI/A/TPE"
            val url_d = "http://e-traffic.taichung.gov.tw/DataAPI/api/AirPortFlyAPI/D/TPE"
            var resultResponse = ""
            val url = URL(url_a)
            val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            if (200 == urlConnection.getResponseCode()) {

                resultResponse = urlConnection.responseMessage
                var inputStream : InputStream = urlConnection.getInputStream()
                if (null != inputStream) {
                    var reader : InputStreamReader = InputStreamReader(inputStream, "UTF-8")
                    var readerIn : BufferedReader  = BufferedReader(reader)
                    var line : String? = null
                    result = readerIn.readLine()


                }



                runOnUiThread( Runnable() {
                    UIReport()
                })


            }
            else {
                var status = urlConnection.getResponseCode()

            }
        }catch(e: Exception){
            val exception = e;
        }



    }


    inner class MyTimerTask(val str:String) : TimerTask() {
        override fun run() {
            println(str)
            getNetData()

        }

    }
    private fun getData()
    {
        myAirportReport.clear()
        for (i in 0..20) {

            val myTemp : CAirportReport = CAirportReport()
            myTemp.FlyType = "FlyType"
            myTemp.AirPortID = "AirlineID"
            myTemp.Airline = "Airline"
            myTemp.FlightNumber = "FlightNumber=>${i}"
            myTemp.DepartureAirportID = "DepartureAirportID"
            myTemp.DepartureAirport = "DepartureAirport"
            myTemp.ArrivalAirportID = "ArrivalAirportID"
            myTemp.ArrivalAirport =  "ArrivalAirport"
            myTemp.ScheduleTime = "ScheduleTime"
            myTemp.ActualTime = "ActualTime"
            myTemp.EstimatedTime = "EstimatedTime"
            myTemp.Remark = "Remark"
            myTemp.Terminal = "Terminal"
            myTemp.Gate = "Gate"
            myTemp.UpdateTime ="UpdateTime"
            myAirportReport.add(myTemp)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnFly = findViewById(R.id.btnFly)
        btnRate = findViewById(R.id.btnRate)
        mRecyclerView = findViewById(R.id.rvFly)



            mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.setLayoutManager(mLayoutManager)

            //getData()
//        Thread {
//            getNetData()
//        }.start()

        //mAdapter = MyAdapter()

            //傳入資料






        btnFly.setOnClickListener{
//            getData()
//            mRecyclerView.adapter = MyAdapter(myAirportReport)
            Thread {
                getNetData()
            }.start()
//            val task = MyTimerTask("")
//            Timer().schedule(task, Date(), 1000 * 60 * 3)
        }
        btnRate.setOnClickListener{

        }

    }


    }
    class MyAdapter(private  var dataListParam : ArrayList<CAirportReport>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        private val dataList = dataListParam
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {

            return dataList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tvEstimatedTime.text = dataList[position].EstimatedTime
            holder.tvActualTime.text = dataList[position].ActualTime
            holder.tvFlightNumbers.text = dataList[position].FlightNumber
            holder.tvTerminalGate.text = dataList[position].Gate
            holder.tvFlightStatus.text = dataList[position].FlyType
            holder.tvDepartureAirportID.text = dataList[position].DepartureAirportID
            holder.tvDepartureAirport.text = dataList[position].DepartureAirport
            holder.tvArrivalAirportID.text = dataList[position].ArrivalAirportID
            holder.tvArrivalAirport.text = dataList[position].ArrivalAirport


        }

        class MyViewHolder(v : View) : RecyclerView.ViewHolder(v) {
            //        var FlightNumber: String = "" //Todo: 航機班號
//        var DepartureAirportID: String = "" //Todo:起點機場IATA國際代碼
//        var DepartureAirport: String = "" //Todo:起點機場名稱
//        var ArrivalAirportID: String = "" //Todo:目的地機場IATA國際代碼
//        var ArrivalAirport: String = "" //Todo:目的地機場名稱
//        var ScheduleTime: String = "" //Todo:表訂出發/到達時間
//        var ActualTime: String = "" //Todo:實際出發/到達時間
//        var EstimatedTime: String = "" //Todo:預估出發/到達時間
//        var Remark: String = "" //Todo:航班狀態描述
//        var Terminal: String = "" //Todo:航廈
//        var Gate: String = "" //Todo:登機門
            var tvEstimatedTime : TextView = v.findViewById(R.id.tvEstimatedTime)
            var tvActualTime : TextView = v.findViewById(R.id.tvActualTime)
            var tvFlightNumbers : TextView = v.findViewById(R.id.tvFlightNumbers)
            var tvTerminalGate : TextView = v.findViewById(R.id.tvTerminalGate)
            var tvFlightStatus : TextView = v.findViewById(R.id.tvFlightStatus)
            var tvDepartureAirportID : TextView = v.findViewById(R.id.tvDepartureAirportID)
            var tvDepartureAirport : TextView = v.findViewById(R.id.tvDepartureAirport)
            var tvArrivalAirportID : TextView = v.findViewById(R.id.tvArrivalAirportID)
            var tvArrivalAirport : TextView = v.findViewById(R.id.tvArrivalAirport)



    }
}

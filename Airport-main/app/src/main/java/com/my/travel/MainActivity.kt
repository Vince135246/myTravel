package com.my.travel

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.travel.R.id.tvTotal
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {
    var myTravelReport: ArrayList<CTravelReport> = ArrayList<CTravelReport>()
    lateinit var btnLoad: Button
    lateinit var tvTotal: TextView
    lateinit var etTo: EditText
    lateinit var rvLoad: RecyclerView
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>
    var iTotal: String = "*"
    var iTo: String = "1"

    //    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            //需要进行的操作
//            super.handleMessage(msg);
//        }
//    };
    public var result: String = ""


    public fun UIReport() {
        var jsResult = JSONObject(result)
        var strTotal = jsResult.getString("total")
        var jsData = jsResult.getString("data")
        val jsonArray = JSONArray(jsData)
        var len = jsonArray.length()
        myTravelReport.clear()
        var iTotal : Int = Integer.parseInt(strTotal)
        var iPage : Int = (iTotal / 30) + 1
        tvTotal.text = iPage.toString()
        for (i in 0..len - 1) {
            val item: JSONObject = jsonArray.getJSONObject(i)
            val myTemp: CTravelReport = CTravelReport()
            myTemp.name = item.getString("name")

            myTemp.introduction = item.getString("introduction")
            myTemp.address = item.getString("address")
            myTemp.official_site = item.getString("official_site")
            myTemp.facebook = item.getString("facebook")
            myTemp.url = "url : " + item.getString("url")
            myTemp.images = item.getString("images")

            myTravelReport.add(myTemp)
        }

        rvLoad.adapter = MyAdapter(myTravelReport)

    }

    public fun getNetData() {
        try {
            result = ""
            val url_a = "https://www.travel.taipei/open-api/zh-cn/Attractions/All?page=$iTo"
            val url_c =
                "https://www.travel.taipei/open-api/zh-tw/Events/Activity?begin=1979-02-02&end=2024-05-05&page=1"
            val url_d = "http://e-traffic.taichung.gov.tw/DataAPI/api/AirPortFlyAPI/D/TPE"
            var resultResponse = ""
            val url = URL(url_a)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET";
            urlConnection.setRequestProperty("User-Agent", "PostmanRuntime/7.33.0");
            //urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br" );
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("accept", "application/json");
            urlConnection.connect()
            if (200 == urlConnection.getResponseCode()) {

                resultResponse = urlConnection.responseMessage
                var inputStream: InputStream = urlConnection.getInputStream()
                if (null != inputStream) {
                    var readerStream: InputStreamReader = InputStreamReader(inputStream, "UTF-8")
                    var readerIn: BufferedReader = BufferedReader(readerStream)
                    var sb = StringBuilder()
                    while (true) {
                        val line = readerIn.readLine()
                        if (line == null) break
                        sb.append(line)
                    }
                    result = sb.toString()
                    var aaa = result
                    readerIn.close()
                    readerStream.close()
                    inputStream.close()
                    //InputStreamReader(inputStream, "UTF-8")

                }


                runOnUiThread( Runnable() {
                    UIReport()
                })


            } else {
                var status = urlConnection.getResponseCode()

            }
        } catch (e: Exception) {
            val exception = e;
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etTo = findViewById(R.id.etTo)
        tvTotal = findViewById(R.id.tvTotal)
        tvTotal.text = "*"

        btnLoad = findViewById(R.id.btnLoad)
        rvLoad = findViewById(R.id.rvLoad)
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvLoad.setHasFixedSize(true)
        rvLoad.setLayoutManager(mLayoutManager)


//        iTo = etTo.text.toString()
//        Thread {
//            getNetData()
//        }.start()

        //mAdapter = MyAdapter()

        //傳入資料


        btnLoad.setOnClickListener {
            iTo = etTo.text.toString()
            Thread {
                getNetData()
            }.start()
        }

    }

}

    class MyAdapter(private  var dataListParam : ArrayList<CTravelReport>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        private val dataList = dataListParam
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {

            return dataList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tvName.text = dataList[position].name

            var url: Uri? = null
            try {
                url = Uri.parse(dataList[position].url)
                //holder.ivimages.setImageURI(url)
            } catch (e: MalformedURLException) {
                // 处理异常
                e.printStackTrace()
            }


        }

        class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var tvName: TextView = v.findViewById(R.id.tvName)
            var ivImages: ImageView = v.findViewById(R.id.ivImages)


        }
    }


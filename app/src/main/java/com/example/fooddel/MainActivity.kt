package com.example.fooddel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.fooddel.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
      lateinit var foodlist:MutableList<FoodModel>
    private val context:Context=this

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val url = URL(AppData.baseUrl+"foods/getAllFoods.php")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }
        }

        val retro = Retrofit.Builder()
            .baseUrl(AppData.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retro.create(FoodService::class.java)
        val countryRequest = service.listAllFood()

        val response: Response<AllFoodModel> = countryRequest.execute()
       val apiResponse: AllFoodModel? = response.body()
        AppData.AllFoodData=apiResponse

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title=""
        setSupportActionBar(binding.toolbar)
        binding.cardViewFood.setBackgroundResource(R.drawable.upper_rounded_corner);


        //onclick func.
        binding.allFoodImageBtn .setOnClickListener {
            Log.e("Message", "setOnClickListener")
            AppData.whichCategorySelected="All"

            setFoodListNCategoryColorValue(AppData.AllFoodData?.foods!!)
        }
        binding.MealsImageBtn.setOnClickListener {
            AppData.whichCategorySelected="Meals"
            setFoodListNCategoryColorValue(AppData.AllFoodData?.foods!!)

        }
        binding.DessertsImageBtn.setOnClickListener {
            AppData.whichCategorySelected="Desserts"
            setFoodListNCategoryColorValue(AppData.AllFoodData?.foods!!)

        }
        binding.DrinksImageBtn.setOnClickListener {
            AppData.whichCategorySelected="Drinks"
            setFoodListNCategoryColorValue(AppData.AllFoodData?.foods!!)

        }
        setFoodListNCategoryColorValue(AppData.AllFoodData?.foods!!);


    }
    fun setFoodListNCategoryColorValue(  appDataFoodModelList:MutableList<FoodModel>){
       // foodlist= arrayListOf()
        Log.e("Message", "whichCategorySelected:${AppData.whichCategorySelected}")

        when (AppData.whichCategorySelected) {
            "Meals" ->{
                foodlist.clear()
               AppData.tempFoodList= arrayListOf()
                for (foodModel in appDataFoodModelList) {
                    if(foodModel.category=="Meals"){
                        AppData.tempFoodList.add(
                            foodModel
                        )
                    }
                }
                foodlist=AppData.tempFoodList

                binding.MealsFrame.setBackgroundColor(0xFFFFFFFF.toInt());
                binding.AllFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DessertsFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DrinksFrame.setBackgroundColor(0xFFebe8e2.toInt());
            }
            "Desserts" -> {
                foodlist.clear()
                AppData.tempFoodList= arrayListOf()
                for (foodModel in appDataFoodModelList) {
                    if(foodModel.category=="Desserts"){
                        AppData.tempFoodList.add(
                            foodModel
                        )
                    }
                }
                foodlist=AppData.tempFoodList
                binding.DessertsFrame.setBackgroundColor(0xFFFFFFFF.toInt());
                binding.AllFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.MealsFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DrinksFrame.setBackgroundColor(0xFFebe8e2.toInt());
            }
            "Drinks" ->{
                foodlist.clear()
                AppData.tempFoodList= arrayListOf()
                for (foodModel in appDataFoodModelList) {
                    if(foodModel.category=="Drinks"){
                        AppData.tempFoodList.add(
                            foodModel
                        )
                    }
                }
                foodlist=AppData.tempFoodList
                binding.DrinksFrame.setBackgroundColor(0xFFFFFFFF.toInt());
                binding.AllFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DessertsFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.MealsFrame.setBackgroundColor(0xFFebe8e2.toInt());
            }
            else -> {

                binding.AllFrame.setBackgroundColor(0xFFFFFFFF.toInt());
                binding.MealsFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DessertsFrame.setBackgroundColor(0xFFebe8e2.toInt());
                binding.DrinksFrame.setBackgroundColor(0xFFebe8e2.toInt());                if(AppData.AllFoodData!=null&& AppData.AllFoodData?.foods!=null) {
                    Log.v(
                        MainActivity::class.simpleName,
                        "NAME1111 check AppData.AllFoodData!=null"
                    )

                    AppData.tempFoodList= arrayListOf()
                    for (foodModel in appDataFoodModelList) {
                        AppData.tempFoodList.add(
                                foodModel
                            )

                    }
                    foodlist=AppData.tempFoodList

                }else{
                    Log.v(
                        MainActivity::class.simpleName,
                        "NAME1111 check AppData.AllFoodData==null"
                    )
                }
            }
        }

        LinearLayoutManager(this).also { binding.rvFood.layoutManager = it }
        var foodListAdapter=FoodListAdapter(context,foodlist)

        binding.rvFood.adapter=foodListAdapter

        foodListAdapter.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_cart ->{
                if(AppData.cartlist!=null&& AppData.cartlist!!.size!=0){
                Log.e(
                    MainActivity::class.simpleName,
                    "NAME1111 check AppData.AllFoodData==null"
                )
                val intent = Intent(context, CartActivity::class.java)
                // start your next activity
                ContextCompat.startActivity(context, intent, null)
              return true
            }else{
                    Toast.makeText(this, "Cart is empty", Toast.LENGTH_LONG).show()

                    return  true
            }
            }
            R.id.action_order ->{
                val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
                    Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                val gson = Gson()
                val json: String? = sharedPreferences.getString("orderList","")
                // below line is to get the type of our array list.
                val type: Type = object : TypeToken<ArrayList<CartModel?>?>() {}.type

                // in below line we are getting data from gson
                // and saving it to our array list

                var tempList:MutableList<CartModel> = gson.fromJson<Any>(json, type) as ArrayList<CartModel>

                // checking below if the array list is empty or not
                if (tempList == null) {

                    tempList = ArrayList()
                }else{
                    AppData.orderList=tempList
                }

                if(AppData.orderList!=null&& AppData.orderList!!.size!=0){
                    Log.e(
                        MainActivity::class.simpleName,
                        "NAME1111 check AppData.AllFoodData==null"
                    )
                    val intent = Intent(context, OrderHistoryActivity::class.java)
                    // start your next activity
                    ContextCompat.startActivity(context, intent, null)
                    return true
                }else{
                    Toast.makeText(this, "Order history is not present", Toast.LENGTH_LONG).show()

                    return  true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
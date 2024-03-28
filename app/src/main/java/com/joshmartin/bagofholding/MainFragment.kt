package com.joshmartin.bagofholding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.joshmartin.bagofholding.databinding.FragmentMainBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: FragmentMainBinding? = null
private val binding get() = _binding!!

class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var requestQueue: RequestQueue
    private var layoutManger: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerAdapter? = null
    private val bagViewModel: BagViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Override the onViewCreated method to perform data manipulation as this occurs after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding the RecyclerView with a Linear Layout and my xml
        layoutManger = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManger

        //Collecting data for RecyclerAdapter from D&D 5e API
        getItemData()

        //Button to add Item into the Bag of Holding, then update the total weight in the bag
        binding.addItemButton.setOnClickListener {
            var itemClicked: Item? = adapter?.getItem() //Setting up the itemClicked item after selecting an item from the recyclerView

            if (itemClicked != null) { //Check if there is an item selected and the getItem method returned an Item
                var attemptItemWeight = itemClicked.weight
                var bagWeight = bagViewModel.getWeight().value

                //Once the bag is full send a warning toast
                if ((bagWeight!! + attemptItemWeight) > 400.0) {
                    val toast = Toast.makeText(context, "Your bag is full", Toast.LENGTH_LONG)
                    toast.show()
                }
                //Otherwise, add the item
                else {
                    bagViewModel.addToBag(itemClicked)
                    bagViewModel.updateBagWeight()
                }
            }
            //else, if the getItem() method returned null, display message
            else {
                Toast.makeText(context, "No Item Selected", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Function to get the JSON array data and pass it into the recycler view adapter
    fun getItemData() {
        var itemList = ArrayList<Item>(0)
        adapter = RecyclerAdapter(itemList) //Setting the itemList to the recyclerview adapter
        binding.recyclerView.adapter = adapter
        requestQueue = Volley.newRequestQueue(context)
        val url =
            "https://api.open5e.com/v1/weapons/?format=json&limit=68" //URL for open 5e D&D API, sadly only weapons have weight data so far
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val dataArray =
                    response.getJSONArray("results") //get Json array out of Json object
                for (index in 0 until dataArray.length()) { //look through Json array and get objects out of it
                    var itemJSON = dataArray.getJSONObject(index)
                    var name = itemJSON.getString("name")
                    var weight = itemJSON.getString("weight")
                    var cost = itemJSON.getString("cost")
                    var weightNum = weight.split(" ")[0]
                    Log.i("Made it", name)

                    //Parsing data from JSON array, some weights were displayed as fractions, needed to parse as decimal
                    if (weightNum.contains('/')) { //If the weight was fraction, parse this way
                        var weightNumerator = weightNum.split("/")[0].toInt()
                        var weightDen = weightNum.split("/")[1].toInt()
                        var weightCalc: Double = weightNumerator.toDouble() / weightDen.toDouble()
                        var item = Item(name, weightCalc, cost)
                        itemList.add(item)
                        adapter?.notifyDataSetChanged() //Ensure that the adapter is updated as the data is being parsed (This is happening Async)
                    } else { //Else, if the weight was not a fraction, parse this way
                        var weightDec = weightNum.toDouble()
                        var item = Item(name, weightDec, cost)
                        Log.i("Item", item.toString())
                        itemList.add(item)
                        adapter?.notifyDataSetChanged()//Ensure that the adapter is updated as the data is being parsed (This is happening Async)
                    }
                }
            },
            { error ->
                Log.e("Volley Error", error.toString())
            })
        requestQueue.add(jsonObjectRequest)
    }

}

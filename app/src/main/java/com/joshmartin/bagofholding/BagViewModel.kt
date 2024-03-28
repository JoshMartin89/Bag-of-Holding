package com.joshmartin.bagofholding

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//ViewModel to handel shared data between MainFragment and BagFragment
class BagViewModel : ViewModel() {
    //The bag of holding itself
    private val _inBagList = ArrayList<Item>(0)
    //Initialize live data for _BagWeight to accurately display data as _inBagList is changed
    private var _BagWeight: MutableLiveData<Double> = MutableLiveData()

    //Add item to the bag
    fun addToBag (item: Item){
        _inBagList.add(item)
    }

    //Remove an item from the bag at a given position
    fun removeFromBag (position: Int){
        _inBagList.removeAt(position)
    }

    //Returns the objects inside the bag as an arrayList for BagFragment listView
    fun getBagList (): ArrayList<Item> {
        return _inBagList
    }

    //Returns the weight of all items in the bag
    fun getWeight(): MutableLiveData<Double>{
        return _BagWeight
    }

    //Live data that updates the weight as new items are added to or removed from the bag
    fun updateBagWeight(): MutableLiveData<Double> {
        _BagWeight.setValue(0.0) //Clear previous weight

        for(item in _inBagList){ //Add new calculated weight
            var weight = item.weight
            _BagWeight.setValue(weight + _BagWeight.value!!)
        }
        return _BagWeight //Returns Double value of the new weight
    }

}

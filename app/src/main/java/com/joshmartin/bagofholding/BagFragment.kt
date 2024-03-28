package com.joshmartin.bagofholding

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.joshmartin.bagofholding.databinding.FragmentBagBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var _binding: FragmentBagBinding? = null
private val binding get() = _binding!!

class BagFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapter: ArrayAdapter<Item>
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
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Binding ListView to Bag Fragment xml
        var itemListView = binding.listViewBag
        var itemPos: Int = 0


        //Setting up the adapter for ListView
        adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_single_choice, bagViewModel.getBagList())
        itemListView.adapter = adapter
        //Creating onclick listener to get the item index of a clicked item in the List View
        itemListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                itemPos = position
            }

        //Binding the remove button and removing selected item from the BagList
        binding.removeItemButton.setOnClickListener {
            //Checking to make sure the list is not empty before removing an item
            if (bagViewModel.getBagList().isEmpty()) {
                Toast.makeText(context, "No Items to Remove", Toast.LENGTH_LONG).show()
            }
            //Checking to ensure that itemPos is not out of bounds for bagList size if the user tries to remove an item without selecting ViewList
            if (itemPos == bagViewModel.getBagList().size) {
                Toast.makeText(context, "Select an Item", Toast.LENGTH_LONG).show()
            }
            //If there is something to remove, remove it from position that was selected and update the adapter
            else {
                bagViewModel.removeFromBag(itemPos)
                adapter.notifyDataSetChanged() //Update the arrayList in the adapter
                bagViewModel.updateBagWeight() //Update the bag weight
            }
        }
    }

}
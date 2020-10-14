package com.example.madlevel4task1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shopping_list_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingListFragment : Fragment() {

    private lateinit var productRepository: ItemRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val shoppingList = arrayListOf<ShoppingItem>()
    private val productAdapter = ShoppingItemAdapter(shoppingList)


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ItemRepository(requireContext())
        getShoppingListFromDatabase()

        initRv()

        addFab.setOnClickListener {
            showAddProductdialog();
        }
        deleteFab.setOnClickListener {
            removeAllProducts()
        }
    }

    private fun removeAllProducts() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
    }


    @SuppressLint("InflateParams")
    private fun showAddProductdialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_product_dialog_title))
        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.txt_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) { _: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun addProduct(txtProductName: EditText, txtAmount: EditText) {
        if (validateFields(txtProductName, txtAmount)) {
            mainScope.launch {
                val product = ShoppingItem(
                    item = txtProductName.text.toString(),
                    amount = txtAmount.text.toString().toShort()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getShoppingListFromDatabase()
            }
        }
    }

    private fun validateFields(txtProductName: EditText
                               , txtAmount: EditText
    ): Boolean {
        return if (txtProductName.text.toString().isNotBlank()
            && txtAmount.text.toString().isNotBlank()
        ) {
            true
        } else {
            Toast.makeText(activity, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }


    private fun initRv() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvShoppingList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvShoppingList.adapter = productAdapter
        rvShoppingList.setHasFixedSize(true)
        rvShoppingList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        btnAddReminder.setOnClickListener {
//            val reminder = binding.etReminder.text.toString()
//            addReminder(reminder)
//        }


//        viewManager = LinearLayoutManager(activity)
//        rv_shopping_list.addItemDecoration(
//            DividerItemDecoration(
//                activity,
//                DividerItemDecoration.VERTICAL
//            )
//        )
        createItemTouchHelper().attachToRecyclerView(rvShoppingList)

//        rv_shopping_list.apply {
//            setHasFixedSize(true)
//            layoutManager = viewManager
//            adapter = productAdapter
//        }
    }


    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@ShoppingListFragment.shoppingList.clear()
            this@ShoppingListFragment.shoppingList.addAll(shoppingList)
            this@ShoppingListFragment.productAdapter.notifyDataSetChanged()
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = shoppingList[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

}
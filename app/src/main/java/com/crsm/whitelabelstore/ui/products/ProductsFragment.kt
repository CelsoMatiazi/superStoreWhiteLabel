package com.crsm.whitelabelstore.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crsm.whitelabelstore.R
import com.crsm.whitelabelstore.domain.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val viewModel: ProductsViewModel by viewModels()

    private lateinit var productsRecycler : RecyclerView
    private lateinit var fabButton : FloatingActionButton
    private lateinit var swipeProducts : SwipeRefreshLayout
    private val productsAdapter = ProductsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsRecycler = view.findViewById(R.id.recycler_products)
        fabButton = view.findViewById(R.id.fab_add)
        swipeProducts = view.findViewById(R.id.swipe_products)

        setRecyclerView()
        setListeners()
        observeNavBackStack()
        observeVMEvents()

        viewModel.getProducts()
    }


    private fun setRecyclerView(){
        productsRecycler.run {
            setHasFixedSize(true)
            adapter = productsAdapter
        }
    }


    private fun setListeners(){

        swipeProducts.setOnRefreshListener {
            viewModel.getProducts()
        }

        fabButton.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
        }
    }

    private fun observeNavBackStack() {
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.productsFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains("product")) {
                    val product = savedStateHandle.get<Product>("product")
                    val oldList = productsAdapter.getCurrentList()
                    val newList = oldList.toMutableList().apply {
                        if (product != null) {
                            add(product)
                        }
                    }
                    productsAdapter.updateList(newList)
                    productsRecycler.smoothScrollToPosition(newList.size -1)
                    savedStateHandle.remove<Product>("product")
                }
            }

            navBackStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _,event ->
                if(event == Lifecycle.Event.ON_DESTROY){
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }

            })
        }
    }


    private fun observeVMEvents(){
        viewModel.productsData.observe(viewLifecycleOwner) {  products ->
            swipeProducts.isRefreshing = false
            productsAdapter.updateList(products)
        }

        viewModel.addButtonVisibilityData.observe(viewLifecycleOwner){  visibility ->
            fabButton.visibility = visibility

        }
    }


}
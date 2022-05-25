package com.crsm.whitelabelstore.ui.addProduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crsm.whitelabelstore.R
import com.crsm.whitelabelstore.util.CurrencyTextWatcher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {

    private val viewModel: AddProductViewModel by viewModels()
    private lateinit var imageProduct: ImageView
    private lateinit var buttonAddProduct: Button
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputLayoutDescription: TextInputLayout
    private lateinit var inputPrice: TextInputEditText
    private lateinit var inputLayoutPrice: TextInputLayout

    private var imageUri: Uri? = null


    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        imageProduct.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageProduct = view.findViewById(R.id.image_product)
        buttonAddProduct = view.findViewById(R.id.button_add_product)
        inputPrice = view.findViewById(R.id.price)
        inputDescription = view.findViewById(R.id.input_description)
        inputLayoutDescription = view.findViewById(R.id.input_layout_descrition)
        inputLayoutPrice = view.findViewById(R.id.input_layout_price)

        setListeners()
        observeVMEvents()
    }


    private fun observeVMEvents(){
        viewModel.imageUriErrorResId.observe(viewLifecycleOwner){ drawableResId ->
            imageProduct.setBackgroundResource(drawableResId)
        }

        viewModel.descriptionFieldErrorResId.observe(viewLifecycleOwner){
            inputLayoutDescription.setError(it)
        }

        viewModel.priceFieldErrorResId.observe(viewLifecycleOwner){
            inputLayoutPrice.setError(it)
        }

        viewModel.productCreated.observe(viewLifecycleOwner){   product ->
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set("product", product)
                popBackStack()
            }
        }
    }


    private fun TextInputLayout.setError(stringResId: Int?){
        error = if(stringResId != null){
            getString(stringResId)
        }else null
    }


    private fun setListeners(){
        imageProduct.setOnClickListener {
            chooseImage()
        }

        buttonAddProduct.setOnClickListener {
            val description = inputDescription.text.toString()
            val price = inputPrice.text.toString()

            viewModel.createProduct(description, price, imageUri)
        }

        inputPrice.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }

    }


    private fun chooseImage(){
        getImage.launch("image/*")
    }

}
package com.example.meuprimeiroapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meuprimeiroapp.databinding.ActivityItemDetailBinding
import com.example.meuprimeiroapp.model.Item
import com.example.meuprimeiroapp.service.Result
import com.example.meuprimeiroapp.service.RetrofitClient
import com.example.meuprimeiroapp.service.safeApiCall
import com.example.meuprimeiroapp.ui.loadUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * An activity representing a single Item detail screen.
 */
class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    private lateinit var item: Item

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        loadItem()
    }

    /**
     * Sets up the view, including the toolbar.
     */
    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Loads the item data from the API based on the provided item ID.
     */
    private fun loadItem() {
        val itemId = intent.getStringExtra(ARG_ID) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getItem(itemId) }

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        item = result.data
                        handleSuccess()
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this@ItemDetailActivity,
                            "Erro ao buscar o getItem(itemId)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Handles the successful retrieval of item data, updating the UI.
     */
    private fun handleSuccess() {
        binding.name.text = item.value.fullName
        binding.age.text = getString(R.string.item_age, item.value.age)
        binding.profession.setText(item.value.profession)
        binding.image.loadUrl(item.value.imageUrl)
    }

    /**
     * Companion object to provide a factory method for creating the activity's intent.
     */
    companion object {
        const val ARG_ID = "arg_id"

        /**
         * Creates a new intent to start this activity.
         * @param context The context of the calling activity.
         * @param itemId The ID of the item to be displayed.
         * @return An intent to start this activity.
         */
        fun newIntent(context: Context, itemId: String): Intent {
            return Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(ARG_ID, itemId)
            }
        }
    }
}
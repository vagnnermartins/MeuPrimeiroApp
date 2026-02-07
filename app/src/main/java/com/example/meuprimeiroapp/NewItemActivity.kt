package com.example.meuprimeiroapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meuprimeiroapp.databinding.ActivityNewItemBinding
import com.example.meuprimeiroapp.model.ItemValue
import com.example.meuprimeiroapp.service.Result
import com.example.meuprimeiroapp.service.RetrofitClient
import com.example.meuprimeiroapp.service.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

class NewItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.saveCta.setOnClickListener { saveItem() }
    }

    private fun saveItem() {
        if (!validateForm()) return

        CoroutineScope(Dispatchers.IO).launch {
            val id = SecureRandom().nextInt().toString()
            val itemValue = ItemValue(
                id = id,
                name = binding.name.text.toString(),
                surname = binding.surname.text.toString(),
                profession = binding.profession.text.toString(),
                age = binding.age.text.toString().toInt(),
                imageUrl = binding.imageUrl.text.toString(),
                location = null
            )

            val result = safeApiCall { RetrofitClient.apiService.addItem(itemValue) }
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> handleOnSuccess()
                    is Result.Error -> handleOnError()
                }
            }
        }
    }

    private fun handleOnError() {
        Toast.makeText(
            this@NewItemActivity,
            R.string.error_add_item,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleOnSuccess() {
        Toast.makeText(
            this,
            R.string.success_add_item,
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun validateForm(): Boolean {
        var hasError = false
        if (binding.name.text.isNullOrBlank()) {
            binding.name.error = getString(R.string.required_field)
            hasError = true
        }
        if (binding.surname.text.isNullOrBlank()) {
            binding.surname.error = getString(R.string.required_field)
            hasError = true
        }
        if (binding.age.text.isNullOrBlank()) {
            binding.age.error = getString(R.string.required_field)
            hasError = true
        }
        if (binding.imageUrl.text.isNullOrBlank()) {
            binding.imageUrl.error = getString(R.string.required_field)
            hasError = true
        }
        if (binding.profession.text.isNullOrBlank()) {
            binding.profession.error = getString(R.string.required_field)
            hasError = true
        }
        return !hasError
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, NewItemActivity::class.java)
        }

    }
}
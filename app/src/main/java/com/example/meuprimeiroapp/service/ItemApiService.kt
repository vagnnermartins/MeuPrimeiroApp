package com.example.meuprimeiroapp.service

import com.example.meuprimeiroapp.model.Item
import com.example.meuprimeiroapp.model.ItemValue
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit API service for fetching items.
 */
interface ItemApiService {

    /**
     * Fetches a list of all items.
     * @return A list of [Item] objects.
     */
    @GET("items")
    suspend fun getItems(): List<Item>

    /**
     * Fetches a single item by its ID.
     * @param id The ID of the item to fetch.
     * @return The [Item] object.
     */
    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: String): Item
    
    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String)

    @PATCH("items/{id}")
    suspend fun updateItem(@Path("id") id: String, @Body item: ItemValue): Item

    @POST("items")
    suspend fun addItem(@Body item: ItemValue): Item

}
package com.example.meuprimeiroapp.service

import com.example.meuprimeiroapp.model.Item
import retrofit2.http.GET
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

}
package com.bignerdranch.android.json_pr_31_sokolov_ivan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etTags: EditText
    private lateinit var tvResult: TextView

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etTags = findViewById(R.id.etTags)
        tvResult = findViewById(R.id.tvResult)
        val btnSerialize = findViewById<Button>(R.id.btnSerialize)
        val btnDeserialize = findViewById<Button>(R.id.btnDeserialize)

        btnSerialize.setOnClickListener {
            try {
                val name = etName.text.toString()

                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0

                val tagsString = etTags.text.toString()
                val tagsList = if (tagsString.isNotEmpty()) {
                    tagsString.split(",").map { it.trim() }
                } else {
                    emptyList()
                }

                val product = Product(name, price, tagsList)
                val jsonString = gson.toJson(product)

                tvResult.text = "JSON:\n$jsonString"

            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeserialize.setOnClickListener {
            val jsonInput = tvResult.text.toString().removePrefix("JSON:\n")

            if (jsonInput.isEmpty() || jsonInput.startsWith("Результат") || jsonInput.startsWith("Название")) {
                Toast.makeText(this, "Сначала создайте JSON!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val product = gson.fromJson(jsonInput, Product::class.java)

                val resultText = """
                    Название: ${product.name}
                    Цена: ${product.price}
                    Теги: ${product.tags.joinToString(", ")}
                """.trimIndent()

                tvResult.text = resultText

            } catch (e: JsonSyntaxException) {
                tvResult.text = "Ошибка парсинга JSON!\nПроверьте синтаксис."
                Toast.makeText(this, "Ошибка: неверный формат JSON", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                tvResult.text = "Неизвестная ошибка: ${e.message}"
            }
        }
    }
}
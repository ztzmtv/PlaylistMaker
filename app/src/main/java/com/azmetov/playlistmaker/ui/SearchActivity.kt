package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.network.ApiFactory
import com.azmetov.playlistmaker.network.PlaylistResponse
import com.azmetov.playlistmaker.other.Converter
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)

        searchEditText = findViewById(R.id.et_search)
        val searchTextInputLayout = findViewById<TextInputLayout>(R.id.til_search)

        searchEditText.requestFocus()
        hideKeyboard(searchEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && searchEditText.text.isNotEmpty()) {
                ApiFactory.apiService.search(searchEditText.text.toString())
                    .enqueue(object : Callback<PlaylistResponse> {
                        override fun onResponse(
                            call: Call<PlaylistResponse>,
                            response: Response<PlaylistResponse>
                        ) {
                            if (response.code() == 200) {
                                val converter = Converter()
                                response.body()?.results?.map { converter.dtoToEntity(it) }?.apply {
                                    recyclerView.adapter = SearchAdapter(this)
                                }
                            }
                            Log.d("TAG", "${response.body()}")
                        }

                        override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                            Toast.makeText(
                                this@SearchActivity,
                                "${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("TAG", "${t.stackTrace}")
                        }
                    })
                true
            } else false
        }

        searchTextInputLayout.setEndIconOnClickListener {
            searchEditText.text.clear()
            hideKeyboard(searchEditText)
        }


    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val editTextString = searchEditText.text.toString()
        outState.putString(EDIT_TEXT_KEY, editTextString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editTextString = savedInstanceState.getString(EDIT_TEXT_KEY, "")
        searchEditText.setText(editTextString, TextView.BufferType.EDITABLE)
    }

    companion object {
        private const val EDIT_TEXT_KEY = "EDIT_TEXT_KEY"
        fun getIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }
}
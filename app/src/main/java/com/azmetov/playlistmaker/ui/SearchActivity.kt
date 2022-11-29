package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.network.ApiFactory
import com.azmetov.playlistmaker.network.PlaylistResponse
import com.azmetov.playlistmaker.other.Converter
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutUpdate: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setViews()

        val searchTextInputLayout = findViewById<TextInputLayout>(R.id.til_search)
        val btnUpdate = findViewById<Button>(R.id.btn_update)

        btnUpdate.setOnClickListener {
            setAllInvisible()
            sendRequest()
        }

        searchEditText.requestFocus()
        hideKeyboard(searchEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            setAllInvisible()
            if (actionId == EditorInfo.IME_ACTION_DONE && searchEditText.text.isNotEmpty()) {
                sendRequest()
                true
            } else
                false
        }

        searchTextInputLayout.setEndIconOnClickListener {
            searchEditText.text.clear()
            hideKeyboard(searchEditText)
        }
    }

    private fun setViews() {
        recyclerView = findViewById(R.id.searchRecyclerView)
        searchEditText = findViewById(R.id.et_search)
        linearLayoutUpdate = findViewById(R.id.ll_update)
    }

    /**
    1хх — Information носит уведомительный характер и обычно не требует реакции.
    2xx — Success сигнализирует об успешном завершении операции.
    3хх — Redirection указывает на необходимость сделать запрос ресурса по другому адресу.
    4xx — Client error показывает, что запрос не может быть выполнен и содержит ошибку на стороне клиента.
    5хх — Server error показывает, что запрос не может быть выполнен и содержит ошибку на стороне сервера.
     **/

    private fun sendRequest() {
        ApiFactory.apiService.search(searchEditText.text.toString())
            .enqueue(object : Callback<PlaylistResponse> {
                override fun onResponse(
                    call: Call<PlaylistResponse>,
                    response: Response<PlaylistResponse>
                ) {
                    when {
                        response.code() in 200..299 -> {
                            setListVisible(true)
                            if (response.body()?.results?.size != 0) {
                                val converter = Converter()
                                response.body()
                                    ?.results
                                    ?.map { converter.dtoToEntity(it) }
                                    ?.apply { recyclerView.adapter = SearchAdapter(this) }
                            } else {
                                showToast("Поиск не дал результатов")
                            }

                        }
                        response.code() in 400..599 -> {
                            setListVisible(false)
                        }
                    }
                    Log.d("TAG", "${response.body()}")
                }

                override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                    showToast(t.message)
                    setListVisible(false)
                    Log.d("TAG", "${t.stackTrace}")
                }
            })
    }

    private fun setAllInvisible() {
        recyclerView.visibility = View.GONE
        linearLayoutUpdate.visibility = View.GONE
    }

    private fun setListVisible(isListMode: Boolean) {
        if (isListMode) {
            recyclerView.visibility = View.VISIBLE
        } else {
            linearLayoutUpdate.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
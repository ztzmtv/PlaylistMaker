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
    private lateinit var rvSearch: RecyclerView
    private lateinit var llNetError: LinearLayout
    private lateinit var llNothingFound: LinearLayout
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchTextInputLayout: TextInputLayout
    private lateinit var btnUpdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setViews()
        setAdapter()

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
        rvSearch = findViewById(R.id.searchRecyclerView)
        searchEditText = findViewById(R.id.et_search)
        llNetError = findViewById(R.id.ll_update)
        llNothingFound = findViewById(R.id.ll_nothing_found)
        searchTextInputLayout = findViewById(R.id.til_search)
        btnUpdate = findViewById(R.id.btn_update)
    }

    private fun setAdapter() {
        searchAdapter = SearchAdapter()
        rvSearch.adapter = searchAdapter
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
                        response.code() == 200 -> {
                            if (response.body()?.resultCount != 0) {
                                val converter = Converter()
                                response.body()
                                    ?.results
                                    ?.map { converter.dtoToEntity(it) }
                                    ?.apply {
                                        setScreen(SearchScreenState.Result(this))
                                    }
                            } else {
                                setScreen(SearchScreenState.NothingFound)
                            }

                        }
                        response.code() in 400..599 -> {
                            setScreen(SearchScreenState.NetworkError)
                        }
                    }
                    Log.d("TAG", "${response.body()}")
                }

                override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                    setScreen(SearchScreenState.NetworkError)
                    Log.d("TAG", "${t.stackTrace}")
                }
            })
    }

    private fun setScreen(state: SearchScreenState) {
        setAllInvisible()
        when (state) {
            is SearchScreenState.Result -> {
                rvSearch.visibility = View.VISIBLE
                searchAdapter.setTrackList(state.list)
            }
            is SearchScreenState.NetworkError -> {
                llNetError.visibility = View.VISIBLE
            }
            is SearchScreenState.NothingFound -> {
                llNothingFound.visibility = View.VISIBLE
            }
        }
    }

    private fun setAllInvisible() {
        rvSearch.visibility = View.GONE
        llNetError.visibility = View.GONE
        llNothingFound.visibility = View.GONE
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
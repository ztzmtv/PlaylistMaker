package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.network.NetworkDispatcher
import com.google.android.material.textfield.TextInputLayout


class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var rvSearch: RecyclerView
    private lateinit var llNetError: LinearLayout
    private lateinit var llNothingFound: LinearLayout
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchTextInputLayout: TextInputLayout
    private lateinit var btnUpdate: Button
    private lateinit var networkDispatcher: NetworkDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        networkDispatcher = NetworkDispatcher()

        setViews()
        setAdapter()
        setListeners()
    }

    private fun setListeners() {
        btnUpdate.setOnClickListener {
            setAllInvisible()
            networkDispatcher.sendRequest(searchEditText.text.toString()) {
                setScreenState(it)
            }
        }

        searchEditText.requestFocus()
        hideKeyboard(searchEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            setAllInvisible()
            if (actionId == EditorInfo.IME_ACTION_DONE && searchEditText.text.isNotEmpty()) {
                networkDispatcher.sendRequest(searchEditText.text.toString()) {
                    setScreenState(it)
                }
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

    @Suppress("UNCHECKED_CAST")
    private fun setScreenState(state: NetworkState) {
        setAllInvisible()
        when (state) {
            is NetworkState.Result<*> -> {
                rvSearch.visibility = View.VISIBLE
                searchAdapter.setTrackList(state.result as List<Track>)
            }
            is NetworkState.NetworkError -> {
                llNetError.visibility = View.VISIBLE
            }
            is NetworkState.NotFound -> {
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
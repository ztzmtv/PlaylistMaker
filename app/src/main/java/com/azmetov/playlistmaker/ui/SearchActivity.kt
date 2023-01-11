package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.App
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.data.entities.Track
import com.azmetov.playlistmaker.data.network.NetworkDispatcher
import com.azmetov.playlistmaker.data.shared.TrackListSharedStore
import com.google.android.material.textfield.TextInputLayout


class SearchActivity : AppCompatActivity() {

    private lateinit var llNetError: LinearLayout
    private lateinit var llHistory: LinearLayout
    private lateinit var llNothingFound: LinearLayout

    private lateinit var rvSearch: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var btnUpdate: Button
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchEditText: EditText
    private lateinit var btnClearHistory: Button
    private lateinit var searchTextInputLayout: TextInputLayout

    private lateinit var networkDispatcher: NetworkDispatcher
    private lateinit var trackListSharedStore: TrackListSharedStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initObjects()
        setupViews()
        setupAdapters()
        setupListeners()
    }

    private fun initObjects() {
        trackListSharedStore = App.instance.trackSharedStore
        networkDispatcher = App.instance.networkDispatcher
        searchAdapter = App.instance.searchAdapter
    }

    private fun setupAdapters() {
        rvSearch.adapter = searchAdapter
        rvHistory.adapter = searchAdapter
    }

    private fun setupListeners() {
        btnUpdate.setOnClickListener {
            setAllInvisible()
            networkDispatcher.sendRequest(searchEditText.text.toString()) {
                setScreenState(it)
            }
        }
        val arrowBack = findViewById<TextView>(R.id.tv_search_label)
        arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnClearHistory.setOnClickListener {
            trackListSharedStore.clearList()
            val emptyList = mutableListOf<Track>()
            setScreenState(SearchScreenState.History(emptyList))
        }

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

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val list = trackListSharedStore.getTrackList()
                list?.let {
                    setScreenState(SearchScreenState.History(it))
                }
            }
        }
        searchEditText.requestFocus()
        hideKeyboard(searchEditText)

        searchTextInputLayout.setEndIconOnClickListener {
            searchEditText.text.clear()
            hideKeyboard(searchEditText)
        }
    }

    private fun setupViews() {
        llHistory = findViewById(R.id.ll_history)
        llNetError = findViewById(R.id.ll_update)
        llNothingFound = findViewById(R.id.ll_nothing_found)

        rvSearch = findViewById(R.id.searchRecyclerView)
        rvHistory = findViewById(R.id.historyRecyclerView)

        btnUpdate = findViewById(R.id.btn_update)
        btnClearHistory = findViewById(R.id.btn_clear_history)
        searchEditText = findViewById(R.id.et_search)
        searchTextInputLayout = findViewById(R.id.til_search)
    }

    @Suppress("UNCHECKED_CAST")
    private fun setScreenState(state: SearchScreenState) {
        setAllInvisible()
        when (state) {
            is SearchScreenState.Result -> {
                rvSearch.visibility = View.VISIBLE
                searchAdapter.setTrackList(state.result)
                searchAdapter.setTrackClickListener { track ->
                    trackListSharedStore.addTrackToList(track)
                    PlayerActivity.getIntent(this, track).apply {
                        startActivity(this)
                    }
                }
            }
            is SearchScreenState.SearchError -> {
                llNetError.visibility = View.VISIBLE
            }
            is SearchScreenState.NotFound -> {
                llNothingFound.visibility = View.VISIBLE
            }
            is SearchScreenState.History -> {
                llHistory.visibility = View.VISIBLE
                searchAdapter.setTrackList(state.list)
                searchAdapter.setTrackClickListener { track ->
                    PlayerActivity.getIntent(this, track).apply {
                        startActivity(this)
                    }
                }
            }
            SearchScreenState.Loading -> TODO()
        }
    }

    private fun setAllInvisible() {
        rvSearch.visibility = View.GONE
        llHistory.visibility = View.GONE
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
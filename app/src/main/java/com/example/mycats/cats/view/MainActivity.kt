package com.example.mycats.cats.view

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycats.R
import com.example.mycats.cats.data.Cat
import com.example.mycats.cats.viewmodel.CatsViewModel
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<CatsViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CatsAdapter
    private lateinit var cats: ArrayList<Cat>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        recyclerView = findViewById(R.id.recyclerView)
        cats = ArrayList()
        adapter = CatsAdapter(this, cats, object : CatsAdapter.ItemClickListener {
            override fun onItemClicked(url: String) {
                viewFullScreen(url)
            }
        })
        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.toFloat(),
                resources.displayMetrics
        ).roundToInt(), true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        showCats()
    }

    private fun showCats() {
        viewModel.cat.observe(this, Observer {
            progressBar.visibility = View.GONE
            it?.let { it1 ->
                cats.addAll(it1)
            }
            adapter.notifyDataSetChanged()
        })
    }

    private fun viewFullScreen(imageUrl: String) {
        val intent = Intent(this, FullScreenActivity::class.java)
        intent.putExtra("Full Image", imageUrl)
        startActivity(intent)
    }
}
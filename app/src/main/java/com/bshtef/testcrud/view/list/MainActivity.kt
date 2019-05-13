package com.bshtef.testcrud.view.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bshtef.testcrud.R
import com.bshtef.testcrud.view.base.TruckSimpleView
import com.bshtef.testcrud.view.base.TrucksViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : TruckAdapter
    private lateinit var viewModel: TrucksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdapter()
        setupSwipeLayout()
        observeViewModel()
    }

    private fun setupAdapter(){
        adapter = TruckAdapter(object : TruckClickListener {
            override fun onClick(truck: TruckSimpleView) {

            }

            override fun onRemoveClick(truck: TruckSimpleView) {
                viewModel.removeTruck(truck)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }



    private fun setupSwipeLayout(){
        swipeToRefresh.setOnRefreshListener {
            viewModel.getList()
        }
    }

    private fun observeViewModel(){
        viewModel = ViewModelProviders.of(this).get(TrucksViewModel::class.java)
        viewModel.getList()

        viewModel.trucks.observe(this, Observer { list ->
            adapter.submitList(list.toMutableList())

            swipeToRefresh.isRefreshing = false
        })

        viewModel.message.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            swipeToRefresh.isRefreshing = false
        })
    }
}

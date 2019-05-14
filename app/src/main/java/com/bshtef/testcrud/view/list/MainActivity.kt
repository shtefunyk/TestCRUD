package com.bshtef.testcrud.view.list

import android.app.Activity
import android.content.Intent
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
import com.bshtef.testcrud.view.detail.DetailActivity
import com.bshtef.testcrud.view.detail.DetailActivity.Companion.KEY_TRUCK
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
        setupFab()
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CREATE){
            viewModel.addTruckToList(data?.getSerializableExtra(DetailActivity.KEY_TRUCK) as TruckSimpleView)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT){
            viewModel.getList()
        }
    }

    private fun setupAdapter(){
        adapter = TruckAdapter(object : TruckClickListener {
            override fun onClick(truck: TruckSimpleView) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(KEY_TRUCK, truck)
                startActivityForResult(intent, REQUEST_CODE_EDIT)
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

    private fun setupFab(){
        fab.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE)
        }
    }

    private fun observeViewModel(){
        viewModel = ViewModelProviders.of(this).get(TrucksViewModel::class.java)

        swipeToRefresh.isRefreshing = true
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

    companion object {
        const val REQUEST_CODE_CREATE = 1
        const val REQUEST_CODE_EDIT = 2
    }
}

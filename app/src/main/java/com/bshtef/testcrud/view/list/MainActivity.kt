package com.bshtef.testcrud.view.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bshtef.testcrud.R
import com.bshtef.testcrud.utils.getErrorMessage
import com.bshtef.testcrud.view.base.TruckSimpleView
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
            viewModel.getList(this)
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
                viewModel.removeTruck(this@MainActivity, truck)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun setupSwipeLayout(){
        swipeToRefresh.setOnRefreshListener {
            viewModel.getList(this)
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
        viewModel.getList(this)

        viewModel.trucks.observe(this, Observer { list ->
            adapter.submitList(list?.toMutableList())

            swipeToRefresh.isRefreshing = false
        })

        viewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, getErrorMessage(error), Toast.LENGTH_SHORT).show()

            swipeToRefresh.isRefreshing = false
        })

        viewModel.action.observe(this, Observer { action ->
            if (action == Action.SHOW_LIST_AFTER_ADD){
                recyclerView.scrollToPosition(0)
            }
        })
    }

    companion object {
        const val REQUEST_CODE_CREATE = 1
        const val REQUEST_CODE_EDIT = 2
    }
}

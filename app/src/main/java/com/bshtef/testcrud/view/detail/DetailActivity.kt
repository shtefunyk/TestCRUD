package com.bshtef.testcrud.view.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bshtef.testcrud.R
import com.bshtef.testcrud.utils.getErrorMessage
import com.bshtef.testcrud.utils.onTextChanged
import com.bshtef.testcrud.utils.toGone
import com.bshtef.testcrud.utils.toVisible
import com.bshtef.testcrud.view.base.TruckSimpleView
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private var truck: TruckSimpleView? = null
    private var editMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        observeViewModel()
        setupActionBar()
        setupEditTexts()
        validateFields()
        setupTruck()
        setupSaveButton()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    private fun observeViewModel() {
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        viewModel.truck.observe(this, Observer { truck ->
            hideProgress()

            val intent = Intent()
            intent.putExtra(KEY_TRUCK, truck)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

        viewModel.error.observe(this, Observer { error ->
            hideProgress()
            Toast.makeText(this, getErrorMessage(error), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupEditTexts() {
        price.onTextChanged { validateFields() }
        name.onTextChanged { validateFields() }
        comment.onTextChanged { validateFields() }
    }

    private fun validateFields() {
        val condition = price.text.toString().isNotEmpty() &&
                name.text.toString().isNotEmpty() &&
                comment.text.toString().isNotEmpty()

        buttonDone.isEnabled = condition
    }

    private fun setupTruck() {
        truck = intent.getSerializableExtra(KEY_TRUCK) as? TruckSimpleView

        if (truck != null) {
            supportActionBar?.title = getString(R.string.edit_truck)
            name.setText(truck!!.name)
            price.setText(truck!!.price)
            comment.setText(truck!!.comment)

            editMode = true
        }
    }

    private fun setupSaveButton() {
        buttonDone.setOnClickListener {
            showProgress()

            val name = name.text.toString()
            val price = price.text.toString()
            val comment = comment.text.toString()

            if (editMode) {
                viewModel.editTruck(truck?.id!!, name, price, comment)
            } else {
                viewModel.createTruck(name, price, comment)
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showProgress(){
        buttonDone.toGone()
        progress.toVisible()
    }

    private fun hideProgress(){
        buttonDone.toVisible()
        progress.toGone()
    }

    companion object {
        const val KEY_TRUCK = "KEY_TRUCK"
    }

}

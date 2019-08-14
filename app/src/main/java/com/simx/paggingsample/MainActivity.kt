package com.simx.paggingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.simx.paggingsample.data.discover.ResponseMovies
import com.simx.paggingsample.data.paging.AdapterMovie
import com.simx.paggingsample.data.paging.State
import com.simx.paggingsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var vm:MainVM
    private lateinit var adapterMovie: AdapterMovie
    private var yearSelected = 2019
    private var queryData = "food"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        vm = MainVM()
        binding.lifecycleOwner = this
        binding.mainVm = vm
        adapterMovie = AdapterMovie()

        binding.etInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(query: Editable?) {
                if (!query.isNullOrEmpty()){
                    if (query.length > 3){
                        queryData = query.toString()
                        search(queryData,yearSelected)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        binding.year2018.setOnClickListener {
            yearSelected = 2018
            search(queryData,yearSelected)
        }
        binding.year2019.setOnClickListener {
            yearSelected = 2019
            search(queryData,yearSelected)
        }



        vm.movies.observe(this, Observer {

            adapterMovie.submitList(it)

        })
        vm.state.observe(this, Observer {
            when (it) {
                State.DONE -> binding.progress.visibility = View.GONE
                State.LOADING -> binding.progress.visibility = View.VISIBLE
            }
        })
        vm.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        binding.rcv.apply {
            hasFixedSize()
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMovie
        }

    }
    fun search(query:String, year:Int){
        vm.search(query,year)
    }
}

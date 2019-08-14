package com.simx.paggingsample

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.simx.paggingsample.data.discover.ResponseMovies
import com.simx.paggingsample.data.paging.MovieDataSource
import com.simx.paggingsample.data.paging.MovieDataSourceFactory
import com.simx.paggingsample.data.paging.State

/**
 * Created by simx on 14,August,2019
 */
class MainVM:BaseObservable() {
    var movies : LiveData<PagedList<ResponseMovies.ResultsItem>>
    var dataSource: MovieDataSourceFactory
    var  state : LiveData<State> = MutableLiveData()
    var  error : LiveData<String> = MutableLiveData()
    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(30)
            .setEnablePlaceholders(true)
            .build()
        dataSource = MovieDataSourceFactory()
        movies = LivePagedListBuilder(dataSource,config).build()
        state = Transformations.switchMap(dataSource.movieSource,MovieDataSource::state)
        error = Transformations.switchMap(dataSource.movieSource,MovieDataSource::error)
    }

    fun search(query: String, year:Int){

        dataSource.search(query,year)
        state = Transformations.switchMap<MovieDataSource, State>(dataSource.movieSource,MovieDataSource::state)
        error = Transformations.switchMap<MovieDataSource,String>(dataSource.movieSource,MovieDataSource::error)
        movies.value?.dataSource?.invalidate()

    }
}
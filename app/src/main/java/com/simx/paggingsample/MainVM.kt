package com.simx.paggingsample

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.simx.paggingsample.data.discover.ResponseMovies
import com.simx.paggingsample.data.paging.MovieDataSourceFactory

/**
 * Created by simx on 14,August,2019
 */
class MainVM:BaseObservable() {
    var movies : LiveData<PagedList<ResponseMovies.ResultsItem>>
    var dataSource: MovieDataSourceFactory

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(30)
            .setEnablePlaceholders(false)
            .build()
        dataSource = MovieDataSourceFactory()
        movies = LivePagedListBuilder(dataSource,config).build()

    }

    fun search(query: String, year:Int){
        dataSource.search(query,year)
        movies.value?.dataSource?.invalidate()
    }
}
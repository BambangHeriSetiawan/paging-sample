package com.simx.paggingsample.data.paging

import androidx.paging.DataSource
import com.simx.paggingsample.data.discover.ResponseMovies

/**
 * Created by simx on 14,August,2019
 */
class MovieDataSourceFactory(): DataSource.Factory<Int, ResponseMovies.ResultsItem>() {
    /**
     * Create a DataSource.
     *
     *
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     *
     *
     * [LivePagedListBuilder] for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * `LiveData<PagedList>` to observers.
     *
     * @return the new DataSource.
     */

    var q = ""
    var y = 0
    override fun create(): DataSource<Int, ResponseMovies.ResultsItem> {
        return MovieDataSource(q,y)
    }

    fun search(quey:String, year: Int){
        q = quey
        y = year
    }
}
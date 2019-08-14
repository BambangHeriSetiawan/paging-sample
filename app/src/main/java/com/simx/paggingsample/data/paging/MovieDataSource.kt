package com.simx.paggingsample.data.paging


import androidx.paging.PageKeyedDataSource
import com.simx.paggingsample.data.ApiRequests
import com.simx.paggingsample.data.discover.ResponseMovies
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by simx on 14,August,2019
 */
class MovieDataSource(private var query:String, private var year:Int): PageKeyedDataSource<Int, ResponseMovies.ResultsItem>() {

    private var scope: CoroutineScope? = null
    private var job: Job? = SupervisorJob()

    private val parentJob: CoroutineContext = CoroutineExceptionHandler{ _, throwable ->
        run {
            job = SupervisorJob()
        }
    }
    init {
        scope = CoroutineScope(parentJob)
    }
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ResponseMovies.ResultsItem>) {
        scope?.launch {
            var res = ApiRequests.search(1,year,query)
            res.results?.let { callback.onResult(it,null,2) }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseMovies.ResultsItem>) {
        scope?.launch {
            var res = ApiRequests.search(params.key,year,query)
            res.results?.let { callback.onResult(it,params.key + 1) }
        }
    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseMovies.ResultsItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
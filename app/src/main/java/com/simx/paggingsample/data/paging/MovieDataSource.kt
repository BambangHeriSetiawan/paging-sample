package com.simx.paggingsample.data.paging


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.simx.paggingsample.data.ApiRequests
import com.simx.paggingsample.data.discover.ResponseMovies
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * Created by simx on 14,August,2019
 */
class MovieDataSource(private var query:String?, private var year:Int?): PageKeyedDataSource<Int, ResponseMovies.ResultsItem>() {

    private var scope: CoroutineScope? = null
    private var job: Job? = SupervisorJob()
    var state:MutableLiveData<State> = MutableLiveData()
    var error:MutableLiveData<String> = MutableLiveData()
    private val parentJob: CoroutineContext = CoroutineExceptionHandler{ _, throwable ->
        run {
            error.postValue(throwable.message)
            job = SupervisorJob()
            state.postValue(State.DONE)

        }
    }
    init {
        scope = CoroutineScope(parentJob)
        Log.e("MovieDataSource"," -> ${state.value}")
    }
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ResponseMovies.ResultsItem>) {
        try {
            state.postValue(State.LOADING)
            scope?.launch {
                var res = ApiRequests.search(1,year,query)
                res.results?.let {
                    callback.onResult(it,null,2)
                }
            }

        }catch (e:Exception){
            error.postValue(e.message)
        }


    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseMovies.ResultsItem>) {
        try {
            scope?.launch {
                var res = ApiRequests.search(params.key,year,query)
                state.postValue(State.DONE)
                res.results?.let {

                    callback.onResult(it,params.key + 1)
                }

            }
        }catch (e:Exception){
            error.postValue(e.message)
        }

    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResponseMovies.ResultsItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
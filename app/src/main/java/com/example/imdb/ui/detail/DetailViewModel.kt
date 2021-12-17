package com.example.imdb.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb.model.Movie
import com.example.imdb.util.ServiceManager
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {

    var showLiveData= MutableLiveData<Movie>()
    var showError=MutableLiveData<Boolean>().apply {
        false
    }

    fun getMovieDetail(id:Int){
        viewModelScope.launch {
            try {
                val result = ServiceManager.request.getMovieDetail(id)
                showLiveData.postValue(result)
                showError.postValue(false)
                Log.d("sa", result.original_title!!)
            } catch (e: Exception) {
                showError.postValue(true)
                Log.e("hata", "Error", e)
            }
        }
    }
}

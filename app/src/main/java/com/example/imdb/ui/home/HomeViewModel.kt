package com.example.imdb.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb.model.Movie
import com.example.imdb.util.ServiceManager
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var showLiveData=MutableLiveData<ArrayList<Movie>>()
    var showError=MutableLiveData<Boolean>().apply {
        false
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    var showPage=MutableLiveData<Int>().apply {
        value=1
    }

    val text: LiveData<String> = _text




    fun getPopularMovies() {

        viewModelScope.launch {

                try {
                    val result = ServiceManager.request.getPopularSeries(page=showPage.value!!)
                    val showList = arrayListOf<Movie>()
                    for (showResult in result.results) {
                        showList.add(showResult)
                    }
                    showError.postValue(false)
                    showLiveData.postValue(showList)
                    Log.e("hata", "Error")
                    Log.d("sa", showList[0].original_title!!)
                } catch (e: Exception) {
                    showPage.value= showPage.value?.minus(1)
                    showError.postValue(true)
                    Log.e("hata", "Error", e)
                }
            }


    }

    fun getSearchMovies(query: String){

        viewModelScope.launch {

            try {
                val result = ServiceManager.request.getSearchSeries(page=showPage.value!!,query= query)
                val showList = arrayListOf<Movie>()
                for (showResult in result.results) {
                    showList.add(showResult)
                }
                showError.postValue(false)
                showLiveData.postValue(showList)
                Log.e("hata", "Error")
                Log.d("sa", showList[0].original_title!!)
            } catch (e: Exception) {
                showPage.value= showPage.value?.minus(1)
                showError.postValue(true)
                Log.e("hata", "Error", e)

            }
        }
    }
}
package com.coroutinerequester.app.ui.restaurants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coroutinerequester.app.data.DataManager
import com.coroutinerequester.app.data.model.Restaurant
import com.coroutinerequester.app.data.model.toPresentation
import com.coroutinerequester.app.presentation.view.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val restaurantsModule = module {
    viewModel { RestaurantsVm(get()) }
}

class RestaurantsVm(dataManager: DataManager) : BaseViewModel(dataManager) {

    var restaurants = MutableLiveData<MutableList<Restaurant>>()

    fun restaurants() {
        viewModelScope.launch {
           coroutinesRequester.request {
               val result = dm.restaurantsRepo.all()
               restaurants.value = result.restaurants.toPresentation()
           }
        }
    }

}

